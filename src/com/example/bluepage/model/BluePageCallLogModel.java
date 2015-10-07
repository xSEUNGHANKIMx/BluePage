package com.example.bluepage.model;

import org.apache.commons.lang3.StringUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class BluePageCallLogModel {

    int _mId = -1;
    String mCallId;
    String mName;
    String mNumber;
    long mStartTimeMillis;
    long mEndTimeMillis;
    long mDurationMillis;
    String mStartTimeString;
    String mEndTimeString;
    String mDurationString;
    int mCallType;
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

    public BluePageCallLogModel() {
        _mId = -1;
        mCallId = "";
        mName = "";
        mNumber = "";
        mStartTimeMillis = 0;
        mEndTimeMillis = 0;
        mDurationMillis = 0;
        mStartTimeString = "";
        mEndTimeString = "";
        mDurationString = "";
        mCallType = 0;
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
    }

    public int getId() {
        return _mId;
    }

    public void setId(int id) {
        this._mId = id;
    }

    public String getCallId() {
        return mCallId;
    }

    public void setCallId(String callId) {
        this.mCallId = callId;

        if (this.mCallId == null) {
            this.mCallId = "";
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

            if (StringUtils.isNotEmpty(mNormalized_number) && (mNormalized_number.length() > 2)) {
                try {
                    phoneNumber = phoneUtil.parse(mNormalized_number, "KR");
                    formattedNumber = phoneUtil.format(phoneNumber, PhoneNumberFormat.NATIONAL);
                } catch (NumberParseException e) {
                    e.printStackTrace();
                }

                mFormatted_number = formattedNumber;
            } else {
                if (StringUtils.isNotEmpty(mNumber) && (mNormalized_number.length() > 2)) {
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
}

