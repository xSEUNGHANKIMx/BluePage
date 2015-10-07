package com.example.bluepage.views;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluepage.BluePageConfig;
import com.example.bluepage.R;
import com.example.bluepage.dbmanager.BluePageContactsDao;
import com.example.bluepage.model.BluePageContactsModel;
import com.example.bluepage.utils.BaseListSort;
import com.example.bluepage.utils.CircularImageView;
import com.example.bluepage.utils.CustomTextView;
import com.example.bluepage.utils.IndexableListView;
import com.example.bluepage.utils.SimpleTextWatcher;
import com.example.bluepage.utils.StringMatcher;
import com.example.bluepage.utils.UtilHangul;

public class BluePageTabContactsFragment extends Fragment implements LoaderCallbacks<ArrayList<BluePageContactsModel>> {

    private static BluePageTabContactsFragment sInstance;
    private ContactsTabContactsAdapter mAdapter;
    private BluePageContactsDao mContactsDao;
    protected ArrayList<BluePageContactsListObject> mObjectList = new ArrayList<BluePageContactsListObject>();
    protected ArrayList<BluePageContactsModel> mContactsList = new ArrayList<BluePageContactsModel>();
    protected IndexableListView mListView;
    private EditText mSearchEdit;
    private ImageView mSearchCancelBtn;
    private CustomTextView mEmptyView;
    protected Loader<ArrayList<BluePageContactsModel>> mLoader;
    private ContentObserver mContactsObserver;
    private InputMethodManager mImm;
    private String mSearchInputString = "";
    private Context mContext;
    private boolean mVisibleLabel = true;

