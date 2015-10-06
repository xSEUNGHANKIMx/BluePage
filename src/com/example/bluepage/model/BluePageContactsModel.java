package com.example.bluepage.model;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bluepage.R;
import com.example.bluepage.dbmanager.BluePageContactsDao;
import com.example.bluepage.views.BluePageContactsDetailListObject;

public class BluePageContactsModel implements Parcelable, Cloneable {

    int _id; // Primary Key

    // OEM Data
    String mContactId;
    String mLookupKey;
    String mDisplayName;
    String mFamilyName;
    String mMiddleName;
    String mGivenName;
    String mNickName;
    String mPhoneMobile1;
    String mPhoneMobile2;
    String mPhoneMobile3;
    String mPhoneHome1;
    String mPhoneHome2;
    String mPhoneWork1;
    String mPhoneWork2;
    String mFaxHome;
    String mFaxWork;
    String mPager;
    String mOtherPhone1;
    String mOtherPhone2;
    String mCustomPhone;
    String mCustomPhoneType;
    String mHomeEmail;
    String mWorkEmail;
    String mOtherEmail;
    String mCustomEmail;
    String mCustomEmailType;
    String mHomeAddress;
    String mWorkAddress;
    String mOtherAddress;
    String mCustomAddress;
    String mCustomAddressType;
    String mCompanyName;
    String mCompanyDepartment;
    String mCompanyTitle;
    String mBirthdayEvent;
    String mAnniversaryEvent;
    String mOtherEvent;
    String mCustomEvent;
    String mCustomEventType;
    String mWebsite;
    String mMessenger;
    String mNote;
    String mRelation;
    String mPhotoUri;

    // PttCall Data
    String mPttNumber; // Unique ID for PTT Service
    String mPttPhotoUri;
    String mPttPresence; // 0:absence 1:registered
    byte mFavorite;
    byte mAdhocMember; // 사용자가 폰에서 직접 추가한 멤버.
    byte mUpdated; // 서버로 부터 새로 업데이트가 되었는지를 저장함.

    // Application Data
    String mPhonePrimary;
    String mSearchKey; // 초성검색용
    String mListLabel; // 리스트 라벨용
    String mTypicalId; // LookupKey 혹은 PttNumber 중 한개가 저장됨.
    String mDispatcherID; //긴급통화시 PTT 전화번호
    int mQoE;             //Priority
    int mLock;
    int mSearchMatchedNameStart;
    int mSearchMatchedNameEnd;
    int mSearchMatchedNumberStart;
    int mSearchMatchedNumberEnd;

    ArrayList<BluePageContactsDetailListObject> mDataList;

