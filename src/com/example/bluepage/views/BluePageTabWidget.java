package com.example.bluepage.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.bluepage.R;

public class BluePageTabWidget extends HorizontalScrollView {

    private static final int TAB_1 = 0;
    private static final int TAB_2 = 1;
    private static final int TAB_3 = 2;

    private LinearLayout.LayoutParams mTabLayoutParams;
    private final PageListener mPageListener = new PageListener();
    public OnPageChangeListener mDelegatePageListener;

    private LinearLayout mTabsContainer;
    private ViewPager mViewPager;
    private int mTabCount;
    private int mCurrentPosition = 0;
    private int mScrollOffset = 52;
    private int mLastScrollX = 0;
    private Context mContext = null;

    public BluePageTabWidget(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public BluePageTabWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public BluePageTabWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;

        setFillViewport(true);
        setWillNotDraw(false);

        mTabsContainer = new LinearLayout(context);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mTabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mScrollOffset, dm);

        mTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        mTabLayoutParams.weight = 1;
        mTabLayoutParams.gravity = Gravity.CENTER;
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;

        if (mViewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        mViewPager.setOnPageChangeListener(mPageListener);

        initTabs();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mDelegatePageListener = listener;
    }

    public void initTabs() {

        View tab1Layout = null, tab2Layout = null, tab3Layout_3 = null;
        Button tab1Btn = null, tab2Btn = null, tab3Btn = null;

        mTabCount = mViewPager.getAdapter().getCount();
        mTabsContainer.removeAllViews();

        tab1Layout = LayoutInflater.from(mContext).inflate(R.layout.bluepage_tab_widget_layout, null);
        mTabsContainer.addView(tab1Layout, TAB_1, mTabLayoutParams);
        tab1Btn = (Button) tab1Layout.findViewById(R.id.contacts_tab_widget_button);
        if (tab1Btn != null) {
            tab1Btn.setText(R.string.bluepage_tab_dialer);
        }

        tab2Layout = LayoutInflater.from(mContext).inflate(R.layout.bluepage_tab_widget_layout, null);
        tab2Btn = (Button) tab2Layout.findViewById(R.id.contacts_tab_widget_button);
        tab2Btn.setText(R.string.bluepage_tab_calllog);
        mTabsContainer.addView(tab2Layout, TAB_2, mTabLayoutParams);

        tab3Layout_3 = LayoutInflater.from(mContext).inflate(R.layout.bluepage_tab_widget_layout, null);
        tab3Btn = (Button) tab3Layout_3.findViewById(R.id.contacts_tab_widget_button);
        if (tab3Btn != null) {
            tab3Btn.setText(R.string.bluepage_tab_contacts);
            mTabsContainer.addView(tab3Layout_3, TAB_3, mTabLayoutParams);
        }

        if (tab1Btn != null) {
            tab1Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(TAB_1);
                    updateTabStyles();
                }
            });
        }

        if (tab2Btn != null) {
            tab2Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(TAB_2);
                    updateTabStyles();
                }
            });
        }

        if (tab3Btn != null) {
            tab3Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(TAB_3);
                    updateTabStyles();
                }
            });
        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                mCurrentPosition = mViewPager.getCurrentItem();
                scrollToChild(mCurrentPosition, 0);
            }
        });

    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View view = mTabsContainer.getChildAt(i);

            if (view instanceof LinearLayout) {
                Button button = (Button) view.findViewById(R.id.contacts_tab_widget_button);
                View selectedIndicator = view.findViewById(R.id.contacts_tab_widget_indicator);

                if (i == mCurrentPosition) {
                    button.setTextColor(getResources().getColor(R.color.contacts_tab_selected_text_color));
                    selectedIndicator.setBackgroundColor(getResources().getColor(
                        R.color.contacts_tab_selected_text_color));
                } else {
                    button.setTextColor(getResources().getColor(R.color.contacts_tab_unselected_text_color));
                    selectedIndicator.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
            }
        }
    }

    private void scrollToChild(int position, int offset) {

        if (mTabCount == 0) {
            return;
        }

        int newScrollX = mTabsContainer.getChildAt(position).getLeft() + offset;

        if ((position > 0) || (offset > 0)) {
            newScrollX -= mScrollOffset;
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            mCurrentPosition = position;
            scrollToChild(position, (int) (positionOffset * mTabsContainer.getChildAt(position).getWidth()));
            updateTabStyles();

            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mViewPager.getCurrentItem(), 0);
            }

            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageSelected(position);
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPosition = savedState.mCurrentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mCurrentPosition = mCurrentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int mCurrentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mCurrentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mCurrentPosition);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
