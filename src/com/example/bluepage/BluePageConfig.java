package com.example.bluepage;

public class BluePageConfig {

    public static final String BLUEPAGE_CONTACTS_DB_NAME = "bluepage_contacts_db";
    public static final String BLUEPAGE_CALL_LOG_DB_NAME = "bluepage_calllog_db";
    public static final String PREF_KEY_CONTACTS_MEMBERS_UPDATE_TIMESTAMP = "contacts_members_update_timestamp";
    public static final String INDEX_SCROLLER_STRING_EN = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String BLUEPAGE_TAB_1 = "tab_1_dial";
    public static final String BLUEPAGE_TAB_2 = "tab_2_callLog";
    public static final String BLUEPAGE_TAB_3 = "tab_3_contacts";

    public static final int CONTACTS_LOADER_ID = 1000;
    public static final int CALL_LOG_LOADER_ID = 2000;

    public static final int TYPE_INCOMMING_CALL = 1;
    public static final int TYPE_OUTGOING_CALL = 2;
    public static final int TYPE_MISSED_CALL = 3;
    public static final int TYPE_REJECTED_CALL = 10;
    public static final int TYPE_SMS_SENDING = 813;
    public static final int TYPE_SMS_RECEIVING = 814;
    public static final int TYPE_MMS_SENDING = 819;
    public static final int TYPE_MMS_RECEIVING = 820;

    public static final String DATA_BASE_NAME = "db_sptt_calllog";
    public static final String PREF_KEY_CALLLOG_UPDATE_TIMESTAMP = "calllog_update_timestamp";

    /* 발신을 하면 제일 먼저 Session ID를 얻어오게 되고,
     * 그 다음 ServerSession ID를 기다리게 되는데 마침내 ServerSession ID가 들어오게 되면
            해당 Pending되어 있는 CallLog를 찾는데 사용되는  ID.*/
    public static String mPendingServerSessionId = "";
}
