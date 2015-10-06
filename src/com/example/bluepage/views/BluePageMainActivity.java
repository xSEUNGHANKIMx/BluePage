package com.example.bluepage.views;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.example.bluepage.BluePageConfig;
import com.example.bluepage.BluePageConstants;
import com.example.bluepage.R;
import com.example.bluepage.dbmanager.BluePageContactsDao;
import com.example.bluepage.utils.CustomTextView;
import com.example.bluepage.utils.DisplayUtils;

public class BluePageMainActivity extends FragmentActivity {

    static final int OPTION_MENU_WIDTH = 150; // dp
    static final int TAB_1 = 0;
    static final int TAB_2 = 1;
    static final int TAB_3 = 2;

    private final String[] mTabTitles = { "Dialer", "Call History", "Contacts" };

    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerViewLayout;
    private ListPopupWindow mListOption;
    private BluePageTabWidget mTabWidget;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private BluePageContactsDao mContactsDao;
    private Context mContext;
    private boolean mIsSelectableMode = false, mIsChangedToSelectable = false;
    private int mSelectableType = 0;
    private ImageView mLeftBtn, mRightBtn1, mRightBtn2, mRightBtn3;
    private Button mRightBtn4;
    private CustomTextView mTitleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bluepage_main_activity);

        mContext = getBaseContext();
        mContactsDao = BluePageContactsDao.getInstance(mContext);
        mIsSelectableMode = getIntent().getBooleanExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_SELECTABLE_MODE, false);
        mIsChangedToSelectable = getIntent().getBooleanExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_CHANGED_TO_SELECTABLE, false);
        mSelectableType = getIntent().getIntExtra(BluePageConstants.INTENT_KEY_CONTACTS_SELECTABLE_BUTTON_TYPE, BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_OPTION);

        mTabWidget = (BluePageTabWidget) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabWidget.setViewPager(mViewPager);

        showButtons();
        if (mIsSelectableMode) {
            updateSelectedNumber();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mIsSelectableMode = getIntent().getBooleanExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_SELECTABLE_MODE, false);
        mIsChangedToSelectable = getIntent().getBooleanExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_CHANGED_TO_SELECTABLE, false);
        mSelectableType = getIntent().getIntExtra(BluePageConstants.INTENT_KEY_CONTACTS_SELECTABLE_BUTTON_TYPE, BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_OPTION);

        mTabWidget = (BluePageTabWidget) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabWidget.setViewPager(mViewPager);
        mContext = getBaseContext();
        mContactsDao = BluePageContactsDao.getInstance(mContext);

        showButtons();
        clearAllDataOnTabs();

        BluePageTabDialerFragment dialerFragment = null;
        BluePageTabContactsFragment contactsFragment = null;
        BluePageTabCallLogFragment calllogFragment = null;

        dialerFragment = (BluePageTabDialerFragment) mPagerAdapter.getItem(TAB_1);
        contactsFragment = (BluePageTabContactsFragment) mPagerAdapter.getItem(TAB_2);
        calllogFragment = (BluePageTabCallLogFragment) mPagerAdapter.getItem(TAB_3);

        if (dialerFragment != null) {
            dialerFragment.scrollUpToHeader();
        }

        if (contactsFragment != null) {
            contactsFragment.scrollUpToHeader();
        }

        if (calllogFragment != null) {
            calllogFragment.scrollUpToHeader();
        }

        if (mIsSelectableMode) {
            updateSelectedNumber();
        }
    }

    @Override
    protected void onDestroy() {

        if (BluePageTabDialerFragment.getInstance() != null) {
            BluePageTabDialerFragment.removeInstance();
        }

        if (BluePageTabContactsFragment.getInstance() != null) {
            BluePageTabContactsFragment.removeInstance();
        }

        if (BluePageTabCallLogFragment.getInstance() != null) {
            BluePageTabCallLogFragment.removeInstance();
        }

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void clearAllDataOnTabs() {
        BluePageTabDialerFragment dialerFragment = null;
        BluePageTabContactsFragment contactsFragment = null;
        BluePageTabCallLogFragment calllogFragment = null;

        dialerFragment = (BluePageTabDialerFragment) mPagerAdapter.getItem(TAB_1);
        contactsFragment = (BluePageTabContactsFragment) mPagerAdapter.getItem(TAB_2);
        calllogFragment = (BluePageTabCallLogFragment) mPagerAdapter.getItem(TAB_3);

        if (dialerFragment != null) {
            dialerFragment.clearAllData();
        }

        if (contactsFragment != null) {
            contactsFragment.clearAllData();
        }

        if (calllogFragment != null) {
            calllogFragment.clearAllData();
        }

        setCheckAllSelected(false);
    }

    @SuppressWarnings("deprecation")
    private void showButtons() {
        LinearLayout defaultActionbarLayout = (LinearLayout) findViewById(R.id.contacts_actionbar_default_layout);
        LinearLayout selectableActionbarLayout = (LinearLayout) findViewById(R.id.contacts_actionbar_selectable_layout);
        if (!mIsSelectableMode) {
            mLeftBtn = (ImageView) findViewById(R.id.contacts_actionbar_default_left_button);
            mRightBtn1 = (ImageView) findViewById(R.id.contacts_actionbar_default_right_button_01);
            mRightBtn2 = (ImageView) findViewById(R.id.contacts_actionbar_default_right_button_02);
            mRightBtn3 = (ImageView) findViewById(R.id.contacts_actionbar_default_right_button_03);

            defaultActionbarLayout.setVisibility(View.VISIBLE);
            selectableActionbarLayout.setVisibility(View.GONE);
            mTitleView = (CustomTextView) findViewById(R.id.contacts_actionbar_default_title);
            mTitleView.setText(R.string.contacts_default_title);

            mLeftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(mDrawerViewLayout);
                }
            });

            mRightBtn1.setVisibility(View.GONE);
            mRightBtn2.setVisibility(View.GONE);
            mRightBtn3.setVisibility(View.VISIBLE);
            mRightBtn3.setImageResource(R.drawable.btn_actionbar_seemore);
            mRightBtn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOption();
                }
            });

            mListOption = new ListPopupWindow(mContext);
            mListOption.setModal(true);
            mListOption.setAnchorView(findViewById(R.id.contacts_actionbar_default_right_button_03));
            mListOption.setWidth(DisplayUtils.dp2px(OPTION_MENU_WIDTH));
            mListOption.setListSelector(getResources().getDrawable(R.drawable.im_popup_menu_ripple));
            mListOption.setOnItemClickListener(mOptionClickListener);

        } else {
            mLeftBtn = (ImageView) findViewById(R.id.contacts_actionbar_selectable_left_button);
            mRightBtn1 = (ImageView) findViewById(R.id.contacts_actionbar_selectable_right_button_01);
            mRightBtn2 = (ImageView) findViewById(R.id.contacts_actionbar_selectable_right_button_02);
            mRightBtn3 = (ImageView) findViewById(R.id.contacts_actionbar_selectable_right_button_03);
            mRightBtn4 = (Button) findViewById(R.id.contacts_actionbar_selectable_right_txt_button_04);

            defaultActionbarLayout.setVisibility(View.GONE);
            selectableActionbarLayout.setVisibility(View.VISIBLE);

            switch (mSelectableType) {
                case BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_DONE_RETURN_GROUP_ID_ALSO:
                case BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_DONE_RETURN_MEMBER_ID_ONLY:
                    mTitleView = (CustomTextView) findViewById(R.id.contacts_actionbar_selectable_title);
                    mTitleView.setText(R.string.contacts_selectable_title_none);

                    mLeftBtn.setVisibility(View.VISIBLE);
                    mLeftBtn.setImageResource(R.drawable.btn_checkbox_selector);
                    mLeftBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.setSelected(!v.isSelected());
                            if (v.isSelected()) {
                                if (mViewPager.getCurrentItem() == TAB_1) {
                                    getSelectableBluePageTabDialerFragment().setCheckAllGroupList();
                                    getSelectableBluePageTabDialerFragment().setSelectedAll(true);
                                } else if (mViewPager.getCurrentItem() == TAB_2) {
                                    getSelectableBluePageTabCallLogFragment().setCheckAllMemberList();
                                    getSelectableBluePageTabCallLogFragment().setSelectedAll(true);
                                } else {
                                    ;
                                }
                            } else {
                                if (mViewPager.getCurrentItem() == TAB_1) {
                                    getSelectableBluePageTabDialerFragment().setUncheckAllGroupList();
                                    getSelectableBluePageTabDialerFragment().setSelectedAll(false);
                                } else if (mViewPager.getCurrentItem() == TAB_2) {
                                    getSelectableBluePageTabCallLogFragment().setUncheckAllMemberList();
                                    getSelectableBluePageTabCallLogFragment().setSelectedAll(false);
                                } else {
                                    ;
                                }
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
                            Intent intent = new Intent();
                            ArrayList<String> selectedGroupList = getSelectedGroupIds();
                            ArrayList<String> selectedMemberList = getSelectedMemberIds();

                            if (mSelectableType == BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_DONE_RETURN_GROUP_ID_ALSO) {
                                if (selectedMemberList != null) {
                                    StringBuilder numberText = new StringBuilder();
                                    if (!selectedMemberList.isEmpty()) {

                                        // Ad-hoc
                                        intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_PRE_ARRANGED, false);
                                        numberText.append("tel:" + selectedMemberList.get(0));
                                        for (int i = 1; i < selectedMemberList.size(); ++i) {
                                            numberText.append(";tel:" + selectedMemberList.get(i));
                                        }
                                    } else {
                                        if (selectedGroupList != null) {
                                            if (selectedGroupList.size() == 1) {
                                                // Pre-arranged
                                                intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_PRE_ARRANGED, true);
                                                numberText.append("tel:" + selectedGroupList.get(0));
                                            } else {
                                                return;
                                            }
                                        }
                                    }

                                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_SELECTED_LIST, numberText.toString());
                                }

                                setResult(RESULT_OK, intent);
                                finish();
                            } else if (mSelectableType == BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_DONE_RETURN_MEMBER_ID_ONLY) {
                                if (selectedMemberList != null) {
                                    StringBuilder numberBuilder = new StringBuilder();
                                    String numberText = "";
                                    if (!selectedMemberList.isEmpty()) {

                                        // Ad-hoc
                                        numberBuilder.append("tel:" + selectedMemberList.get(0));
                                        for (int i = 1; i < selectedMemberList.size(); ++i) {
                                            numberBuilder.append(";tel:" + selectedMemberList.get(i));
                                        }
                                        numberText = numberBuilder.toString();
                                    } else {
                                        if (selectedGroupList != null) {
                                            if (selectedGroupList.size() > 0) {
                                                for (String groupId : selectedGroupList) {
                                                    ContactsGroupModel groupModel = mGroupDao.loadOneByGroupId(groupId, false);
                                                    if (groupModel != null) {
                                                        for (String memberId : groupModel.getMemberIdEntry()) {
                                                            numberBuilder.append(";tel:" + memberId);
                                                        }
                                                    }
                                                }

                                                if (numberBuilder.toString().startsWith(";")) {
                                                    numberText = numberBuilder.substring(1);
                                                }
                                            }
                                        }
                                    }

                                    if (StringUtils.isNotEmpty(numberText)) {
                                        intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_PRE_ARRANGED, false);
                                        intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_SELECTED_LIST, numberText);

                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }
                            }
                        }
                    });

                    break;
                case BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_OPTION:
                default:
                    mTitleView = (CustomTextView) findViewById(R.id.contacts_actionbar_selectable_title);
                    mTitleView.setText(R.string.contacts_selectable_title_none);

                    mLeftBtn.setVisibility(View.VISIBLE);
                    mLeftBtn.setImageResource(R.drawable.btn_checkbox_selector);
                    mLeftBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.setSelected(!v.isSelected());
                            if (v.isSelected()) {
                                if (mViewPager.getCurrentItem() == TAB_1) {
                                    getSelectableBluePageTabDialerFragment().setCheckAllGroupList();
                                    getSelectableBluePageTabDialerFragment().setSelectedAll(true);
                                } else if (mViewPager.getCurrentItem() == TAB_2) {
                                    getSelectableBluePageTabCallLogFragment().setCheckAllMemberList();
                                    getSelectableBluePageTabCallLogFragment().setSelectedAll(true);
                                } else {
                                    ;
                                }
                            } else {
                                if (mViewPager.getCurrentItem() == TAB_1) {
                                    getSelectableBluePageTabDialerFragment().setUncheckAllGroupList();
                                    getSelectableBluePageTabDialerFragment().setSelectedAll(false);
                                } else if (mViewPager.getCurrentItem() == TAB_2) {
                                    getSelectableBluePageTabCallLogFragment().setUncheckAllMemberList();
                                    getSelectableBluePageTabCallLogFragment().setSelectedAll(false);
                                } else {
                                    ;
                                }
                            }
                        }
                    });

                    mRightBtn1.setVisibility(View.GONE);
                    mRightBtn2.setVisibility(View.GONE);
                    mRightBtn2.setImageResource(R.drawable.btn_actionbar_trash);
                    mRightBtn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doDeleteMembers();
                        }
                    });

                    mRightBtn3.setVisibility(View.VISIBLE);
                    mRightBtn3.setImageResource(R.drawable.btn_actionbar_seemore);
                    mRightBtn3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onOption();
                        }
                    });

                    mRightBtn4.setVisibility(View.GONE);
                    mListOption = new ListPopupWindow(mContext);
                    mListOption.setModal(true);
                    mListOption.setAnchorView(findViewById(R.id.contacts_actionbar_selectable_right_button_03));
                    mListOption.setWidth(DisplayUtil.dp2px(OPTION_MENU_WIDTH));
                    mListOption.setListSelector(getResources().getDrawable(R.drawable.im_popup_menu_ripple));
                    mListOption.setOnItemClickListener(mOptionClickListener);

                    break;
            }
        }
    }

    OnItemClickListener mOptionClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String label = ((CustomTextView) view.findViewById(R.id.option_text)).getText().toString();
            mListOption.dismiss();

            if (!mIsSelectableMode) {
                if (getString(R.string.option_label_selectable).equals(label)) {
                    Intent intent = getIntent();
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_SELECTABLE_MODE, true);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_CHANGED_TO_SELECTABLE, true);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_SELECTABLE_BUTTON_TYPE, BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_OPTION);
                    startActivity(intent);
                } else if (getString(R.string.option_label_add_new_member).equals(label)) {
                    Intent intent = new Intent();
                    intent.setAction(mContext.getPackageName() + BluePageConstants.INTENT_ACTION_CONTACTS_EDIT);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_ADD_NEW, true);
                    startActivityForResult(intent, BluePageConstants.INTENT_REQUEST_CONTACTS_EDIT);
                }
            } else {
                // It is Pre-arranged call if the size of selectedGroupList is 1 and selectedMemberList is 0.
                // Otherwise, it is ad-hoc call
                ArrayList<String> selectedGroupList = getSelectedGroupIds();

                // This list of member's ID will use only ad-hoc call if the size of selected group is bigger than 1.
                ArrayList<String> selectedMemberList = getSelectedMemberIds();

                Intent intent = new Intent(mContext, callMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                StringBuilder numberText = new StringBuilder();

                intent.setAction(BluePageConstants.INTENT_ACTION_MAKE_CALL);

                if (selectedMemberList != null) {
                    if (!selectedMemberList.isEmpty()) {
                        // Ad-hoc call
                        intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_PRE_ARRANGED, false);
                        numberText.append("tel:" + selectedMemberList.get(0));
                        for (int i = 1; i < selectedMemberList.size(); ++i) {
                            numberText.append(";tel:" + selectedMemberList.get(i));
                        }

                        if (StringUtils.splitByWholeSeparatorPreserveAllTokens(numberText.toString(), ";").length > BluePageConstants.MAX_RECIPIENTS) {
                            showToast(getResources().getString(R.string.contacts_exceed_max_recipient_message));
                            return;
                        }

                        intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_QOE, callMainActivity.getMyProfile().getQoE());
                    } else {
                        if (selectedGroupList != null) {
                            if (selectedGroupList.size() == 1) {
                                // Pre-arranged Group call
                                intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_PRE_ARRANGED, true);
                                numberText.append("tel:" + selectedGroupList.get(0));
                                ArrayList<ContactsGroupModel> groups = mGroupDao.searchGroupId(selectedGroupList.get(0));
                                if (groups != null) {
                                    ContactsGroupModel group = groups.get(0);
                                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_QOE, group.getQoE());
                                } else {
                                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_QOE, BluePageConstants.DEFAULT_QOE);
                                }
                            } else {
                                showToast(getResources().getString(R.string.contacts_message_no_selection));
                                return;
                            }
                        }
                    }
                }

                if (getString(R.string.option_label_voice_ptt).equals(label)) {
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_HDX, true);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_VOICE, true);
                } else if (getString(R.string.option_label_video_ptt).equals(label)) {
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_HDX, true);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_VOICE, false);
                } else if (getString(R.string.option_label_voice_call).equals(label)) {
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_HDX, false);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_VOICE, true);
                } else if (getString(R.string.option_label_video_call).equals(label)) {
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_HDX, false);
                    intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_VOICE, false);
                } else if (getString(R.string.option_label_message).equals(label)) {
                    return;
                } else if (getString(R.string.option_label_send_contact).equals(label)) {
                    return;
                }

                intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_SELECTED_LIST, numberText.toString());
                startActivity(intent); // going to onNewIntent()
                finish();
            }
        }
    };

    static protected class OptionItemViewHolder {
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
                view = LayoutInflater.from(context).inflate(R.layout.contacts_option_list_item, null);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BluePageConstants.INTENT_REQUEST_CONTACTS_EDIT:
                    break;
            }
        }
    }

    void onOption() {
        if (mListOption.isShowing() == true) {
            mListOption.dismiss();
        } else {
            if (mIsSelectableMode) {
                switch (mViewPager.getCurrentItem()) {
                    case TAB_1:
                    case TAB_2:
                        mListOption.setAdapter(new OptionAdapter(this, R.array.contacts_option_array_selectable));
                        break;
                    default:
                        break;
                }
            } else {
                switch (mViewPager.getCurrentItem()) {
                    case TAB_1:
                    case TAB_2:
                        mListOption.setAdapter(new OptionAdapter(this, R.array.contacts_option_array_select_only));
                        break;
                    case TAB_3:
                        mListOption.setAdapter(new OptionAdapter(this, R.array.contacts_option_array_normal));
                        break;
                    default:
                        break;
                }
            }

            hideSoftInputKeyboard();
            mListOption.show();
        }
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }

        @Override
        public int getCount() {
            return mTabTitles.length;
        }

        @Override
        public int getItemPosition(Object object) {
            if (mIsSelectableMode) {
                // Remove TAB_2 if Selectable mode.
                if (super.getItemPosition(object) == TAB_2) {
                    return POSITION_NONE;
                }
            }

            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            if (mIsSelectableMode) {
                switch (position) {
                    case TAB_1:
                        fragment = BluePageTabDialerFragment.getInstance();
                        break;
                    case TAB_2:
                    default:
                        fragment = BluePageTabCallLogFragment.getInstance();
                        break;
                }
            } else {
                switch (position) {
                    case TAB_1:
                        fragment = BluePageTabDialerFragment.getInstance();
                        break;
                    case TAB_2:
                        fragment = BluePageTabContactsFragment.getInstance();
                        break;
                    case TAB_3:
                    default:
                        fragment = BluePageTabCallLogFragment.getInstance();
                        break;
                }
            }

            return fragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager != null) {
            int tab = mViewPager.getCurrentItem();
            Fragment fragment = mPagerAdapter.getItem(tab);

            if (mIsSelectableMode) {
                switch (tab) {
                    case TAB_1:
                        if (StringUtils.isNotEmpty(((BluePageTabDialerFragment) fragment).getSearchText())) {
                            ((BluePageTabDialerFragment) fragment).clearSearchFilter();
                            return;
                        }
                        break;
                    case TAB_2:
                        if (StringUtils.isNotEmpty(((BluePageTabCallLogFragment) fragment).getSearchText())) {
                            ((BluePageTabCallLogFragment) fragment).clearSearchFilter();
                            return;
                        }
                        break;
                    default:
                        break;

                }
            } else {
                switch (tab) {
                    case TAB_1:
                        if (StringUtils.isNotEmpty(((BluePageTabDialerFragment) fragment).getSearchText())) {
                            ((BluePageTabDialerFragment) fragment).clearSearchFilter();
                            return;
                        }
                        break;
                    case TAB_2:
                        if (StringUtils.isNotEmpty(((BluePageTabContactsFragment) fragment).getSearchText())) {
                            ((BluePageTabContactsFragment) fragment).clearSearchFilter();
                            return;
                        }
                        break;
                    case TAB_3:
                        if (StringUtils.isNotEmpty(((BluePageTabCallLogFragment) fragment).getSearchText())) {
                            ((BluePageTabCallLogFragment) fragment).clearSearchFilter();
                            return;
                        }
                        break;
                    default:
                        break;

                }

            }
        }

        if (mIsSelectableMode) {
            if (mIsChangedToSelectable) {
                Intent intent = getIntent();
                intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_SELECTABLE_MODE, false);
                startActivity(intent);
                return;
            }
        } else {
            if (this.mDrawerLayout.isDrawerOpen(mDrawerViewLayout)) {
                mDrawerLayout.closeDrawer(mDrawerViewLayout);
                return;
            }
        }

        super.onBackPressed();
    }

    public void checkOemContactsUpdated() {
        long contactsLastUpdated = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
            .getLong(BluePageConfig.PREF_KEY_CONTACTS_MEMBERS_UPDATE_TIMESTAMP, 0);

        mContactsDao.doCheckOemContactsUpdated(contactsLastUpdated, null);
    }

    public ArrayList<String> getSelectedGroupIds() {
        if (mIsSelectableMode) {
            ArrayList<String> selectedGroupList = getSelectableBluePageTabDialerFragment().getSelectedGroupList();
            return selectedGroupList;
        } else {
            return null;
        }
    }

    public ArrayList<String> getSelectedMemberIds() {
        if (mIsSelectableMode) {
            ArrayList<String> selectedMemberList = new ArrayList<String>();
            HashSet<String> memberHashSet = new HashSet<String>();

            selectedMemberList.addAll(getSelectableBluePageTabDialerFragment().getSelectedMemberList());
            selectedMemberList.addAll(getSelectableBluePageTabCallLogFragment().getSelectedMemberList());

            // Remove duplicated member ids.
            memberHashSet.addAll(selectedMemberList);
            selectedMemberList.clear();
            selectedMemberList.addAll(memberHashSet);

            return selectedMemberList;
        } else {
            return null;
        }
    }

    public int getSelectedGroupCount() {
        if (mIsSelectableMode) {
            ArrayList<String> selectedGroupList = getSelectableBluePageTabDialerFragment().getSelectedGroupList();

            if ((selectedGroupList != null) && (selectedGroupList.size() > 0)) {
                return selectedGroupList.size();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public int getSelectedMemberCount() {
        if (mIsSelectableMode) {
            ArrayList<String> selectedMemberList = new ArrayList<String>();
            selectedMemberList.addAll(getSelectableBluePageTabDialerFragment().getSelectedMemberList());
            selectedMemberList.addAll(getSelectableBluePageTabCallLogFragment().getSelectedMemberList());

            return selectedMemberList.size();
        } else {
            return 0;
        }
    }

    public void updateSelectedNumber() {
        if (mIsSelectableMode) {
            String titleString = "";
            int group = getSelectedGroupCount();
            int member = getSelectedMemberCount();

            if ((group == 1) && (member == 0)) {
                // Pre-arranged call
                titleString = String.format(getResources().getString(R.string.contacts_selectable_title_group_only), group);
                if (mSelectableType == BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_OPTION) {
                    mRightBtn2.setVisibility(View.VISIBLE);
                } else {
                    mRightBtn2.setVisibility(View.GONE);
                }
            } else {
                // Ad-hoc call
                if (member > 0) {
                    titleString = String.format(getResources().getString(R.string.contacts_selectable_title_member_only), member);
                    if (mSelectableType == BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_OPTION) {
                        mRightBtn2.setVisibility(View.VISIBLE);
                    } else {
                        mRightBtn2.setVisibility(View.GONE);
                    }
                } else {
                    titleString = String.format(getResources().getString(R.string.contacts_selectable_title_none));
                    if (mSelectableType == BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_OPTION) {
                        mRightBtn2.setVisibility(View.GONE);
                    }
                }
            }

            mTitleView.setText(titleString);
        }
    }

    private void doDeleteMembers() {
        ArrayList<String> selectedGroupList = getSelectableBluePageTabDialerFragment().getPureSelectedGroupList();
        ArrayList<String> selectedMemberList = getSelectedMemberIds();

        if ((selectedGroupList != null) && !selectedGroupList.isEmpty()) {
            // You can never delete pre-arranged group.
            showToast(getResources().getString(R.string.contacts_delete_unavailable_pre_arranged_group));
            return;
        }

        if ((selectedMemberList != null) && !selectedMemberList.isEmpty()) {
            if (checkSelectedMemberCanDelete()) {
                final Handler contactsDeleteHandler = new Handler(Looper.getMainLooper());
                mContactsDao.deleteByPttNumberList(getSelectedMemberIds(), new BluePageContactsDao.ContactsDaoCallBack<Void>() {
                    @Override
                    public void onCompleted(Void data) {
                        contactsDeleteHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                showToast(getResources().getString(R.string.contacts_delete_success_pre_arranged_member));
                                Intent intent = getIntent();
                                intent.putExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_SELECTABLE_MODE, false);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onFailure(final String data) {
                        contactsDeleteHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                showToast(getResources().getString(R.string.contacts_delete_failed));
                            }
                        });
                    }
                });
            } else {
                // You can never delete member on the pre-arranged group.
                showToast(getResources().getString(R.string.contacts_delete_unavailable_pre_arranged_member));
            }
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message != null) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected BluePageTabDialerFragment getSelectableBluePageTabDialerFragment() {
        BluePageTabDialerFragment fragment = null;

        if (mIsSelectableMode) {
            fragment = (BluePageTabDialerFragment) mPagerAdapter.getItem(TAB_1);
        }

        return fragment;
    }

    protected BluePageTabCallLogFragment getSelectableBluePageTabCallLogFragment() {
        BluePageTabCallLogFragment fragment = null;

        if (mIsSelectableMode) {
            fragment = (BluePageTabCallLogFragment) mPagerAdapter.getItem(TAB_2);
        }

        return fragment;
    }

    protected void setCheckAllSelected(boolean selected) {
        if (mLeftBtn != null) {
            mLeftBtn.setSelected(selected);
        }
    }

    private void hideSoftInputKeyboard() {
        int tab = mViewPager.getCurrentItem();
        Fragment fragment = mPagerAdapter.getItem(tab);

        if (mIsSelectableMode) {
            switch (tab) {
                case TAB_1:
                    ((BluePageTabDialerFragment) fragment).hideSoftInputKeyboard();
                    break;
                case TAB_2:
                    ((BluePageTabCallLogFragment) fragment).hideSoftInputKeyboard();
                    break;
                default:
                    break;

            }
        } else {
            switch (tab) {
                case TAB_1:

                    ((BluePageTabDialerFragment) fragment).hideSoftInputKeyboard();
                    break;
                case TAB_2:
                    ((BluePageTabContactsFragment) fragment).hideSoftInputKeyboard();
                    break;
                case TAB_3:
                    ((BluePageTabCallLogFragment) fragment).hideSoftInputKeyboard();
                    break;
                default:
                    break;

            }

        }
    }
}
