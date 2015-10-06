package com.example.bluepage.views;

import org.apache.commons.lang3.StringUtils;

import com.example.bluepage.BluePageConfig;
import com.example.bluepage.BluePageConstants;
import com.example.bluepage.R;
import com.example.bluepage.dbmanager.BluePageContactsDao;
import com.example.bluepage.utils.CustomTextView;
import com.example.bluepage.utils.DisplayUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private boolean mIsChangedToSelectable = false;
    private int mSelectableType = 0;
    private ImageView mRightBtn1, mRightBtn2, mRightBtn3;
    private Button mRightBtn4;
    private CustomTextView mTitleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bluepage_main_activity);

        mContext = getBaseContext();
        mContactsDao = BluePageContactsDao.getInstance(mContext);
        mIsChangedToSelectable = getIntent().getBooleanExtra(BluePageConstants.INTENT_KEY_CONTACTS_IS_CHANGED_TO_SELECTABLE, false);
        mSelectableType = getIntent().getIntExtra(BluePageConstants.INTENT_KEY_CONTACTS_SELECTABLE_BUTTON_TYPE, BluePageConstants.CONTACTS_SELECTABLE_BUTTON_TYPE_OPTION);

        mTabWidget = (BluePageTabWidget) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabWidget.setViewPager(mViewPager);

        showButtons();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

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

        BluePageTabContactsFragment contactsFragment = null;
        BluePageTabCallLogFragment calllogFragment = null;

        contactsFragment = (BluePageTabContactsFragment) mPagerAdapter.getItem(TAB_2);
        calllogFragment = (BluePageTabCallLogFragment) mPagerAdapter.getItem(TAB_3);

        if (contactsFragment != null) {
            contactsFragment.scrollUpToHeader();
        }

        if (calllogFragment != null) {
            calllogFragment.scrollUpToHeader();
        }
    }

    @Override
    protected void onDestroy() {

        if (BluePageTabDialerFragment.getInstance() != null) {
            BluePageTabDialerFragment.removeInstance();
        }

        if (BluePageTabCallLogFragment.getInstance() != null) {
            BluePageTabCallLogFragment.removeInstance();
        }

        if (BluePageTabContactsFragment.getInstance() != null) {
            BluePageTabContactsFragment.removeInstance();
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
        BluePageTabContactsFragment contactsFragment = null;

        contactsFragment = (BluePageTabContactsFragment) mPagerAdapter.getItem(TAB_2);

        if (contactsFragment != null) {
            contactsFragment.clearAllData();
        }
    }

    @SuppressWarnings("deprecation")
    private void showButtons() {
        LinearLayout defaultActionbarLayout = (LinearLayout) findViewById(R.id.contacts_actionbar_default_layout);
        LinearLayout selectableActionbarLayout = (LinearLayout) findViewById(R.id.contacts_actionbar_selectable_layout);
        mRightBtn1 = (ImageView) findViewById(R.id.contacts_actionbar_default_right_button_01);
        mRightBtn2 = (ImageView) findViewById(R.id.contacts_actionbar_default_right_button_02);
        mRightBtn3 = (ImageView) findViewById(R.id.contacts_actionbar_default_right_button_03);

        defaultActionbarLayout.setVisibility(View.VISIBLE);
        selectableActionbarLayout.setVisibility(View.GONE);
        mTitleView = (CustomTextView) findViewById(R.id.contacts_actionbar_default_title);
        mTitleView.setText(R.string.contacts_default_title);

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
    }

    OnItemClickListener mOptionClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String label = ((CustomTextView) view.findViewById(R.id.option_text)).getText().toString();
            mListOption.dismiss();

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
            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case TAB_1:
                    fragment = BluePageTabDialerFragment.getInstance();
                    break;
                case TAB_2:
                    fragment = BluePageTabCallLogFragment.getInstance();
                    break;
                case TAB_3:
                    fragment = BluePageTabContactsFragment.getInstance();
                    break;
            }

            return fragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager != null) {
            int tab = mViewPager.getCurrentItem();
            Fragment fragment = mPagerAdapter.getItem(tab);

            switch (tab) {
                case TAB_3:
                    if (StringUtils.isNotEmpty(((BluePageTabContactsFragment) fragment).getSearchText())) {
                        ((BluePageTabContactsFragment) fragment).clearSearchFilter();
                        return;
                    }
                    break;
                default:
                    break;
            }
        }

        if (this.mDrawerLayout.isDrawerOpen(mDrawerViewLayout)) {
            mDrawerLayout.closeDrawer(mDrawerViewLayout);
            return;
        }

        super.onBackPressed();
    }

    public void checkOemContactsUpdated() {
        long contactsLastUpdated = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
            .getLong(BluePageConfig.PREF_KEY_CONTACTS_MEMBERS_UPDATE_TIMESTAMP, 0);

        mContactsDao.doCheckOemContactsUpdated(contactsLastUpdated, null);
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

    private void hideSoftInputKeyboard() {
        int tab = mViewPager.getCurrentItem();
        Fragment fragment = mPagerAdapter.getItem(tab);

        switch (tab) {
            case TAB_3:
                ((BluePageTabContactsFragment) fragment).hideSoftInputKeyboard();
                break;
            default:
                break;

        }

    }

}
