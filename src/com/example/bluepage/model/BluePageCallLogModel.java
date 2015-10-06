package com.example.bluepage.model;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class BluePageCallLogModel implements Parcelable {

    int _mId = -1;
    String mOemCallId; // using for OEM Call Log.
    String mName;
    String mNumber; // [1] OEM: as Phone Number, [2] Pre-arranged Group: as GroupId, [3] Ad-hoc: as like GroupId.
    long mStartTimeMillis;
    long mEndTimeMillis;
    long mDurationMillis;
    String mStartTimeString;
    String mEndTimeString;
    String mDurationString;
    int mCallType;
    int mCallKind;
    String mCountryiso;
    String mVoicemail_uri;
    String mGeocoded_location;
    String mNumbertype;
    String mNumberlabel;
    String mLookup_uri;
    String mMatched_number;
    String mNormalized_number;
    String mPhoto_id;
    String mFormatted_number;
    int mPresentation;
    String mContacts_id;
    String mListLabel;
    String mServerSessionId;
    String mSessionId;
    byte mIsNew;
    byte mIsPreArgd;

    // Application Data
    private ArrayList<BluePageContactsModel> mMemberEntry;
    private ArrayList<String> mMemberIdEntry;

    public BluePageCallLogModel() {
        _mId = -1;
        mOemCallId = "";
        mName = "";
        mNumber = "";
        mStartTimeMillis = 0;
        mEndTimeMillis = 0;
        mDurationMillis = 0;
        mStartTimeString = "";
        mEndTimeString = "";
        mDurationString = "";
        mCallType = 0;
        mCallKind = 0;
        mCountryiso = "";
        mVoicemail_uri = "";
        mGeocoded_location = "";
        mNumbertype = "";
        mNumberlabel = "";
        mLookup_uri = "";
        mMatched_number = "";
        mNormalized_number = "";
        mPhoto_id = "";
        mFormatted_number = "";
        mPresentation = 0;
        mContacts_id = "";
        mListLabel = "";
        mServerSessionId = "";
        mSessionId = "";
        mIsNew = 1;
        mIsPreArgd = 0;

        mMemberEntry = new ArrayList<BluePageContactsModel>();
        mMemberIdEntry = new ArrayList<String>();
    }

    public int getId() {
        return _mId;
    }

    public void setId(int id) {
        this._mId = id;
    }

    public String getOemCallId() {
        return mOemCallId;
    }

    public void setOemCallId(String oemCallId) {
        this.mOemCallId = oemCallId;

        if (this.mOemCallId == null) {
            this.mOemCallId = "";
        }
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;

        if (this.mName == null) {
            this.mName = "";
        }
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        this.mNumber = number;

        if (this.mNumber == null) {
            this.mNumber = "";
        }
    }

    public long getStartTimeMillis() {
        return mStartTimeMillis;
    }

    public void setStartTimeMillis(long time) {
        this.mStartTimeMillis = time;
    }

    public long getEndTimeMillis() {
        return mEndTimeMillis;
    }

    public void setEndTimeMillis(long time) {
        this.mEndTimeMillis = time;
    }

    public long getDurationMillis() {
        return mDurationMillis;
    }

    public void setDurationMillis(long duration) {
        this.mDurationMillis = duration;
    }

    public String getStartTimeString() {
        return mStartTimeString;
    }

    public void setStartTimeString(String time) {
        this.mStartTimeString = time;

        if (this.mStartTimeString == null) {
            this.mStartTimeString = "";
        }
    }

    public String getEndTimeString() {
        return mEndTimeString;
    }

    public void setEndTimeString(String time) {
        this.mEndTimeString = time;

        if (this.mEndTimeString == null) {
            this.mEndTimeString = "";
        }
    }

    public String getDurationString() {
        return mDurationString;
    }

    public void setDurationString(String duration) {
        this.mDurationString = duration;

        if (this.mDurationString == null) {
            this.mDurationString = "";
        }
    }

    public int getCallType() {
        return mCallType;
    }

    public void setCallType(int type) {
        this.mCallType = type;
    }

    public int getCallKind() {
        return mCallKind;
    }

    public void setCallKind(int kind) {
        this.mCallKind = kind;
    }

    public String getCountryiso() {
        return mCountryiso;
    }

    public void setCountryiso(String countryiso) {
        this.mCountryiso = countryiso;

        if (this.mCountryiso == null) {
            this.mCountryiso = "";
        }
    }

    public String getVoicemail_uri() {
        return mVoicemail_uri;
    }

    public void setVoicemail_uri(String voicemail_uri) {
        this.mVoicemail_uri = voicemail_uri;

        if (this.mVoicemail_uri == null) {
            this.mVoicemail_uri = "";
        }
    }

    public String getGeocoded_location() {
        return mGeocoded_location;
    }

    public void setGeocoded_location(String geocoded_location) {
        this.mGeocoded_location = geocoded_location;

        if (this.mGeocoded_location == null) {
            this.mGeocoded_location = "";
        }
    }

    public String getNumbertype() {
        return mNumbertype;
    }

    public void setNumbertype(String numbertype) {
        this.mNumbertype = numbertype;

        if (this.mNumbertype == null) {
            this.mNumbertype = "";
        }
    }

    public String getNumberlabel() {
        return mNumberlabel;
    }

    public void setNumberlabel(String numberlabel) {
        this.mNumberlabel = numberlabel;

        if (this.mNumberlabel == null) {
            this.mNumberlabel = "";
        }
    }

    public String getLookup_uri() {
        return mLookup_uri;
    }

    public void setLookup_uri(String lookup_uri) {
        this.mLookup_uri = lookup_uri;

        if (this.mLookup_uri == null) {
            this.mLookup_uri = "";
        }
    }

    public String getMatched_number() {
        return mMatched_number;
    }

    public void setMatched_number(String matched_number) {
        this.mMatched_number = matched_number;

        if (this.mMatched_number == null) {
            this.mMatched_number = "";
        }
    }

    public String getNormalized_number() {
        if (StringUtils.isEmpty(mNormalized_number)) {
            mNormalized_number = mNumber;
        }

        return mNormalized_number;
    }

    public void setNormalized_number(String normalized_number) {
        this.mNormalized_number = normalized_number;

        if (this.mNormalized_number == null) {
            this.mNormalized_number = "";
        }
    }

    public String getPhoto_id() {
        return mPhoto_id;
    }

    public void setPhoto_id(String photo_id) {
        this.mPhoto_id = photo_id;

        if (this.mPhoto_id == null) {
            this.mPhoto_id = "";
        }
    }

    public String getFormatted_number() {
        if (StringUtils.isEmpty(mFormatted_number)) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            PhoneNumber phoneNumber = null;
            String formattedNumber = "";

            if (StringUtils.isNotEmpty(mNormalized_number)) {
                try {
                    phoneNumber = phoneUtil.parse(mNormalized_number, "KR");
                    formattedNumber = phoneUtil.format(phoneNumber, PhoneNumberFormat.NATIONAL);
                } catch (NumberParseException e) {
                    e.printStackTrace();
                }

                mFormatted_number = formattedNumber;
            } else {
                if (StringUtils.isNotEmpty(mNumber)) {
                    try {
                        phoneNumber = phoneUtil.parse(mNumber, "KR");
                        formattedNumber = phoneUtil.format(phoneNumber, PhoneNumberFormat.NATIONAL);
                    } catch (NumberParseException e) {
                        e.printStackTrace();
                    }

                    mFormatted_number = formattedNumber;
                } else {
                    mFormatted_number = "";
                }
            }
        }

        return mFormatted_number;
    }

    public void setFormatted_number(String formatted_number) {
        this.mFormatted_number = formatted_number;

        if (this.mFormatted_number == null) {
            this.mFormatted_number = "";
        }
    }

    public int getPresentation() {
        return mPresentation;
    }

    public void setPresentation(int presentation) {
        this.mPresentation = presentation;
    }

    public String getContacts_id() {
        return mContacts_id;
    }

    public void setContacts_id(String contacts_id) {
        this.mContacts_id = contacts_id;

        if (this.mContacts_id == null) {
            this.mContacts_id = "";
        }
    }

    public String getListLabel() {
        return mListLabel;
    }

    public void setListLabel(String label) {
        this.mListLabel = label;

        if (this.mListLabel == null) {
            this.mListLabel = "";
        }
    }

    public String getServerSessionId() {
        return mServerSessionId;
    }

    public void setServerSessionId(String id) {
        this.mServerSessionId = id;

        if (this.mServerSessionId == null) {
            this.mServerSessionId = "";
        }
    }

    public String getSessionId() {
        return mSessionId;
    }

    public void setSessionId(String id) {
        this.mSessionId = id;

        if (this.mSessionId == null) {
            this.mSessionId = "";
        }
    }

    public boolean isNew() {
        return mIsNew == 1 ? true : false;
    }

    public void setNew(boolean isNew) {
        this.mIsNew = (byte) (isNew == true ? 1 : 0);
    }

    public boolean isPreArgd() {
        return mIsPreArgd == 1 ? true : false;
    }

    public void setPreArgd(boolean isPreArgd) {
        this.mIsPreArgd = (byte) (isPreArgd == true ? 1 : 0);
    }

    public ArrayList<String> getMemberIdEntry() {
        return mMemberIdEntry;
    }

    public ArrayList<BluePageContactsModel> getMemberEntry() {
        return mMemberEntry;
    }

    public void setMemberEntry(ArrayList<BluePageContactsModel> entry) {
        this.mMemberEntry = entry;
    }

    public void setMemberEntry(BluePageContactsModel model) {
        this.mMemberEntry.add(model);
    }

    public void setMemberIdEntry(ArrayList<String> entry) {
        HashSet<String> hashEntry = new HashSet<String>(entry);
        this.mMemberIdEntry.clear();
        this.mMemberIdEntry.addAll(hashEntry);
    }

    public void setMemberIdEntry(String id) {
        this.mMemberIdEntry.add(id);
    }

    public static final Parcelable.Creator<BluePageCallLogModel> CREATOR = new Parcelable.Creator<BluePageCallLogModel>() {

        @Override
        public BluePageCallLogModel createFromParcel(Parcel source) {
            return new BluePageCallLogModel(source);
        }

        @Override
        public BluePageCallLogModel[] newArray(int size) {
            return new BluePageCallLogModel[size];
        }
    };

    public BluePageCallLogModel(Parcel in) {
        mMemberEntry = new ArrayList<BluePageContactsModel>();
        mMemberIdEntry = new ArrayList<String>();

        _mId = in.readInt();
        mOemCallId = in.readString();
        mName = in.readString();
        mNumber = in.readString();
        mStartTimeMillis = in.readLong();
        mEndTimeMillis = in.readLong();
        mDurationMillis = in.readLong();
        mStartTimeString = in.readString();
        mEndTimeString = in.readString();
        mDurationString = in.readString();
        mCallType = in.readInt();
        mCallKind = in.readInt();
        mCountryiso = in.readString();
        mVoicemail_uri = in.readString();
        mGeocoded_location = in.readString();
        mNumbertype = in.readString();
        mNumberlabel = in.readString();
        mLookup_uri = in.readString();
        mMatched_number = in.readString();
        mNormalized_number = in.readString();
        mPhoto_id = in.readString();
        mFormatted_number = in.readString();
        mPresentation = in.readInt();
        mContacts_id = in.readString();
        mListLabel = in.readString();
        mServerSessionId = in.readString();
        mSessionId = in.readString();
        mIsNew = in.readByte();
        mIsPreArgd = in.readByte();

        in.readTypedList(mMemberEntry, BluePageContactsModel.CREATOR);
        in.readStringList(mMemberIdEntry);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_mId);
        dest.writeString(mOemCallId);
        dest.writeString(mName);
        dest.writeString(mNumber);
        dest.writeLong(mStartTimeMillis);
        dest.writeLong(mEndTimeMillis);
        dest.writeLong(mDurationMillis);
        dest.writeString(mStartTimeString);
        dest.writeString(mEndTimeString);
        dest.writeString(mDurationString);
        dest.writeInt(mCallType);
        dest.writeInt(mCallKind);
        dest.writeString(mCountryiso);
        dest.writeString(mVoicemail_uri);
        dest.writeString(mGeocoded_location);
        dest.writeString(mNumbertype);
        dest.writeString(mNumberlabel);
        dest.writeString(mLookup_uri);
        dest.writeString(mMatched_number);
        dest.writeString(mNormalized_number);
        dest.writeString(mPhoto_id);
        dest.writeString(mFormatted_number);
        dest.writeInt(mPresentation);
        dest.writeString(mContacts_id);
        dest.writeString(mListLabel);
        dest.writeString(mServerSessionId);
        dest.writeString(mSessionId);
        dest.writeByte(mIsNew);
        dest.writeByte(mIsPreArgd);

        dest.writeTypedList(mMemberEntry);
        dest.writeStringList(mMemberIdEntry);
    }
}

