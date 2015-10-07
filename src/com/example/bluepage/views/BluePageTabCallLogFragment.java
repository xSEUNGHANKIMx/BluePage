package com.example.bluepage.views;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bluepage.BluePageConfig;
import com.example.bluepage.R;
import com.example.bluepage.dbmanager.BluePageCallLogDao;
import com.example.bluepage.model.BluePageCallLogModel;
import com.example.bluepage.utils.BaseListSort;
import com.example.bluepage.utils.CustomTextView;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class BluePageTabCallLogFragment extends Fragment implements LoaderCallbacks<List<BluePageCallLogModel>>,
AdapterView.OnItemClickListener {

    private static BluePageTabCallLogFragment sInstance;
    private CallLogAdapter mAdapter;
    private BluePageCallLogDao mCallLogDao;
    private ArrayList<BluePageCallLogListObject> mObjectList = new ArrayList<BluePageCallLogListObject>();
    private ArrayList<BluePageCallLogModel> mCallLogList = new ArrayList<BluePageCallLogModel>();
    private ListView mListView;
    private CustomTextView mEmptyView;
    protected Loader<List<BluePageCallLogModel>> mLoader;
    private ContentObserver mObserver;
    private Context mContext;
    private Long mLastUpdated;
    private boolean mVisibleLabel = true;

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

        mCallLogDao = BluePageCallLogDao.getInstance(mContext);
        mCallLogDao.setSortingType(BluePageCallLogDao.SORT_BY_DATE);
        mAdapter = new CallLogAdapter();
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

        mObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                final Handler callLogUpdateHandler = new Handler(Looper.getMainLooper());
                mCallLogDao.doCheckCallLogUpdated(mLastUpdated, new BluePageCallLogDao.BluePageCallLogDaoCallBack<Void>() {
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

        getActivity().getSupportLoaderManager().initLoader(BluePageConfig.CALL_LOG_LOADER_ID, null, this);
        mCallLogDao.doCheckCallLogUpdated(mLastUpdated, null);
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, mObserver);
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().getContentResolver().unregisterContentObserver(mObserver);

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
        super.onDestroy();
    }

    @Override
    public Loader<List<BluePageCallLogModel>> onCreateLoader(int arg0, Bundle arg1) {
        mLoader = mCallLogDao.getLoader();
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<BluePageCallLogModel>> loader, List<BluePageCallLogModel> data) {
        mCallLogList.clear();
        mObjectList.clear();

        if (data.size() > 0) {
            mCallLogList.addAll(data);
            setListLabels(data);
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

    @Override
    public void onLoaderReset(Loader<List<BluePageCallLogModel>> arg0) {
        mCallLogList.clear();
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

    protected void scrollUpToHeader() {
        mListView.setSelectionAfterHeaderView();
    }


    class CallLogAdapter extends BaseAdapter {

        private LayoutInflater inflator;

        public CallLogAdapter() {
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
            ImageView thumb;
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
                view = inflator.inflate(R.layout.bluepage_calllog_list_item_label, null);
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
                    view = inflator.inflate(R.layout.bluepage_calllog_list_item, null);
                    holder = new ViewHolder();
                    holder.thumb = (ImageView) view.findViewById(R.id.calllog_item_thumb);
                    holder.name = (TextView) view.findViewById(R.id.calllog_item_name);
                    holder.callTypeIcon = (ImageView) view.findViewById(R.id.calllog_item_type_icon);
                    holder.callTypeLabel = (TextView) view.findViewById(R.id.calllog_item_type_label);
                    holder.number = (TextView) view.findViewById(R.id.calllog_item_number);
                    holder.newIcon = (ImageView) view.findViewById(R.id.calllog_item_new_icon);
                    holder.datetime = (TextView) view.findViewById(R.id.calllog_item_datetime);

                    view.setTag(holder);
                } else {
                    view.destroyDrawingCache();
                    holder = (ViewHolder) view.getTag();
                }

                // Main Icon
                if (calllogModel != null) {
                    switch (calllogModel.getCallType()) {
                        case BluePageCallLogDao.CAll_TYPE_INCOMING:
                            holder.callTypeIcon.setImageResource(R.drawable.icon_calllog_ic);
                            break;
                        case BluePageCallLogDao.CAll_TYPE_OUTGOING:
                            holder.callTypeIcon.setImageResource(R.drawable.icon_calllog_og);
                            break;
                        case BluePageCallLogDao.CAll_TYPE_MISSED:
                            holder.callTypeIcon.setImageResource(R.drawable.icon_calllog_miss);
                            break;
                        case BluePageCallLogDao.CAll_TYPE_VOICEMAIL:
                        case BluePageCallLogDao.CAll_TYPE_OTHER:
                        default:
                            holder.callTypeIcon.setImageResource(R.drawable.icon_calllog_ic);
                            break;
                    }

                    // Set Thumbnail Image
                    if ((calllogModel.getPhoto_id() != null) && !calllogModel.getPhoto_id().equals("0")) {
                        Uri photoUri = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, calllogModel.getPhoto_id());

                        if ((photoUri != null) && !photoUri.toString().isEmpty()) {
                            holder.thumb.setImageURI(photoUri);
                        } else {
                            holder.thumb.setImageResource(R.drawable.img_incomingcall_default);
                        }
                    } else {
                        holder.thumb.setImageResource(R.drawable.img_incomingcall_default);
                    }


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
                                    holder.name.setText(getString(R.string.bluepage_calllog_no_number_data));
                                }
                            }
                        }
                        holder.number.setText(R.string.bluepage_calllog_no_saved_number);
                    }

                    holder.datetime.setText(calllogModel.getStartTimeString());
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