    public static synchronized BluePageTabContactsFragment getInstance() {
        if (sInstance == null) {
            sInstance = new BluePageTabContactsFragment();
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

        mContactsDao = BluePageContactsDao.getInstance(mContext);
        mAdapter = new ContactsTabContactsAdapter();
        mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        getActivity().getSupportLoaderManager().initLoader(BluePageConfig.CONTACTS_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bluepage_tab_contacts_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSearchEdit = (EditText) getView().findViewById(R.id.contacts_search_edittext);
        mSearchCancelBtn = (ImageView) getView().findViewById(R.id.contacts_search_right_icon);
        mEmptyView = (CustomTextView) getView().findViewById(R.id.contacts_listview_empty);
        mListView = (IndexableListView) getView().findViewById(R.id.contacts_listview);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);
        mListView.setFastScrollEnabled(true);
        mListView.showScrollbar(true);

        initListeners();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, mContactsObserver);

        if (StringUtils.isEmpty(mSearchInputString)) {
            long contactsLastUpdated = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getLong(BluePageConfig.PREF_KEY_CONTACTS_UPDATE_TIMESTAMP, 0);

            mContactsDao.doCheckContactsUpdated(contactsLastUpdated, null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().getContentResolver().unregisterContentObserver(mContactsObserver);
        if (mLoader != null) {
            mLoader.stopLoading();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void clearAllData() {
        if (mSearchEdit != null) {
            clearSearchFilter();
        }
    }

    private void initListeners() {
        mSearchEdit.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s) {
                setFilterText(s.toString());

                if (s.toString().length() > 0) {
                    mVisibleLabel = false;
                    mSearchCancelBtn.setVisibility(View.VISIBLE);
                } else {
                    mVisibleLabel = true;
                    mSearchCancelBtn.setVisibility(View.GONE);
                }
            }
        });

        mContactsObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                long contactsLastUpdated = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                    .getLong(BluePageConfig.PREF_KEY_CONTACTS_UPDATE_TIMESTAMP, 0);

                mContactsDao.doCheckContactsUpdated(contactsLastUpdated, null);
            }
        };

        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        hideSoftInputKeyboard();
                        break;
                }
                return false;
            }
        });

        mSearchEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mListView.showScrollbar(false);
                        break;
                }
                return false;
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    hideSoftInputKeyboard();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                int totalItemCount) {
            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            }
        });

        mSearchCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearchFilter();
            }
        });
    }

    @Override
    public Loader<ArrayList<BluePageContactsModel>> onCreateLoader(int arg0, Bundle arg1) {
        mLoader = mContactsDao.getContactsLoader();
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<BluePageContactsModel>> loader, ArrayList<BluePageContactsModel> data) {
        if (StringUtils.isEmpty(mSearchInputString)) {
            mContactsList.clear();

            if (data.size() > 0) {
                sortDataByName(data);
                setListLabels(data);

                mContactsList.addAll(data);
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<BluePageContactsModel>> arg0) {
        mContactsList.clear();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    private <T> List<T> sortDataByName(List<T> data) {
        MemberListSortByName<T> listSort = new MemberListSortByName<T>(data);

        return listSort.sort();
    }

    private <T> List<T> sortDataByNumber(List<T> data) {
        MemberListSortByNumber<T> listSort = new MemberListSortByNumber<T>(data);

        return listSort.sort();
    }

    public void setListLabels(ArrayList<BluePageContactsModel> models) {
        String currletter = null;
        String symbol = null;

        mObjectList.clear();
        for (BluePageContactsModel memberModel : models) {
            if (UtilHangul.isHangul(memberModel.getDisplayName().charAt(0))
                || UtilHangul.isEnglish(memberModel.getDisplayName().charAt(0))
                || UtilHangul.isChinese(memberModel.getDisplayName().charAt(0))) {
                if (!memberModel.getListLabel().equalsIgnoreCase(currletter)) {
                    currletter = memberModel.getListLabel().toUpperCase();
                    if (mVisibleLabel) {
                        BluePageContactsListObject label = new BluePageContactsListObject(BluePageContactsListObject.LIST_TYPE_LABEL, null, currletter);
                        mObjectList.add(label);
                    }
                }

                BluePageContactsListObject contact = new BluePageContactsListObject(BluePageContactsListObject.LIST_TYPE_CONTACT, memberModel, currletter);
                mObjectList.add(contact);
            } else {
                if (symbol == null) {
                    if (mVisibleLabel) {
                        symbol = "#";
                        BluePageContactsListObject label = new BluePageContactsListObject(BluePageContactsListObject.LIST_TYPE_LABEL, null, symbol);
                        mObjectList.add(label);
                    }
                }

                BluePageContactsListObject contact = new BluePageContactsListObject(BluePageContactsListObject.LIST_TYPE_CONTACT, memberModel, symbol);
                mObjectList.add(contact);
            }
        }
    }

    public void makeFilteredObjectList(ArrayList<BluePageContactsModel> models) {
        ArrayList<BluePageContactsModel> mSortedList = sortFilteredList(models);

        mObjectList.clear();
        for (BluePageContactsModel memberModel : mSortedList) {
            BluePageContactsListObject contact = new BluePageContactsListObject(BluePageContactsListObject.LIST_TYPE_CONTACT, memberModel, "");
            mObjectList.add(contact);
        }
    }

    private ArrayList<BluePageContactsModel> sortFilteredList(ArrayList<BluePageContactsModel> data) {
        ArrayList<BluePageContactsModel> result = new ArrayList<BluePageContactsModel>();
        SparseArray<ArrayList<BluePageContactsModel>> nameList = new SparseArray<ArrayList<BluePageContactsModel>>();
        ArrayList<BluePageContactsModel> numberList = new ArrayList<BluePageContactsModel>();

        for (BluePageContactsModel model : data) {
            if (model.getSearchMatchedNameStart() > -1) {
                if (nameList.get(model.getSearchMatchedNameStart()) == null) {
                    nameList.put(model.getSearchMatchedNameStart(), new ArrayList<BluePageContactsModel>());
                }
                nameList.get(model.getSearchMatchedNameStart()).add(model);
            } else if (model.getSearchMatchedNumberStart() > -1) {
                numberList.add(model);
            }
        }

        for (int i = 0; i < nameList.size(); ++i) {
            result.addAll(sortDataByName(nameList.get(nameList.keyAt(i))));
        }

        result.addAll(sortDataByNumber(numberList));

        return result;
    }

    public void clearSearchFilter() {
        if (mSearchEdit.getText().toString().length() > 0) {
            mSearchEdit.setText("");
            mSearchInputString = "";
        }

        if (mImm.isActive() == true) {
            mImm.hideSoftInputFromWindow(mSearchEdit.getWindowToken(), 0);
        }
    }

    protected void hideSoftInputKeyboard() {
        if (mSearchEdit.hasFocus() == true) {
            mSearchEdit.clearFocus();
            if (mImm.isActive() == true) {
                mImm.hideSoftInputFromWindow(mSearchEdit.getWindowToken(), 0);
            }
        }
    }

    protected void showToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message != null) {
                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            if (!isVisibleToUser) {
                hideSoftInputKeyboard();
            }
        }
    }

    void setFilterText(String text) {
        new ContactsListFilterTask().execute(text.toLowerCase());
    }

    public String getSearchText() {
        return mSearchInputString;
    }

    protected void scrollUpToHeader() {
        mListView.setSelectionAfterHeaderView();
    }

    class ContactsTabContactsAdapter extends BaseAdapter implements SectionIndexer {

        private LayoutInflater inflator;
        private String sectionStr = BluePageConfig.INDEX_SCROLLER_STRING_EN;

        public ContactsTabContactsAdapter() {
            this.inflator = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            int nSize = 0;
            nSize = mObjectList.size();

            return nSize;
        }

        @Override
        public BluePageContactsListObject getItem(int position) {
            return mObjectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return BluePageContactsListObject.LIST_TYPE_MAX;
        }

        @Override
        public int getItemViewType(int position) {
            BluePageContactsListObject item = getItem(position);
            int type = item.getType();

            if (type == BluePageContactsListObject.LIST_TYPE_UNKNOWN) {
                return BluePageContactsListObject.LIST_TYPE_UNKNOWN;
            } else if (type == BluePageContactsListObject.LIST_TYPE_LABEL) {
                return BluePageContactsListObject.LIST_TYPE_LABEL;
            } else if (type == BluePageContactsListObject.LIST_TYPE_CONTACT) {
                return BluePageContactsListObject.LIST_TYPE_CONTACT;
            }

            return 0;
        }

        protected class ViewHolder {
            CircularImageView thumb;
            CustomTextView displayName;
            CustomTextView number;
        }

        @SuppressLint({ "ViewHolder", "InflateParams" })
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            int viewType;

            viewType = getItemViewType(position);
            BluePageContactsListObject item = getItem(position);

            if (viewType == BluePageContactsListObject.LIST_TYPE_LABEL) {
                view = getLabelView(position, convertView, parent, item);
            } else if (viewType == BluePageContactsListObject.LIST_TYPE_CONTACT) {
                view = getContactView(position, convertView, parent, item);
            }

            return view;
        }

        private View getLabelView(int position, View convertView, ViewGroup parent, BluePageContactsListObject obj) {
            View view = convertView;

            if (view == null) {
                view = inflator.inflate(R.layout.bluepage_contacts_list_item_label, null);
            }

            TextView tv = (TextView) view.findViewById(R.id.contacts_list_label_textview);

            if (obj.getLabel() != null) {
                tv.setText(obj.getLabel());
            } else {
                tv.setText("Unknown Label");
            }
            return view;
        }

        private View getContactView(final int position, View convertView, ViewGroup parent, BluePageContactsListObject obj) {
            View view = convertView;

            if (obj != null) {
                final BluePageContactsModel model = (BluePageContactsModel) obj.getObject();
                final ViewHolder holder;

                if (view == null) {
                    view = inflator.inflate(R.layout.bluepage_contacts_list_item, null);
                    holder = new ViewHolder();
                    holder.thumb = (CircularImageView) view.findViewById(R.id.contacts_item_thumb);
                    holder.displayName = (CustomTextView) view.findViewById(R.id.contacts_item_name);
                    holder.number = (CustomTextView) view.findViewById(R.id.contacts_item_number);

                    view.setTag(holder);
                } else {
                    view.destroyDrawingCache();
                    holder = (ViewHolder) view.getTag();
                }

                // Set Thumbnail Image
                if (StringUtils.isNotEmpty(model.getPhotoUri())) {
                    holder.thumb.setImageURI(Uri.parse(model.getPhotoUri()));
                } else {
                    holder.thumb.setImageResource(R.drawable.img_incomingcall_default);
                }

                // Set Name
                if (StringUtils.isNotEmpty(mSearchInputString)
                    && (model.getSearchMatchedNameStart() >= 0)
                    && (model.getSearchMatchedNameEnd() >= 0)) {
                    holder.displayName.setColor(model.getSearchMatchedNameStart(), model.getSearchMatchedNameEnd(), model.getDisplayName());
                } else {
                    // It is same action with setText().
                    holder.displayName.setColor(0, 0, model.getDisplayName());
                }

                // Set Number
                if (StringUtils.isNotEmpty(mSearchInputString)
                    && (model.getSearchMatchedNumberStart() >= 0)
                    && (model.getSearchMatchedNumberEnd() >= 0)) {
                    holder.number.setColor(model.getSearchMatchedNumberStart(), model.getSearchMatchedNumberEnd(), model.getPhonePrimary());
                } else {
                    // It is same action with setText().
                    holder.number.setColor(0, 0, model.getPhonePrimary());
                }
            }

            return view;
        }

        @Override
        public int getPositionForSection(int section) {
            // If there is no item for current section, previous section will be selected
            for (int i = section; i >= 0; i--) {
                for (int j = 0; j < getCount(); j++) {
                    if (i == 0) {
                        // For numeric section
                        /*                        for (int k = 0; k <= 9; k++) {
                            if (StringMatcher.match(getItem(j).getLabel(), String.valueOf(k))) {
                                return j;
                            }
                        }*/
                    } else {
                        if (StringMatcher.match(getItem(j).getLabel(), String.valueOf(sectionStr.charAt(i)))) {
                            return j;
                        }
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Object[] getSections() {
            String[] sections = new String[sectionStr.length()];
            for (int i = 0; i < sectionStr.length(); i++) {
                sections[i] = String.valueOf(sectionStr.charAt(i));
            }
            return sections;
        }
    }

    class ContactsListFilterTask extends AsyncTask<String, Void, ArrayList<BluePageContactsModel>> {
        String initialSound;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BluePageContactsModel> doInBackground(String... params) {
            mSearchInputString = params[0];

            if (StringUtils.isNotEmpty(mSearchInputString)) {
                ArrayList<BluePageContactsModel> filteredList = new ArrayList<BluePageContactsModel>();
                initialSound = getHangulInitialSound(mSearchInputString);

                for (BluePageContactsModel model : mContactsList) {
                    int machedCount = 0;

                    // Check Member Name is matching.
                    if (findMemberNameMachedString(model)) {
                        machedCount++;
                    } else {
                        model.setSearchMatchedNameStart(-1);
                        model.setSearchMatchedNameEnd(-1);
                    }

                    // Check Member Number is matching.
                    if (findMemberNumberMachedString(model)) {
                        machedCount++;
                    } else {
                        model.setSearchMatchedNumberStart(-1);
                        model.setSearchMatchedNumberEnd(-1);
                    }

                    if (machedCount > 0) {
                        filteredList.add(model);
                    }
                }

                return filteredList;
            } else {
                return mContactsList;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<BluePageContactsModel> result) {
            if (StringUtils.isNotEmpty(mSearchInputString)) {
                makeFilteredObjectList(result);
            } else {
                setListLabels(result);
            }

            if (!mObjectList.isEmpty()) {
                mAdapter.notifyDataSetChanged();
                if (StringUtils.isNotEmpty(mSearchInputString)) {
                    scrollUpToHeader();
                }
            } else {
                mAdapter.notifyDataSetInvalidated();
            }
        }

        private boolean findMemberNameMachedString(BluePageContactsModel memberModel) {
            if (memberModel == null) {
                return false;
            }

            String displayName = memberModel.getDisplayName();
            String searchKey = memberModel.getSearchKey().toLowerCase();
            boolean bIsMached = false;

            if (displayName.length() >= initialSound.length()) {
                int start = -1, end = -1, offset = 0;
                if (searchKey.indexOf(initialSound) >= 0) {
                    start = searchKey.indexOf(initialSound);
                    end = start + initialSound.length();

                    while (end <= displayName.length()) {
                        String buf = displayName.substring(start, end);
                        int i;

                        for (i = 0; i < buf.length(); i++) {
                            char c = mSearchInputString.charAt(i);
                            if (isHangul(c)
                                && !isChoSungOnly(c)
                                && !isChoSungOnly(buf.charAt(i))) {
                                if (getHangulJaSo(buf.charAt(i))[1] == getHangulJaSo(c)[1]) {
                                    if (getHangulJaSo(buf.charAt(i))[2] == 0) {
                                        if (getHangulJaSo(c)[2] != 0) {
                                            break;
                                        }
                                    } else {
                                        if (getHangulJaSo(c)[2] != 0) {
                                            if (getHangulJaSo(buf.charAt(i))[2]
                                                != getHangulJaSo(c)[2]) {
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    break;
                                }
                            }
                        }

                        if (i == buf.length()) {
                            // 완벽히 매칭되므로 리스트에 추가한다.
                            memberModel.setSearchMatchedNameStart(start + offset);
                            memberModel.setSearchMatchedNameEnd(end + offset);
                            bIsMached = true;
                            break;
                        } else {
                            // 뒷부분에 또 다시 매칭이 되는 초성이 있는지 계속해서 검사한다.
                            offset += end;
                            searchKey = searchKey.substring(end);
                            displayName = displayName.substring(end);
                            if (searchKey.indexOf(initialSound) >= 0) {
                                start = searchKey.indexOf(initialSound);
                                end = start + initialSound.length();
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            return bIsMached;
        }

        private boolean findMemberNumberMachedString(BluePageContactsModel memberModel) {
            if (memberModel == null) {
                return false;
            }

            String number = memberModel.getPhonePrimary();
            String searchKey = UtilHangul.getHangulInitialSound(number);
            boolean bIsMached = false;

            if (number.length() >= initialSound.length()) {
                int start = -1, end = -1, offset = 0;
                if (searchKey.indexOf(initialSound) >= 0) {
                    start = searchKey.indexOf(initialSound);
                    end = start + initialSound.length();

                    while (end <= number.length()) {
                        String buf = number.substring(start, end);
                        int i;

                        for (i = 0; i < buf.length(); i++) {
                            char c = mSearchInputString.charAt(i);
                            if (isHangul(c) && !isChoSungOnly(c) && !isChoSungOnly(buf.charAt(i))) {
                                if (getHangulJaSo(buf.charAt(i))[1] == getHangulJaSo(c)[1]) {
                                    if (getHangulJaSo(buf.charAt(i))[2] == 0) {
                                        if (getHangulJaSo(c)[2] != 0) {
                                            break;
                                        }
                                    } else {
                                        if (getHangulJaSo(c)[2] != 0) {
                                            if (getHangulJaSo(buf.charAt(i))[2]
                                                != getHangulJaSo(c)[2]) {
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    break;
                                }
                            }
                        }

                        if (i == buf.length()) {
                            // 완벽히 매칭되므로 리스트에 추가한다.
                            memberModel.setSearchMatchedNumberStart(start + offset);
                            memberModel.setSearchMatchedNumberEnd(end + offset);
                            bIsMached = true;
                            break;
                        } else {
                            // 뒷부분에 또 다시 매칭이 되는 초성이 있는지 계속해서 검사한다.
                            offset += end;
                            searchKey = searchKey.substring(end);
                            number = number.substring(end);
                            if (searchKey.indexOf(initialSound) >= 0) {
                                start = searchKey.indexOf(initialSound);
                                end = start + initialSound.length();
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            return bIsMached;
        }

        private boolean isHangul(char c) {
            return UtilHangul.isHangul(c);
        }

        private boolean isChoSungOnly(char searchChar) {
            return UtilHangul.isChoSungOnly(searchChar);
        }

        private int[] getHangulJaSo(char s) {
            return UtilHangul.getHangulJaSo(s);
        }

        private String getHangulInitialSound(String value) {
            return UtilHangul.getHangulInitialSound(value);
        }
    }

    static class MemberListSortByName<T> extends BaseListSort<T> {

        public MemberListSortByName(List<T> lists) {
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

                name1 = ((BluePageContactsModel) object1).getDisplayName();
                name2 = ((BluePageContactsModel) object2).getDisplayName();
                return collator.compare(name1, name2);
            }
        };

        public List<T> sort() {
            Collections.sort(mLists, mComparator);
            return mLists;
        }
    }

    static class MemberListSortByNumber<T> extends BaseListSort<T> {

        public MemberListSortByNumber(List<T> lists) {
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

                name1 = ((BluePageContactsModel) object1).getPhonePrimary();
                name2 = ((BluePageContactsModel) object2).getPhonePrimary();
                return collator.compare(name1, name2);
            }
        };

        public List<T> sort() {
            Collections.sort(mLists, mComparator);
            return mLists;
        }
    }

}
