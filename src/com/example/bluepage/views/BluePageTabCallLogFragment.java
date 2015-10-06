package com.example.bluepage.views;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.example.bluepage.BluePageConfig;
import com.example.bluepage.BluePageConstants;
import com.example.bluepage.R;
import com.example.bluepage.dbmanager.BluePageCallLogDao;
import com.example.bluepage.model.BluePageCallLogModel;
import com.example.bluepage.utils.BaseListSort;
import com.example.bluepage.utils.CNormalDialog;
import com.example.bluepage.utils.CustomTextView;
import com.example.bluepage.utils.DisplayUtils;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluePageTabCallLogFragment extends Fragment implements LoaderCallbacks<List<BluePageCallLogModel>>,
AdapterView.OnItemClickListener {

    static final int OPTION_MENU_WIDTH = 100; // dp

    private static BluePageTabCallLogFragment sInstance;
    private CallLogAdapter mAdapter;
    private BluePageCallLogDao mBluePageCallLogDao;
    private ArrayList<BluePageCallLogListObject> mObjectList = new ArrayList<BluePageCallLogListObject>();
    private ArrayList<BluePageCallLogModel> mBluePageCallLogList = new ArrayList<BluePageCallLogModel>();
    private HashSet<Integer> mSelectedCallLog = new HashSet<Integer>();
    private ListView mListView;
    private CustomTextView mEmptyView;
    protected Loader<List<BluePageCallLogModel>> mLoader;
    private ContentObserver mPttCallLogObserver, mOemCallLogObserver;
    private Context mContext;
    private Long mLastUpdated;
    private DrawerLayout mDrawerLayout;
    private TextView mSpinnerTextView;
    private LinearLayout mDrawerViewLayout;
    private String[] mSpinnerTitles;
    private ListPopupWindow mListOption;
    private int mSpinnerSelection = 0;
    private ImageView mLeftBtn, mRightBtn1, mRightBtn2, mRightBtn3;
    private Button mRightBtn4;
    private boolean mIsSelectableMode;
    private CustomTextView mTitleView;
    private boolean mVisibleLabel = true;
    private CNormalDialog mDialog;

    public static synchronized BluePageTabCallLogFragment getInstance() {
        if (sInstance == null) {
            sInstance = new BluePageTabCallLogFragment();
        }
        return sInstance;
    }

    public static synchronized void removeInstance() {
        sInstance = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsSelectableMode = getActivity().getIntent().getBooleanExtra(BluePageConstants.INTENT_KEY_CALL_LOG_IS_SELECTABLE_MODE, false);
        mBluePageCallLogDao = BluePageCallLogDao.getInstance(mContext);
        mBluePageCallLogDao.setSortingType(BluePageCallLogDao.SORT_BY_DATE + mSpinnerSelection);
        mAdapter = new CallLogAdapter(mIsSelectableMode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bluepage_tab_calllog_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEmptyView = (CustomTextView) getView().findViewById(R.id.calllog_listview_empty);
        mListView = (ListView) getView().findViewById(R.id.calllog_listview);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);
        mListView.setFastScrollEnabled(true);
        mSelectedCallLog.clear();

        mPttCallLogObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                mLoader.startLoading();
            }
        };

        mOemCallLogObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                final Handler callLogUpdateHandler = new Handler(Looper.getMainLooper());
                mBluePageCallLogDao.doCheckOemCallLogUpdated(mLastUpdated, new BluePageCallLogDao.BluePageCallLogDaoCallBack<Void>() {
                    @Override
                    public void onCompleted(Void data) {
                        callLogUpdateHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mLastUpdated = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getLong(
                                    BluePageConfig.PREF_KEY_CALLLOG_UPDATE_TIMESTAMP, 0);
                            }
                        });
                    }

                    @Override
                    public void onFailure(final String data) {
                        callLogUpdateHandler.post(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                });
            }
        };

        mLastUpdated = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getLong(
            BluePageConfig.PREF_KEY_CALLLOG_UPDATE_TIMESTAMP, 0);

        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        mBluePageCallLogDao.doCheckOemCallLogUpdated(mLastUpdated, null);
        // mBluePageCallLogDao.deleteCallLogWithoutSessionInfo(); // ServerSessionID 가 없는경우(발신실패, 크래쉬) DB에서 삭제함. --> 일단 보류

        showButtons();
        if (mIsSelectableMode) {
            updateSelectedNumber();
        }
    }

    @SuppressWarnings("deprecation")
    private void showButtons() {

        LinearLayout defaultActionbarLayout = (LinearLayout) getView().findViewById(R.id.calllog_actionbar_default_layout);
        LinearLayout selectableActionbarLayout = (LinearLayout) getView().findViewById(R.id.calllog_actionbar_selectable_layout);
        if (!mIsSelectableMode) {
            mLeftBtn = (ImageView) getView().findViewById(R.id.calllog_actionbar_default_left_button);
            mRightBtn1 = (ImageView) getView().findViewById(R.id.calllog_actionbar_default_right_button_01);
            mRightBtn2 = (ImageView) getView().findViewById(R.id.calllog_actionbar_default_right_button_02);
            mRightBtn3 = (ImageView) getView().findViewById(R.id.calllog_actionbar_default_right_button_03);
            mRightBtn4 = (Button) getView().findViewById(R.id.calllog_actionbar_selectable_right_txt_button_04);

            defaultActionbarLayout.setVisibility(View.VISIBLE);
            selectableActionbarLayout.setVisibility(View.GONE);
            mTitleView = (CustomTextView) getView().findViewById(R.id.calllog_actionbar_default_title);
            mTitleView.setText(R.string.calllog_title);

            mLeftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(mDrawerViewLayout);
                }
            });

            mRightBtn1.setVisibility(View.GONE);
            mRightBtn2.setVisibility(View.GONE);
            mRightBtn3.setVisibility(View.VISIBLE);
            mRightBtn3.setImageResource(R.drawable.btn_actionbar_trash);
            mRightBtn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getActivity().getIntent();
                    intent.putExtra(BluePageConstants.INTENT_KEY_CALL_LOG_IS_SELECTABLE_MODE, true);
                    startActivity(intent);
                }
            });
            mRightBtn4.setVisibility(View.GONE);
        } else {
            mLeftBtn = (ImageView) getView().findViewById(R.id.calllog_actionbar_selectable_left_button);
            mRightBtn1 = (ImageView) getView().findViewById(R.id.calllog_actionbar_selectable_right_button_01);
            mRightBtn2 = (ImageView) getView().findViewById(R.id.calllog_actionbar_selectable_right_button_02);
            mRightBtn3 = (ImageView) getView().findViewById(R.id.calllog_actionbar_selectable_right_button_03);
            mRightBtn4 = (Button) getView().findViewById(R.id.calllog_actionbar_selectable_right_txt_button_04);

            defaultActionbarLayout.setVisibility(View.GONE);
            selectableActionbarLayout.setVisibility(View.VISIBLE);

            mTitleView = (CustomTextView) getView().findViewById(R.id.calllog_actionbar_selectable_title);
            mTitleView.setText(R.string.calllog_selectable_title_none);

            mLeftBtn.setVisibility(View.VISIBLE);
            mLeftBtn.setImageResource(R.drawable.btn_checkbox_selector);
            mLeftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                    if (v.isSelected()) {
                        setCheckAllCallLogList();
                    } else {
                        setUncheckAllCallLogList();
                    }

                }
            });

            mRightBtn1.setVisibility(View.GONE);
            mRightBtn2.setVisibility(View.GONE);
            mRightBtn3.setVisibility(View.GONE);
            mRightBtn4.setVisibility(View.VISIBLE);
            mRightBtn4.setText(getString(R.string.contacts_actionbar_btn_done));
            mRightBtn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteSeletedItemDialog();
                }
            });
        }

        LinearLayout spinnerLayout = (LinearLayout) getView().findViewById(R.id.calllog_spinner_layout);
        spinnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOption();
            }
        });

        mSpinnerSelection = 0;
        mSpinnerTextView = (TextView) getView().findViewById(R.id.calllog_spinner_text);
        mSpinnerTitles = getResources().getStringArray(R.array.calllog_spinner_array);
        mSpinnerTextView.setText(mSpinnerTitles[mSpinnerSelection]);
        mListOption = new ListPopupWindow(mContext);
        mListOption.setModal(true);
        mListOption.setAnchorView(spinnerLayout);
        mListOption.setWidth(DisplayUtils.dp2px(OPTION_MENU_WIDTH));
        mListOption.setListSelector(getResources().getDrawable(R.drawable.im_popup_menu_ripple));
        mListOption.setOnItemClickListener(mOptionClickListener);
        mListOption.setAdapter(new OptionAdapter(this.getActivity(), R.array.calllog_spinner_array));
    }

    OnItemClickListener mOptionClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String label = ((CustomTextView) view.findViewById(R.id.option_text)).getText().toString();

            if (getString(R.string.calllog_spinner_latest).equals(label)) {
                mVisibleLabel = true;
            } else if (getString(R.string.calllog_spinner_group).equals(label)) {
                mVisibleLabel = false;
            } else if (getString(R.string.calllog_spinner_abc).equals(label)) {
                mVisibleLabel = false;
            } else {
                mVisibleLabel = true;
            }

            mSpinnerSelection = position;
            mSpinnerTextView.setText(mSpinnerTitles[mSpinnerSelection]);
            mListOption.dismiss();
            mBluePageCallLogDao.setSortingType(BluePageCallLogDao.SORT_BY_DATE + mSpinnerSelection);
            mLoader.startLoading();
        }
    };

    private void setCheckAllCallLogList() {
        if (mIsSelectableMode) {
            for (BluePageCallLogModel model : mBluePageCallLogList) {
                mSelectedCallLog.add(model.getId());
            }

            mAdapter.notifyDataSetChanged();
            updateSelectedNumber();
        }
    }

    private void setUncheckAllCallLogList() {
        if (mIsSelectableMode) {
            mSelectedCallLog.clear();

            mAdapter.notifyDataSetChanged();
            updateSelectedNumber();
        }
    }

    private void updateSelectedNumber() {
        if (mIsSelectableMode) {
            int count = mSelectedCallLog.size();
            String titleString = String.format(getResources().getString(R.string.calllog_selectable_title), count);
            mTitleView.setText(titleString);

            if (count > 0) {
                mRightBtn3.setVisibility(View.VISIBLE);
            } else {
                mRightBtn3.setVisibility(View.GONE);
            }
        }
    }

    private void showDeleteSeletedItemDialog() {
        String body;

        if (mIsSelectableMode) {
            if (mSelectedCallLog.size() > 0) {
                body = getString(R.string.calllog_delete_popup_message, mSelectedCallLog.size());
            } else {
                return;
            }
        } else {
            return;
        }

        mDialog = new CNormalDialog(this.getActivity(), CNormalDialog.TYPE_CALL_LOG_DELETE, body);
        mDialog.setOnPositiveListener(new CNormalDialog.OnPositiveListener() {
            @Override
            public void onPositive() {
                doDeleteCallLog();
            }
        });

        mDialog.show();
    }

    private void doDeleteCallLog() {
        if (mSelectedCallLog.size() > 0) {
            final Handler callLogDeleteHandler = new Handler(Looper.getMainLooper());
            mBluePageCallLogDao.deleteByCallLogIdList(new ArrayList<Integer>(mSelectedCallLog), new BluePageCallLogDao.BluePageCallLogDaoCallBack<Void>() {
                @Override
                public void onCompleted(Void data) {
                    callLogDeleteHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showToast(getResources().getString(R.string.calllog_delete_success));
                            Intent intent = getActivity().getIntent();
                            intent.putExtra(BluePageConstants.INTENT_KEY_CALL_LOG_IS_SELECTABLE_MODE, false);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onFailure(final String data) {
                    callLogDeleteHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showToast(getResources().getString(R.string.calllog_delete_failed));
                        }
                    });
                }
            });
        } else {
            showToast(getResources().getString(R.string.calllog_list_delete_no_selection_message));
        }
    }

    private void showRetryDialog(final BluePageCallLogModel callLogModel) {
        String body;

        if (mIsSelectableMode) {
            return;
        }

        body = getString(R.string.calllog_retry_failed_outgoing_call_message);

        mDialog = new CNormalDialog(this.getActivity(), CNormalDialog.TYPE_CALL_LOG_RETRY, body);
        mDialog.setOnPositiveListener(new CNormalDialog.OnPositiveListener() {
            @Override
            public void onPositive() {
                doRetryFailedOutgoingCall(callLogModel);
            }
        });

        mDialog.show();
    }

    private void doRetryFailedOutgoingCall(BluePageCallLogModel callLogModel) {
        /*        Intent intent = new Intent(mContext, callMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setAction(BluePageConstants.INTENT_ACTION_MAKE_CALL);
                intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_SELECTED_LIST, attachScheme(callLogModel.getNumber()));
                intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_PRE_ARRANGED, callLogModel.isPreArgd());

                if (callLogModel.isPreArgd()) {
                    ContactsGroupDao groupDao = ContactsGroupDao.getInstance(mContext);
                    ArrayList<ContactsGroupModel> groups = groupDao.searchGroupId(callLogModel.getNumber());
                    if ((groups != null) && (groups.size() > 0)) {
                        ContactsGroupModel group = groups.get(0);
                        intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_QOE, group.getQoE());
                    } else {
                        intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_QOE, BluePageConstants.DEFAULT_QOE);
                    }
                } else {
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_QOE, callMainActivity.getMyProfile().getQoE());
                }

                if (callLogModel.getCallKind() == BluePageCallLogDao.CAll_KIND_VOICE_PTT) {
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_HDX, true);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_VOICE, true);
                } else if (callLogModel.getCallKind() == BluePageCallLogDao.CAll_KIND_VIDEO_PTT) {
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_HDX, true);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_VOICE, false);
                } else if (callLogModel.getCallKind() == BluePageCallLogDao.CAll_KIND_VOICE_CALL) {
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_HDX, false);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_VOICE, true);
                } else if (callLogModel.getCallKind() == BluePageCallLogDao.CAll_KIND_VIDEO_CALL) {
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_HDX, false);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_VOICE, false);
                }

                startActivity(intent); // going to onNewIntent()
                finish();*/
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getContentResolver().registerContentObserver(BluePageCallLogDao.CONTENT_URI, true, mPttCallLogObserver);
        getActivity().getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, mOemCallLogObserver);
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().getContentResolver().unregisterContentObserver(mPttCallLogObserver);
        getActivity().getContentResolver().unregisterContentObserver(mOemCallLogObserver);

        if (mLoader != null) {
            mLoader.stopLoading();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mBluePageCallLogDao.removeNewTags();
        super.onDestroy();
    }

    @Override
    public Loader<List<BluePageCallLogModel>> onCreateLoader(int arg0, Bundle arg1) {
        mLoader = mBluePageCallLogDao.getLoader();
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<BluePageCallLogModel>> loader, List<BluePageCallLogModel> data) {
        mBluePageCallLogList.clear();
        mObjectList.clear();

        if (data.size() > 0) {

            // Sorting Data List by options.
            // data = sortData(data);
            mBluePageCallLogList.addAll(data);
            setListLabels(data);
        }

        if (!mIsSelectableMode) {
            if (mBluePageCallLogList.isEmpty()) {
                mRightBtn3.setVisibility(View.GONE);
            } else {
                mRightBtn3.setVisibility(View.VISIBLE);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private void setListLabels(List<BluePageCallLogModel> models) {
        String currlabel = null;

        mObjectList.clear();
        for (BluePageCallLogModel callLogModel : models) {
            if (!callLogModel.getListLabel().equalsIgnoreCase(currlabel)) {
                currlabel = callLogModel.getListLabel();
                if (mVisibleLabel) {
                    BluePageCallLogListObject label = new BluePageCallLogListObject(BluePageCallLogListObject.LIST_TYPE_LABEL, null, currlabel);
                    mObjectList.add(label);
                }
            }

            BluePageCallLogListObject calllog = new BluePageCallLogListObject(BluePageCallLogListObject.LIST_TYPE_CALLLOG, callLogModel, currlabel);
            mObjectList.add(calllog);
        }
    }

    private <T> List<T> sortData(List<T> data) {
        CallLogListSort<T> listSort = new CallLogListSort<T>(data);

        return listSort.sort();
    }

    @Override
    public void onLoaderReset(Loader<List<BluePageCallLogModel>> arg0) {
        mBluePageCallLogList.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    };

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    private String attachScheme(String inNumbers) {
        StringBuilder outNumbers = new StringBuilder();
        if (inNumbers != null) {
            String[] array = StringUtils.splitByWholeSeparatorPreserveAllTokens(inNumbers, ";");
            if (array != null) {
                ArrayList<String> numberList = new ArrayList<String>(Arrays.asList(array));
                if (numberList.size() > 0) {
                    outNumbers.append("tel:" + numberList.get(0));
                    for (int i = 1; i < numberList.size(); i++) {
                        outNumbers.append(";tel:" + numberList.get(i));
                    }
                }
            }
        }
        return outNumbers.toString();
    }

    protected void scrollUpToHeader() {
        mListView.setSelectionAfterHeaderView();
    }

    void onOption() {
        if (mListOption.isShowing() == true) {
            mListOption.dismiss();
        } else {
            mListOption.show();
        }
    }

    private void showToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message != null) {
                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    static class OptionItemViewHolder {
        CustomTextView tvItemLabel;
        View divider;
    }

    class OptionAdapter extends BaseAdapter {
        Context context;
        String[] arrays;

        public OptionAdapter(Context context, int listRsc) {
            this.context = context;
            arrays = getResources().getStringArray(listRsc);
        }

        @Override
        public int getCount() {
            if (arrays == null) {
                return 0;
            }
            return arrays.length;
        }

        @Override
        public Object getItem(int position) {
            return arrays[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            OptionItemViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.calllog_option_list_item, null);
                holder = new OptionItemViewHolder();
                holder.tvItemLabel = (CustomTextView) view.findViewById(R.id.option_text);
                holder.divider = view.findViewById(R.id.option_divider);

                view.setTag(holder);
            } else {
                view.destroyDrawingCache();
                holder = (OptionItemViewHolder) view.getTag();
            }

            holder.tvItemLabel.setText(arrays[position]);
            if (position == (getCount() - 1)) {
                holder.divider.setVisibility(View.GONE);
            } else {
                holder.divider.setVisibility(View.VISIBLE);
            }

            return view;
        }
    }

    class CallLogAdapter extends BaseAdapter {

        private LayoutInflater inflator;

        public CallLogAdapter(boolean selectable) {
            this.inflator = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            int nSize = 0;
            nSize = mObjectList.size();

            return nSize;
        }

        @Override
        public BluePageCallLogListObject getItem(int position) {
            return mObjectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return BluePageCallLogListObject.LIST_TYPE_MAX;
        }

        @Override
        public int getItemViewType(int position) {
            BluePageCallLogListObject item = getItem(position);
            int type = item.getType();

            if (type == BluePageCallLogListObject.LIST_TYPE_UNKNOWN) {
                return BluePageCallLogListObject.LIST_TYPE_UNKNOWN;
            } else if (type == BluePageCallLogListObject.LIST_TYPE_LABEL) {
                return BluePageCallLogListObject.LIST_TYPE_LABEL;
            } else if (type == BluePageCallLogListObject.LIST_TYPE_CALLLOG) {
                return BluePageCallLogListObject.LIST_TYPE_CALLLOG;
            }

            return 0;
        }

        protected class ViewHolder {
            ImageView checkbox;
            ImageView mainIcon;
            TextView name;
            ImageView callTypeIcon;
            TextView callTypeLabel;
            TextView number;
            ImageView newIcon;
            TextView datetime;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            int viewType;

            viewType = getItemViewType(position);
            BluePageCallLogListObject item = getItem(position);

            if (viewType == BluePageCallLogListObject.LIST_TYPE_LABEL) {
                view = getLabelView(position, convertView, parent, item);
            } else if (viewType == BluePageCallLogListObject.LIST_TYPE_CALLLOG) {
                view = getCallLogView(position, convertView, parent, item);
            }

            return view;
        }

        private View getLabelView(int position, View convertView, ViewGroup parent, BluePageCallLogListObject obj) {
            View view = convertView;

            if (view == null) {
                view = inflator.inflate(R.layout.calllog_list_item_label, null);
            }

            TextView tv = (TextView) view.findViewById(R.id.calllog_list_label_textview);

            if (obj.getLabel() != null) {
                tv.setText(obj.getLabel());
            } else {
                tv.setText("Unknown Label");
            }
            return view;
        }

        public View getCallLogView(final int position, View convertView, ViewGroup parent, BluePageCallLogListObject obj) {

            View view = convertView;
            if (obj != null) {
                final ViewHolder holder;
                final BluePageCallLogModel calllogModel = (BluePageCallLogModel) obj.getObject();

                if (view == null) {
                    view = inflator.inflate(R.layout.calllog_list_item, null);
                    holder = new ViewHolder();
                    holder.checkbox = (ImageView) view.findViewById(R.id.calllog_item_checkbox);
                    holder.mainIcon = (ImageView) view.findViewById(R.id.calllog_item_main_icon);
                    holder.name = (TextView) view.findViewById(R.id.calllog_item_name);
                    holder.callTypeIcon = (ImageView) view.findViewById(R.id.calllog_item_type_icon);
                    holder.callTypeLabel = (TextView) view.findViewById(R.id.calllog_item_type_label);
                    holder.number = (TextView) view.findViewById(R.id.calllog_item_number);
                    holder.newIcon = (ImageView) view.findViewById(R.id.calllog_item_new_icon);
                    holder.datetime = (TextView) view.findViewById(R.id.calllog_item_datetime);
                    if (mIsSelectableMode) {
                        holder.checkbox.setVisibility(View.VISIBLE);
                    } else {
                        holder.checkbox.setVisibility(View.GONE);
                    }

                    view.setTag(holder);
                } else {
                    view.destroyDrawingCache();
                    holder = (ViewHolder) view.getTag();
                }

                // Check Box
                if (mIsSelectableMode) {
                    holder.checkbox.setSelected(mSelectedCallLog.contains(calllogModel.getId()));
                    holder.checkbox.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int _id = calllogModel.getId();
                            boolean bSelected = !v.isSelected();
                            v.setSelected(bSelected);
                            if (bSelected) {
                                mSelectedCallLog.add(_id);
                            } else {
                                mSelectedCallLog.remove(_id);
                            }

                            notifyDataSetChanged();
                            mLeftBtn.setSelected(false);
                            updateSelectedNumber();
                        }
                    });
                }

                // Main Icon
                if (calllogModel != null) {
                    switch (calllogModel.getCallType()) {
                        case BluePageCallLogDao.CAll_TYPE_OEM_INCOMING:
                        case BluePageCallLogDao.CAll_TYPE_PTT_INCOMING:
                            holder.callTypeIcon.setImageResource(R.drawable.icon_calllog_ic);
                            break;
                        case BluePageCallLogDao.CAll_TYPE_OEM_OUTGOING:
                            holder.callTypeIcon.setImageResource(R.drawable.icon_calllog_og);
                            break;
                        case BluePageCallLogDao.CAll_TYPE_PTT_OUTGOING:
                            if (StringUtils.isNotEmpty(calllogModel.getServerSessionId())) {
                                holder.callTypeIcon.setImageResource(R.drawable.icon_calllog_og);
                            } else {
                                holder.callTypeIcon.setImageResource(R.drawable.icon_calllog_og_failed);
                            }
                            break;
                        case BluePageCallLogDao.CAll_TYPE_OEM_MISSED:
                        case BluePageCallLogDao.CAll_TYPE_PTT_MISSED:
                            holder.callTypeIcon.setImageResource(R.drawable.icon_calllog_miss);
                            break;
                        case BluePageCallLogDao.CAll_TYPE_OEM_VOICEMAIL:
                        case BluePageCallLogDao.CAll_TYPE_OEM_OTHER:
                        case BluePageCallLogDao.CAll_TYPE_PTT_OTHER:
                        default:
                            holder.callTypeIcon.setImageResource(R.drawable.icon_calllog_ic);
                            break;
                    }

                    switch (calllogModel.getCallKind()) {
                        case BluePageCallLogDao.CAll_KIND_VOICE_PTT:
                            holder.mainIcon.setImageResource(R.drawable.icon_calllog_voice_ptt);
                            holder.callTypeLabel.setText(mContext.getString(R.string.calllog_type_label_voice_ptt));
                            break;
                        case BluePageCallLogDao.CAll_KIND_VIDEO_PTT:
                            holder.mainIcon.setImageResource(R.drawable.icon_calllog_movie_ptt);
                            holder.callTypeLabel.setText(mContext.getString(R.string.calllog_type_label_video_ptt));
                            break;
                        case BluePageCallLogDao.CAll_KIND_VOICE_CALL:
                            holder.mainIcon.setImageResource(R.drawable.icon_calllog_voice_call);
                            holder.callTypeLabel.setText(mContext.getString(R.string.calllog_type_label_voice_call));
                            break;
                        case BluePageCallLogDao.CAll_KIND_VIDEO_CALL:
                            holder.mainIcon.setImageResource(R.drawable.icon_calllog_movie_call);
                            holder.callTypeLabel.setText(mContext.getString(R.string.calllog_type_label_video_call));
                            break;
                        case BluePageCallLogDao.CAll_KIND_MESSAGE:
                            holder.mainIcon.setImageResource(R.drawable.icon_calllog_message);
                            holder.callTypeLabel.setText(mContext.getString(R.string.calllog_type_label_message));
                            break;
                        case BluePageCallLogDao.CAll_KIND_OEM_VOICE_CALL:
                            holder.mainIcon.setImageResource(R.drawable.icon_calllog_voice_call);
                            holder.callTypeLabel.setText(mContext.getString(R.string.calllog_type_label_oem_voice_call));
                            break;
                        case BluePageCallLogDao.CAll_KIND_OEM_VIDEO_CALL:
                            holder.mainIcon.setImageResource(R.drawable.icon_calllog_movie_call);
                            holder.callTypeLabel.setText(mContext.getString(R.string.calllog_type_label_oem_video_call));
                            break;
                        case BluePageCallLogDao.CAll_KIND_OTHER:
                        default:
                            holder.mainIcon.setImageResource(R.drawable.icon_calllog_voice_ptt);
                            holder.callTypeLabel.setText(mContext.getString(R.string.calllog_type_label_other));
                            break;
                    }

                    switch (calllogModel.getCallKind()) {
                        case BluePageCallLogDao.CAll_KIND_VOICE_PTT:
                        case BluePageCallLogDao.CAll_KIND_VIDEO_PTT:
                        case BluePageCallLogDao.CAll_KIND_VOICE_CALL:
                        case BluePageCallLogDao.CAll_KIND_VIDEO_CALL:
                        case BluePageCallLogDao.CAll_KIND_MESSAGE:
                            if (StringUtils.isNotEmpty(calllogModel.getName())) {
                                holder.name.setText(calllogModel.getName());
                                holder.number.setText(calllogModel.getNumber());
                            } else {
                                holder.name.setText(calllogModel.getNumber());
                                holder.number.setText(R.string.calllog_no_number_data);
                            }
                            break;
                        case BluePageCallLogDao.CAll_KIND_OEM_VOICE_CALL:
                        case BluePageCallLogDao.CAll_KIND_OEM_VIDEO_CALL:
                        case BluePageCallLogDao.CAll_KIND_OTHER:
                        default:
                            if (StringUtils.isNotEmpty(calllogModel.getName())) {
                                holder.name.setText(calllogModel.getName());
                                holder.number.setText(calllogModel.getFormatted_number());
                            } else {
                                if (StringUtils.isNotEmpty(calllogModel.getFormatted_number())) {
                                    holder.name.setText(calllogModel.getFormatted_number());
                                } else {
                                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                                    PhoneNumber phoneNumber = null;
                                    String formattedNumber = "";

                                    if (StringUtils.isNotEmpty(calllogModel.getNormalized_number())) {
                                        try {
                                            phoneNumber = phoneUtil.parse(calllogModel.getNormalized_number(), "KR");
                                            formattedNumber = phoneUtil.format(phoneNumber, PhoneNumberFormat.NATIONAL);
                                        } catch (NumberParseException e) {
                                            e.printStackTrace();
                                        }

                                        holder.name.setText(formattedNumber);
                                    } else {
                                        if (StringUtils.isNotEmpty(calllogModel.getNumber())) {
                                            try {
                                                phoneNumber = phoneUtil.parse(calllogModel.getNumber(), "KR");
                                                formattedNumber = phoneUtil.format(phoneNumber, PhoneNumberFormat.NATIONAL);
                                            } catch (NumberParseException e) {
                                                e.printStackTrace();
                                            }

                                            holder.name.setText(formattedNumber);
                                        } else {
                                            holder.name.setText(getString(R.string.calllog_no_number_data));
                                        }
                                    }
                                }
                                holder.number.setText(R.string.calllog_no_saved_number);
                            }
                            break;
                    }

                    if (calllogModel.isNew()) {
                        holder.newIcon.setVisibility(View.VISIBLE);
                    } else {
                        holder.newIcon.setVisibility(View.INVISIBLE);
                    }

                    holder.datetime.setText(calllogModel.getStartTimeString());

                    // Add click listener.
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {/*
                                                     if (mObjectList.get(position).getType() == BluePageCallLogListObject.LIST_TYPE_CALLLOG) {
                                                     if (mIsSelectableMode) {
                                                     View checkBox = v.findViewById(R.id.calllog_item_checkbox);
                                                     int _id = calllogModel.getId();
                                                     boolean bSelected = !checkBox.isSelected();
                                                     checkBox.setSelected(bSelected);
                                                     if (bSelected) {
                                                     mSelectedCallLog.add(_id);
                                                     } else {
                                                     mSelectedCallLog.remove(_id);
                                                     }

                                                     notifyDataSetChanged();
                                                     mLeftBtn.setSelected(false);
                                                     updateSelectedNumber();
                                                     } else {
                                                     if (StringUtils.isNotEmpty(calllogModel.getServerSessionId())) {
                                                     Intent intent = new Intent();
                                                     intent.setAction(getPackageName() + BluePageConstants.INTENT_ACTION_CALL_LOG_DETAIL_EVENT);
                                                     intent.putExtra(BluePageConstants.INTENT_KEY_CALL_LOG_IS_OEM_CALL, StringUtils.isNotEmpty(calllogModel.getOemCallId()));
                                                     intent.putExtra(BluePageConstants.INTENT_KEY_CALL_LOG_ID, calllogModel.getId());
                                                     startActivityForResult(intent, BluePageConstants.INTENT_REQUEST_CALL_LOG);
                                                     } else {
                                                     if (calllogModel.getCallType() == BluePageCallLogDao.CAll_TYPE_PTT_OUTGOING) {
                                                     showRetryDialog(calllogModel);
                                                     }
                                                     }
                                                     }
                                                     }*/
                        }
                    });
                }

            }

            return view;
        }
    }

    static private class CallLogListSort<T> extends BaseListSort<T> {

        public CallLogListSort(List<T> lists) {
            super(lists);
            mLists = lists;
        }

        final Comparator<T> mComparator = new Comparator<T>() {
            private final Collator collator = Collator.getInstance();

            @Override
            public int compare(T object1, T object2) {
                if ((object1 == null) || (object2 == null)) {
                    return 0;
                }

                String name1, name2;
                name1 = ((BluePageCallLogModel) object1).getName();
                name2 = ((BluePageCallLogModel) object2).getName();
                return collator.compare(name1, name2);
            }
        };

        public List<T> sort() {
            Collections.sort(mLists, mComparator);
            /*            int offset = 0;
            int size = mLists.size();

            for (int i = 0; i < size; i++) {
                T model = mLists.get(i - offset);
                String label = null;

                label = ((BluePageContactsModel) model).getListLabel();

                if (label != null) {
                    if (label.equals("#")) {
                        mLists.remove(i - offset);
                        mLists.add(model);
                        offset++;
                    }
                }
            }*/

            return mLists;
        }
    }
}
