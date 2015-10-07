package com.example.bluepage.model;


public class BluePageContactsModel implements Cloneable {

    int _id;
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

    String mPhonePrimary;
    String mSearchKey; // 초성검색용
    String mListLabel; // 리스트 라벨용

    int mSearchMatchedNameStart;
    int mSearchMatchedNameEnd;
    int mSearchMatchedNumberStart;
    int mSearchMatchedNumberEnd;


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

        mPhonePrimary = "";
        mSearchKey = "";
        mListLabel = "";

        mSearchMatchedNameStart = -1;
        mSearchMatchedNameEnd = -1;
        mSearchMatchedNumberStart = -1;
        mSearchMatchedNumberEnd = -1;
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

    @Override
    public BluePageContactsModel clone() throws CloneNotSupportedException {
        return (BluePageContactsModel) super.clone();
    }
}
