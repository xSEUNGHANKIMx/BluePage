package com.example.bluepage.views;

public class BluePageContactsListObject {

    public final static int LIST_TYPE_UNKNOWN = 0;
    public final static int LIST_TYPE_LABEL = 1;
    public final static int LIST_TYPE_CONTACT = 2;
    public final static int LIST_TYPE_MAX = 3;

    private int mType;
    private Object mObject;
    private String mLabel;

    public BluePageContactsListObject() {

    }

    public BluePageContactsListObject(int type, Object obj, String label) {
        mType = type;
        mObject = obj;
        mLabel = label;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }

    public void setObject(Object obj) {
        mObject = obj;
    }

    public Object getObject() {
        return mObject;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getLabel() {
        return mLabel;
    }
}
