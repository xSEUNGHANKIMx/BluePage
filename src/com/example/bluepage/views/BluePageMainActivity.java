package com.example.bluepage.views;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.bluepage.BluePageConfig;
import com.example.bluepage.R;
import com.example.bluepage.dbmanager.BluePageContactsDao;

public class BluePageMainActivity extends FragmentActivity {

    static final int TAB_1 = 0;
    static final int TAB_2 = 1;
    static final int TAB_3 = 2;

    private final String[] mTabTitles = { "Dialer", "Call History", "Contacts" };

    private BluePageTabWidget mTabWidget;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private BluePageContactsDao mContactsDao;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bluepage_main_activity);

        mContext = getBaseContext();
        mContactsDao = BluePageContactsDao.getInstance(mContext);
        mTabWidget = (BluePageTabWidget) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabWidget.setViewPager(mViewPager);
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

        super.onBackPressed();
    }

    public void checkContactsUpdated() {
        long contactsLastUpdated = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
            .getLong(BluePageConfig.PREF_KEY_CONTACTS_UPDATE_TIMESTAMP, 0);

        mContactsDao.doCheckContactsUpdated(contactsLastUpdated, null);
    }
}
