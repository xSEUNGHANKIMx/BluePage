package com.example.bluepage.views;

public class BluePageContactsDetailListObject {

    private int mType = 0;
    private int mTyepLabelResId = 0;
    private String mData = "";
    private String mCustomType = "";

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public int getTypeLabelResId() {
        return mTyepLabelResId;
    }

    public void setTypeLabelResId(int resId) {
        this.mTyepLabelResId = resId;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        if (data != null) {
            this.mData = data;
        } else {
            this.mData = "";
        }
    }

    public String getCustomType() {
        return mCustomType;
    }

    public void setCustomType(String customType) {
        if (customType != null) {
            this.mCustomType = customType;
        } else {
            this.mData = "";
        }
    }
}