    public BluePageContactsModel() {
        _id = -1;
        mContactId = "";
        mLookupKey = "";
        mDisplayName = "";
        mFamilyName = "";
        mMiddleName = "";
        mGivenName = "";
        mNickName = "";
        mPhoneMobile1 = "";
        mPhoneMobile2 = "";
        mPhoneMobile3 = "";
        mPhoneHome1 = "";
        mPhoneHome2 = "";
        mPhoneWork1 = "";
        mPhoneWork2 = "";
        mFaxHome = "";
        mFaxWork = "";
        mPager = "";
        mOtherPhone1 = "";
        mOtherPhone2 = "";
        mCustomPhone = "";
        mCustomPhoneType = "";
        mHomeEmail = "";
        mWorkEmail = "";
        mOtherEmail = "";
        mCustomEmail = "";
        mCustomEmailType = "";
        mHomeAddress = "";
        mWorkAddress = "";
        mOtherAddress = "";
        mCustomAddress = "";
        mCustomAddressType = "";
        mCompanyName = "";
        mCompanyDepartment = "";
        mCompanyTitle = "";
        mBirthdayEvent = "";
        mAnniversaryEvent = "";
        mOtherEvent = "";
        mCustomEvent = "";
        mCustomEventType = "";
        mWebsite = "";
        mMessenger = "";
        mNote = "";
        mRelation = "";
        mPhotoUri = "";

        mPttNumber = "";
        mPttPhotoUri = "";
        mPttPresence = "";
        mFavorite = 0;
        mAdhocMember = 0;
        mUpdated = 0;

        mPhonePrimary = "";
        mSearchKey = "";
        mListLabel = "";
        mTypicalId = "";

        mSearchMatchedNameStart = -1;
        mSearchMatchedNameEnd = -1;
        mSearchMatchedNumberStart = -1;
        mSearchMatchedNumberEnd = -1;

        mDispatcherID = "";
        mQoE = 1;
        mLock = 1;

        mDataList = new ArrayList<BluePageContactsDetailListObject>();
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getContactId() {
        return mContactId;
    }

    public void setContactId(String contactId) {
        this.mContactId = contactId;

        if (this.mContactId == null) {
            this.mContactId = "";
        }
    }

    public String getLookupKey() {
        return mLookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.mLookupKey = lookupKey;

        if (this.mLookupKey == null) {
            this.mLookupKey = "";
        }
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;

        if (this.mDisplayName == null) {
            this.mDisplayName = "";
        }
    }

    public String getFamilyName() {
        return mFamilyName;
    }

    public void setFamilyName(String familyName) {
        this.mFamilyName = familyName;

        if (this.mFamilyName == null) {
            this.mFamilyName = "";
        }
    }

    public String getMiddleName() {
        return mMiddleName;
    }

    public void setMiddleName(String middleName) {
        this.mMiddleName = middleName;

        if (this.mMiddleName == null) {
            this.mMiddleName = "";
        }
    }

    public String getGivenName() {
        return mGivenName;
    }

    public void setGivenName(String givenName) {
        this.mGivenName = givenName;

        if (this.mGivenName == null) {
            this.mGivenName = "";
        }
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        this.mNickName = nickName;

        if (this.mNickName == null) {
            this.mNickName = "";
        }
    }

    public String getPhoneMobile1() {
        return mPhoneMobile1;
    }

    public void setPhoneMobile1(String phoneMobile1) {
        this.mPhoneMobile1 = phoneMobile1;

        if (this.mPhoneMobile1 == null) {
            this.mPhoneMobile1 = "";
        }
    }

    public String getPhoneMobile2() {
        return mPhoneMobile2;
    }

    public void setPhoneMobile2(String phoneMobile2) {
        this.mPhoneMobile2 = phoneMobile2;

        if (this.mPhoneMobile2 == null) {
            this.mPhoneMobile2 = "";
        }
    }

    public String getPhoneMobile3() {
        return mPhoneMobile3;
    }

    public void setPhoneMobile3(String phoneMobile3) {
        this.mPhoneMobile3 = phoneMobile3;

        if (this.mPhoneMobile3 == null) {
            this.mPhoneMobile3 = "";
        }
    }

    public String getPhoneHome1() {
        return mPhoneHome1;
    }

    public void setPhoneHome1(String phoneHome1) {
        this.mPhoneHome1 = phoneHome1;

        if (this.mPhoneHome1 == null) {
            this.mPhoneHome1 = "";
        }
    }

    public String getPhoneHome2() {
        return mPhoneHome2;
    }

    public void setPhoneHome2(String phoneHome2) {
        this.mPhoneHome2 = phoneHome2;

        if (this.mPhoneHome2 == null) {
            this.mPhoneHome2 = "";
        }
    }

    public String getPhoneWork1() {
        return mPhoneWork1;
    }

    public void setPhoneWork1(String phoneWork1) {
        this.mPhoneWork1 = phoneWork1;

        if (this.mPhoneWork1 == null) {
            this.mPhoneWork1 = "";
        }
    }

    public String getPhoneWork2() {
        return mPhoneWork2;
    }

    public void setPhoneWork2(String phoneWork2) {
        this.mPhoneWork2 = phoneWork2;

        if (this.mPhoneWork2 == null) {
            this.mPhoneWork2 = "";
        }
    }

    public String getFaxHome() {
        return mFaxHome;
    }

    public void setFaxHome(String faxHome) {
        this.mFaxHome = faxHome;

        if (this.mFaxHome == null) {
            this.mFaxHome = "";
        }
    }

    public String getFaxWork() {
        return mFaxWork;
    }

    public void setFaxWork(String faxWork) {
        this.mFaxWork = faxWork;

        if (this.mFaxWork == null) {
            this.mFaxWork = "";
        }
    }

    public String getPager() {
        return mPager;
    }

    public void setPager(String pager) {
        this.mPager = pager;

        if (this.mPager == null) {
            this.mPager = "";
        }
    }

    public String getOtherPhone1() {
        return mOtherPhone1;
    }

    public void setOtherPhone1(String otherPhone1) {
        this.mOtherPhone1 = otherPhone1;

        if (this.mOtherPhone1 == null) {
            this.mOtherPhone1 = "";
        }
    }

    public String getOtherPhone2() {
        return mOtherPhone2;
    }

    public void setOtherPhone2(String otherPhone2) {
        this.mOtherPhone2 = otherPhone2;

        if (this.mOtherPhone2 == null) {
            this.mOtherPhone2 = "";
        }
    }

    public String getCustomPhone() {
        return mCustomPhone;
    }

    public void setCustomPhone(String customPhone) {
        this.mCustomPhone = customPhone;

        if (this.mCustomPhone == null) {
            this.mCustomPhone = "";
        }
    }

    public String getCustomPhoneType() {
        return mCustomPhoneType;
    }

    public void setCustomPhoneType(String customPhoneType) {
        this.mCustomPhoneType = customPhoneType;

        if (this.mCustomPhoneType == null) {
            this.mCustomPhoneType = "";
        }
    }

    public String getHomeEmail() {
        return mHomeEmail;
    }

    public void setHomeEmail(String homeEmail) {
        this.mHomeEmail = homeEmail;

        if (this.mHomeEmail == null) {
            this.mHomeEmail = "";
        }
    }

    public String getWorkEmail() {
        return mWorkEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.mWorkEmail = workEmail;

        if (this.mWorkEmail == null) {
            this.mWorkEmail = "";
        }
    }

    public String getOtherEmail() {
        return mOtherEmail;
    }

    public void setOtherEmail(String otherEmail) {
        this.mOtherEmail = otherEmail;

        if (this.mOtherEmail == null) {
            this.mOtherEmail = "";
        }
    }

    public String getCustomEmail() {
        return mCustomEmail;
    }

    public void setCustomEmail(String customEmail) {
        this.mCustomEmail = customEmail;

        if (this.mCustomEmail == null) {
            this.mCustomEmail = "";
        }
    }

    public String getCustomEmailType() {
        return mCustomEmailType;
    }

    public void setCustomEmailType(String customEmailType) {
        this.mCustomEmailType = customEmailType;

        if (this.mCustomEmailType == null) {
            this.mCustomEmailType = "";
        }
    }

    public String getHomeAddress() {
        return mHomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.mHomeAddress = homeAddress;

        if (this.mHomeAddress == null) {
            this.mHomeAddress = "";
        }
    }

    public String getWorkAddress() {
        return mWorkAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.mWorkAddress = workAddress;

        if (this.mWorkAddress == null) {
            this.mWorkAddress = "";
        }
    }

    public String getOtherAddress() {
        return mOtherAddress;
    }

    public void setOtherAddress(String otherAddress) {
        this.mOtherAddress = otherAddress;

        if (this.mOtherAddress == null) {
            this.mOtherAddress = "";
        }
    }

    public String getCustomAddress() {
        return mCustomAddress;
    }

    public void setCustomAddress(String customAddress) {
        this.mCustomAddress = customAddress;

        if (this.mCustomAddress == null) {
            this.mCustomAddress = "";
        }
    }

    public String getCustomAddressType() {
        return mCustomAddressType;
    }

    public void setCustomAddressType(String customAddressType) {
        this.mCustomAddressType = customAddressType;

        if (this.mCustomAddressType == null) {
            this.mCustomAddressType = "";
        }
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String companyName) {
        this.mCompanyName = companyName;

        if (this.mCompanyName == null) {
            this.mCompanyName = "";
        }
    }

    public String getCompanyDepartment() {
        return mCompanyDepartment;
    }

    public void setCompanyDepartment(String companyDepartment) {
        this.mCompanyDepartment = companyDepartment;

        if (this.mCompanyDepartment == null) {
            this.mCompanyDepartment = "";
        }
    }

    public String getCompanyTitle() {
        return mCompanyTitle;
    }

    public void setCompanyTitle(String companyTitle) {
        this.mCompanyTitle = companyTitle;

        if (this.mCompanyTitle == null) {
            this.mCompanyTitle = "";
        }
    }

    public String getBirthdayEvent() {
        return mBirthdayEvent;
    }

    public void setBirthdayEvent(String birthdayEvent) {
        this.mBirthdayEvent = birthdayEvent;

        if (this.mBirthdayEvent == null) {
            this.mBirthdayEvent = "";
        }
    }

    public String getAnniversaryEvent() {
        return mAnniversaryEvent;
    }

    public void setAnniversaryEvent(String anniversaryEvent) {
        this.mAnniversaryEvent = anniversaryEvent;

        if (this.mAnniversaryEvent == null) {
            this.mAnniversaryEvent = "";
        }
    }

    public String getOtherEvent() {
        return mOtherEvent;
    }

    public void setOtherEvent(String otherEvent) {
        this.mOtherEvent = otherEvent;

        if (this.mOtherEvent == null) {
            this.mOtherEvent = "";
        }
    }

    public String getCustomEvent() {
        return mCustomEvent;
    }

    public void setCustomEvent(String customEvent) {
        this.mCustomEvent = customEvent;

        if (this.mCustomEvent == null) {
            this.mCustomEvent = "";
        }
    }

    public String getCustomEventType() {
        return mCustomEventType;
    }

    public void setCustomEventType(String customEventType) {
        this.mCustomEventType = customEventType;

        if (this.mCustomEventType == null) {
            this.mCustomEventType = "";
        }
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        this.mWebsite = website;

        if (this.mWebsite == null) {
            this.mWebsite = "";
        }
    }

    public String getMessenger() {
        return mMessenger;
    }

    public void setMessenger(String messenger) {
        this.mMessenger = messenger;

        if (this.mMessenger == null) {
            this.mMessenger = "";
        }
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        this.mNote = note;

        if (this.mNote == null) {
            this.mNote = "";
        }
    }

    public String getRelation() {
        return mRelation;
    }

    public void setRelation(String relation) {
        this.mRelation = relation;

        if (this.mRelation == null) {
            this.mRelation = "";
        }
    }

    public String getPhotoUri() {
        return mPhotoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.mPhotoUri = photoUri;

        if (this.mPhotoUri == null) {
            this.mPhotoUri = "";
        }
    }

    public String getPttNumber() {
        return mPttNumber;
    }

    public void setPttNumber(String number) {
        this.mPttNumber = number;

        if (this.mPttNumber == null) {
            this.mPttNumber = "";
        }
    }

    public String getPttPhotoUri() {
        return mPttPhotoUri;
    }

    public void setPttPhotoUri(String photoUri) {
        this.mPttPhotoUri = photoUri;

        if (this.mPttPhotoUri == null) {
            this.mPttPhotoUri = "";
        }
    }

    public String getPttPresence() {
        return mPttPresence;
    }

    public void setPttPresence(String presence) {
        this.mPttPresence = presence;

        if (this.mPttPresence == null) {
            this.mPttPresence = "";
        }
    }

    public boolean isFavorite() {
        return mFavorite == 1 ? true : false;
    }

    public void setFavorite(boolean favorite) {
        this.mFavorite = (byte) (favorite == true ? 1 : 0);
    }

    public boolean isAdhocMember() {
        return mAdhocMember == 1 ? true : false;
    }

    public void setAdhocMember(boolean adhoc) {
        this.mAdhocMember = (byte) (adhoc == true ? 1 : 0);
    }

    public byte getUpdated() {
        return mUpdated;
    }

    public void setUpdated(int updated) {
        this.mUpdated = (byte) updated;
    }

    public String getPhonePrimary() {
        return mPhonePrimary;
    }

    public void setPhonePrimary(String phonePrimary) {
        this.mPhonePrimary = phonePrimary;

        if (this.mPhonePrimary == null) {
            this.mPhonePrimary = "";
        }
    }

    public String getSearchKey() {
        return mSearchKey;
    }

    public void setSearchKey(String searchKey) {
        this.mSearchKey = searchKey;

        if (this.mSearchKey == null) {
            this.mSearchKey = "";
        }
    }

    public String getListLabel() {
        return mListLabel;
    }

    public void setListLabel(String listLabel) {
        this.mListLabel = listLabel;

        if (this.mListLabel == null) {
            this.mListLabel = "";
        }
    }

    public String getTypicalId() {
        return mTypicalId;
    }

    public void setTypicalId(String typicalId) {
        this.mTypicalId = typicalId;

        if (this.mTypicalId == null) {
            this.mTypicalId = "";
        }
    }

    public int getSearchMatchedNameStart() {
        return mSearchMatchedNameStart;
    }

    public void setSearchMatchedNameStart(int searchMatchedNameStart) {
        this.mSearchMatchedNameStart = searchMatchedNameStart;
    }

    public int getSearchMatchedNameEnd() {
        return mSearchMatchedNameEnd;
    }

    public void setSearchMatchedNameEnd(int searchMatchedNameEnd) {
        this.mSearchMatchedNameEnd = searchMatchedNameEnd;
    }

    public int getSearchMatchedNumberStart() {
        return mSearchMatchedNumberStart;
    }

    public void setSearchMatchedNumberStart(int searchMatchedNumberStart) {
        this.mSearchMatchedNumberStart = searchMatchedNumberStart;
    }

    public int getSearchMatchedNumberEnd() {
        return mSearchMatchedNumberEnd;
    }

    public void setSearchMatchedNumberEnd(int searchMatchedNumberEnd) {
        this.mSearchMatchedNumberEnd = searchMatchedNumberEnd;
    }

    public String getDispatcherID() {
        return this.mDispatcherID;
    }

    public void setDispatcherID(String iD) {
        this.mDispatcherID = iD;
        if (this.mDispatcherID == null) {
            this.mDispatcherID = "";
        }
    }

    public int getQoE() {
        return this.mQoE;
    }

    public void setQoE(int qoe) {
        this.mQoE = qoe;
    }

    public int getLock() {
        return this.mLock;
    }

    public void setLock(int lock) {
        this.mLock = lock;
    }

    public void setDataList() {
        mDataList.clear();

        // Ptt field is mandatory
        BluePageContactsDetailListObject pttNumberObj = new BluePageContactsDetailListObject();
        pttNumberObj.setType(BluePageContactsDao.TYPE_PTT);
        pttNumberObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_ptt);
        pttNumberObj.setData(mPttNumber);
        mDataList.add(pttNumberObj);

        if (StringUtils.isNotEmpty(mPhoneMobile1)) {
            BluePageContactsDetailListObject phoneMobileObj1 = new BluePageContactsDetailListObject();
            phoneMobileObj1.setType(BluePageContactsDao.TYPE_PHONE);
            phoneMobileObj1.setTypeLabelResId(R.string.contacts_detail_listitem_title_phone_mobile1);
            phoneMobileObj1.setData(mPhoneMobile1);
            mDataList.add(phoneMobileObj1);
        }

        if (StringUtils.isNotEmpty(mPhoneMobile2)) {
            BluePageContactsDetailListObject phoneMobileObj2 = new BluePageContactsDetailListObject();
            phoneMobileObj2.setType(BluePageContactsDao.TYPE_PHONE);
            phoneMobileObj2.setTypeLabelResId(R.string.contacts_detail_listitem_title_phone_mobile2);
            phoneMobileObj2.setData(mPhoneMobile2);
            mDataList.add(phoneMobileObj2);
        }

        if (StringUtils.isNotEmpty(mPhoneMobile3)) {
            BluePageContactsDetailListObject phoneMobileObj3 = new BluePageContactsDetailListObject();
            phoneMobileObj3.setType(BluePageContactsDao.TYPE_PHONE);
            phoneMobileObj3.setTypeLabelResId(R.string.contacts_detail_listitem_title_phone_mobile3);
            phoneMobileObj3.setData(mPhoneMobile3);
            mDataList.add(phoneMobileObj3);
        }

        if (StringUtils.isNotEmpty(mPhoneHome1)) {
            BluePageContactsDetailListObject phoneHomeObj1 = new BluePageContactsDetailListObject();
            phoneHomeObj1.setType(BluePageContactsDao.TYPE_PHONE);
            phoneHomeObj1.setTypeLabelResId(R.string.contacts_detail_listitem_title_phone_home1);
            phoneHomeObj1.setData(mPhoneHome1);
            mDataList.add(phoneHomeObj1);
        }

        if (StringUtils.isNotEmpty(mPhoneHome2)) {
            BluePageContactsDetailListObject phoneHomeObj2 = new BluePageContactsDetailListObject();
            phoneHomeObj2.setType(BluePageContactsDao.TYPE_PHONE);
            phoneHomeObj2.setTypeLabelResId(R.string.contacts_detail_listitem_title_phone_home2);
            phoneHomeObj2.setData(mPhoneHome2);
            mDataList.add(phoneHomeObj2);
        }

        if (StringUtils.isNotEmpty(mPhoneWork1)) {
            BluePageContactsDetailListObject phoneWorkObj1 = new BluePageContactsDetailListObject();
            phoneWorkObj1.setType(BluePageContactsDao.TYPE_PHONE);
            phoneWorkObj1.setTypeLabelResId(R.string.contacts_detail_listitem_title_phoen_work1);
            phoneWorkObj1.setData(mPhoneWork1);
            mDataList.add(phoneWorkObj1);
        }

        if (StringUtils.isNotEmpty(mPhoneWork2)) {
            BluePageContactsDetailListObject phoneWorkObj2 = new BluePageContactsDetailListObject();
            phoneWorkObj2.setType(BluePageContactsDao.TYPE_PHONE);
            phoneWorkObj2.setTypeLabelResId(R.string.contacts_detail_listitem_title_phoen_work2);
            phoneWorkObj2.setData(mPhoneWork2);
            mDataList.add(phoneWorkObj2);
        }

        if (StringUtils.isNotEmpty(mFaxHome)) {
            BluePageContactsDetailListObject faxHomeObj = new BluePageContactsDetailListObject();
            faxHomeObj.setType(BluePageContactsDao.TYPE_PHONE);
            faxHomeObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_fax_home);
            faxHomeObj.setData(mFaxHome);
            mDataList.add(faxHomeObj);
        }

        if (StringUtils.isNotEmpty(mFaxWork)) {
            BluePageContactsDetailListObject faxWorkObj = new BluePageContactsDetailListObject();
            faxWorkObj.setType(BluePageContactsDao.TYPE_PHONE);
            faxWorkObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_fax_work);
            faxWorkObj.setData(mFaxWork);
            mDataList.add(faxWorkObj);
        }

        if (StringUtils.isNotEmpty(mPager)) {
            BluePageContactsDetailListObject pagerObj = new BluePageContactsDetailListObject();
            pagerObj.setType(BluePageContactsDao.TYPE_OTHER);
            pagerObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_pager);
            pagerObj.setData(mPager);
            mDataList.add(pagerObj);
        }

        if (StringUtils.isNotEmpty(mOtherPhone1)) {
            BluePageContactsDetailListObject otherPhoneObj1 = new BluePageContactsDetailListObject();
            otherPhoneObj1.setType(BluePageContactsDao.TYPE_PHONE);
            otherPhoneObj1.setTypeLabelResId(R.string.contacts_detail_listitem_title_other_phone1);
            otherPhoneObj1.setData(mOtherPhone1);
            mDataList.add(otherPhoneObj1);
        }

        if (StringUtils.isNotEmpty(mOtherPhone2)) {
            BluePageContactsDetailListObject otherPhoneObj2 = new BluePageContactsDetailListObject();
            otherPhoneObj2.setType(BluePageContactsDao.TYPE_PHONE);
            otherPhoneObj2.setTypeLabelResId(R.string.contacts_detail_listitem_title_other_phone2);
            otherPhoneObj2.setData(mOtherPhone2);
            mDataList.add(otherPhoneObj2);
        }

        if (StringUtils.isNotEmpty(mCustomPhone)) {
            BluePageContactsDetailListObject customPhoneObj = new BluePageContactsDetailListObject();
            customPhoneObj.setType(BluePageContactsDao.TYPE_PHONE);
            customPhoneObj.setTypeLabelResId(0);
            customPhoneObj.setData(mCustomPhone);
            customPhoneObj.setCustomType(mCustomPhoneType);
            mDataList.add(customPhoneObj);
        }

        if (StringUtils.isNotEmpty(mHomeEmail)) {
            BluePageContactsDetailListObject homeEmailObj = new BluePageContactsDetailListObject();
            homeEmailObj.setType(BluePageContactsDao.TYPE_EMAIL);
            homeEmailObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_home_email);
            homeEmailObj.setData(mHomeEmail);
            mDataList.add(homeEmailObj);
        }

        if (StringUtils.isNotEmpty(mWorkEmail)) {
            BluePageContactsDetailListObject workEmailObj = new BluePageContactsDetailListObject();
            workEmailObj.setType(BluePageContactsDao.TYPE_EMAIL);
            workEmailObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_work_email);
            workEmailObj.setData(mWorkEmail);
            mDataList.add(workEmailObj);
        }

        if (StringUtils.isNotEmpty(mOtherEmail)) {
            BluePageContactsDetailListObject otherEmailObj = new BluePageContactsDetailListObject();
            otherEmailObj.setType(BluePageContactsDao.TYPE_EMAIL);
            otherEmailObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_other_email);
            otherEmailObj.setData(mOtherEmail);
            mDataList.add(otherEmailObj);
        }

        if (StringUtils.isNotEmpty(mCustomEmailType)) {
            BluePageContactsDetailListObject customEmailObj = new BluePageContactsDetailListObject();
            customEmailObj.setType(BluePageContactsDao.TYPE_EMAIL);
            customEmailObj.setTypeLabelResId(0);
            customEmailObj.setData(mCustomEmail);
            customEmailObj.setCustomType(mCustomEmailType);
            mDataList.add(customEmailObj);
        }

        if (StringUtils.isNotEmpty(mHomeAddress)) {
            BluePageContactsDetailListObject homeAddressObj = new BluePageContactsDetailListObject();
            homeAddressObj.setType(BluePageContactsDao.TYPE_OTHER);
            homeAddressObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_home_address);
            homeAddressObj.setData(mHomeAddress);
            mDataList.add(homeAddressObj);
        }

        if (StringUtils.isNotEmpty(mWorkAddress)) {
            BluePageContactsDetailListObject workAddressObj = new BluePageContactsDetailListObject();
            workAddressObj.setType(BluePageContactsDao.TYPE_OTHER);
            workAddressObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_work_address);
            workAddressObj.setData(mWorkAddress);
            mDataList.add(workAddressObj);
        }

        if (StringUtils.isNotEmpty(mOtherAddress)) {
            BluePageContactsDetailListObject otherAddressObj = new BluePageContactsDetailListObject();
            otherAddressObj.setType(BluePageContactsDao.TYPE_OTHER);
            otherAddressObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_other_address);
            otherAddressObj.setData(mOtherAddress);
            mDataList.add(otherAddressObj);
        }

        if (StringUtils.isNotEmpty(mCustomAddress)) {
            BluePageContactsDetailListObject customAddressObj = new BluePageContactsDetailListObject();
            customAddressObj.setType(BluePageContactsDao.TYPE_OTHER);
            customAddressObj.setTypeLabelResId(0);
            customAddressObj.setData(mCustomAddress);
            customAddressObj.setCustomType(mCustomAddressType);
            mDataList.add(customAddressObj);
        }

        if (StringUtils.isNotEmpty(mCompanyName)) {
            BluePageContactsDetailListObject companyNameObj = new BluePageContactsDetailListObject();
            companyNameObj.setType(BluePageContactsDao.TYPE_COMPANY_NAME);
            companyNameObj.setData(mCompanyName);
            mDataList.add(companyNameObj);
        }

        if (StringUtils.isNotEmpty(mCompanyDepartment)) {
            BluePageContactsDetailListObject companyDeaprtmentObj = new BluePageContactsDetailListObject();
            companyDeaprtmentObj.setType(BluePageContactsDao.TYPE_COMPANY_DEPARTMENT);
            companyDeaprtmentObj.setData(mCompanyDepartment);
            mDataList.add(companyDeaprtmentObj);
        }

        if (StringUtils.isNotEmpty(mCompanyTitle)) {
            BluePageContactsDetailListObject companyTitleObj = new BluePageContactsDetailListObject();
            companyTitleObj.setType(BluePageContactsDao.TYPE_COMPANY_TITLE);
            companyTitleObj.setData(mCompanyTitle);
            mDataList.add(companyTitleObj);
        }

        if (StringUtils.isNotEmpty(mBirthdayEvent)) {
            BluePageContactsDetailListObject birthdayEventObj = new BluePageContactsDetailListObject();
            birthdayEventObj.setType(BluePageContactsDao.TYPE_OTHER);
            birthdayEventObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_birthday_event);
            birthdayEventObj.setData(mBirthdayEvent);
            mDataList.add(birthdayEventObj);
        }

        if (StringUtils.isNotEmpty(mAnniversaryEvent)) {
            BluePageContactsDetailListObject anniversaryEventObj = new BluePageContactsDetailListObject();
            anniversaryEventObj.setType(BluePageContactsDao.TYPE_OTHER);
            anniversaryEventObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_anniversary_event);
            anniversaryEventObj.setData(mAnniversaryEvent);
            mDataList.add(anniversaryEventObj);
        }

        if (StringUtils.isNotEmpty(mOtherEvent)) {
            BluePageContactsDetailListObject otherEventObj = new BluePageContactsDetailListObject();
            otherEventObj.setType(BluePageContactsDao.TYPE_OTHER);
            otherEventObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_other_event);
            otherEventObj.setData(mOtherEvent);
            mDataList.add(otherEventObj);
        }

        if (StringUtils.isNotEmpty(mCustomEventType)) {
            BluePageContactsDetailListObject customEventObj = new BluePageContactsDetailListObject();
            customEventObj.setType(BluePageContactsDao.TYPE_OTHER);
            customEventObj.setTypeLabelResId(0);
            customEventObj.setData(mCustomEvent);
            customEventObj.setCustomType(mCustomEventType);
            mDataList.add(customEventObj);
        }

        if (StringUtils.isNotEmpty(mWebsite)) {
            BluePageContactsDetailListObject websiteObj = new BluePageContactsDetailListObject();
            websiteObj.setType(BluePageContactsDao.TYPE_WEBSITE);
            websiteObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_website);
            websiteObj.setData(mWebsite);
            mDataList.add(websiteObj);
        }

        if (StringUtils.isNotEmpty(mMessenger)) {
            BluePageContactsDetailListObject messengerObj = new BluePageContactsDetailListObject();
            messengerObj.setType(BluePageContactsDao.TYPE_OTHER);
            messengerObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_messenger);
            messengerObj.setData(mMessenger);
            mDataList.add(messengerObj);
        }

        if (StringUtils.isNotEmpty(mNote)) {
            BluePageContactsDetailListObject noteObj = new BluePageContactsDetailListObject();
            noteObj.setType(BluePageContactsDao.TYPE_OTHER);
            noteObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_note);
            noteObj.setData(mNote);
            mDataList.add(noteObj);
        }

        if (StringUtils.isNotEmpty(mRelation)) {
            BluePageContactsDetailListObject relationObj = new BluePageContactsDetailListObject();
            relationObj.setType(BluePageContactsDao.TYPE_OTHER);
            relationObj.setTypeLabelResId(R.string.contacts_detail_listitem_title_relation);
            relationObj.setData(mRelation);
            mDataList.add(relationObj);
        }
    }

    public ArrayList<BluePageContactsDetailListObject> getDataList() {
        return this.mDataList;
    }

    public static final Parcelable.Creator<BluePageContactsModel> CREATOR = new Parcelable.Creator<BluePageContactsModel>() {

        @Override
        public BluePageContactsModel createFromParcel(Parcel source) {
            return new BluePageContactsModel(source);
        }

        @Override
        public BluePageContactsModel[] newArray(int size) {
            return new BluePageContactsModel[size];
        }
    };

    public BluePageContactsModel(Parcel in) {
        _id = in.readInt();
        mContactId = in.readString();
        mLookupKey = in.readString();
        mDisplayName = in.readString();
        mFamilyName = in.readString();
        mMiddleName = in.readString();
        mGivenName = in.readString();
        mNickName = in.readString();
        mPhoneMobile1 = in.readString();
        mPhoneMobile2 = in.readString();
        mPhoneMobile3 = in.readString();
        mPhoneHome1 = in.readString();
        mPhoneHome2 = in.readString();
        mPhoneWork1 = in.readString();
        mPhoneWork2 = in.readString();
        mFaxHome = in.readString();
        mFaxWork = in.readString();
        mPager = in.readString();
        mOtherPhone1 = in.readString();
        mOtherPhone2 = in.readString();
        mCustomPhone = in.readString();
        mCustomPhoneType = in.readString();
        mHomeEmail = in.readString();
        mWorkEmail = in.readString();
        mOtherEmail = in.readString();
        mCustomEmail = in.readString();
        mCustomEmailType = in.readString();
        mHomeAddress = in.readString();
        mWorkAddress = in.readString();
        mOtherAddress = in.readString();
        mCustomAddress = in.readString();
        mCustomAddressType = in.readString();
        mCompanyName = in.readString();
        mCompanyDepartment = in.readString();
        mCompanyTitle = in.readString();
        mBirthdayEvent = in.readString();
        mAnniversaryEvent = in.readString();
        mOtherEvent = in.readString();
        mCustomEvent = in.readString();
        mCustomEventType = in.readString();
        mWebsite = in.readString();
        mMessenger = in.readString();
        mNote = in.readString();
        mRelation = in.readString();
        mPhotoUri = in.readString();

        mPttNumber = in.readString();
        mPttPhotoUri = in.readString();
        mPttPresence = in.readString();
        mFavorite = in.readByte();
        mAdhocMember = in.readByte();
        mUpdated = in.readByte();

        mPhonePrimary = in.readString();
        mSearchKey = in.readString();
        mListLabel = in.readString();
        mTypicalId = in.readString();
        mDispatcherID = in.readString();
        mQoE = in.readInt();
        mLock = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(mContactId);
        dest.writeString(mLookupKey);
        dest.writeString(mDisplayName);
        dest.writeString(mFamilyName);
        dest.writeString(mMiddleName);
        dest.writeString(mGivenName);
        dest.writeString(mNickName);
        dest.writeString(mPhoneMobile1);
        dest.writeString(mPhoneMobile2);
        dest.writeString(mPhoneMobile3);
        dest.writeString(mPhoneHome1);
        dest.writeString(mPhoneHome2);
        dest.writeString(mPhoneWork1);
        dest.writeString(mPhoneWork2);
        dest.writeString(mFaxHome);
        dest.writeString(mFaxWork);
        dest.writeString(mPager);
        dest.writeString(mOtherPhone1);
        dest.writeString(mOtherPhone2);
        dest.writeString(mCustomPhone);
        dest.writeString(mCustomPhoneType);
        dest.writeString(mHomeEmail);
        dest.writeString(mWorkEmail);
        dest.writeString(mOtherEmail);
        dest.writeString(mCustomEmail);
        dest.writeString(mCustomEmailType);
        dest.writeString(mHomeAddress);
        dest.writeString(mWorkAddress);
        dest.writeString(mOtherAddress);
        dest.writeString(mCustomAddress);
        dest.writeString(mCustomAddressType);
        dest.writeString(mCompanyName);
        dest.writeString(mCompanyDepartment);
        dest.writeString(mCompanyTitle);
        dest.writeString(mBirthdayEvent);
        dest.writeString(mAnniversaryEvent);
        dest.writeString(mOtherEvent);
        dest.writeString(mCustomEvent);
        dest.writeString(mCustomEventType);
        dest.writeString(mWebsite);
        dest.writeString(mMessenger);
        dest.writeString(mNote);
        dest.writeString(mRelation);
        dest.writeString(mPhotoUri);

        dest.writeString(mPttNumber);
        dest.writeString(mPttPhotoUri);
        dest.writeString(mPttPresence);
        dest.writeByte(mFavorite);
        dest.writeByte(mAdhocMember);
        dest.writeByte(mUpdated);

        dest.writeString(mPhonePrimary);
        dest.writeString(mSearchKey);
        dest.writeString(mListLabel);
        dest.writeString(mTypicalId);
        dest.writeString(mDispatcherID);
        dest.writeInt(mQoE);
        dest.writeInt(mLock);
    }

    @Override
    public BluePageContactsModel clone() throws CloneNotSupportedException {
        return (BluePageContactsModel) super.clone();
    }
}
