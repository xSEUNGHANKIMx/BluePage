package com.example.bluepage;

public class BluePageConstants {

    public static final String INTENT_ACTION_MAIN = ".MAIN";
    public static final String INTENT_ACTION_CALL_DIALER = ".CALL_DIALER";
    public static final String INTENT_ACTION_CONTACTS = ".CONTACTS";
    public static final String INTENT_ACTION_CONTACTS_DETAIL = ".CONTACTS_DETAIL";
    public static final String INTENT_ACTION_CONTACTS_EDIT = ".CONTACTS_EDIT";
    public static final String INTENT_ACTION_CONTACTS_SELECTABLE_GROUPS = ".CONTACTS_SELECTABLE_GROUPS";
    public static final String INTENT_ACTION_CONTACTS_SELECTABLE_MEMBERS = ".CONTACTS_SELECTABLE_MEMBERS";
    public static final String INTENT_ACTION_CALL_LOG = ".CALL_LOG";
    public static final String INTENT_ACTION_CALL_LOG_DETAIL_EVENT = ".CALL_LOG_DETAIL_EVENT";
    public static final String INTENT_ACTION_MESSAGE = ".MESSAGE";
    public static final String INTENT_ACTION_MESSAGE_SEARCH = ".MESSAGE_SEARCH";
    public static final String INTENT_ACTION_OVERLAY_MESSAGE_WRITER = ".OVERLAY_MESSAGE_WRITER";
    public static final String INTENT_ACTION_FILE_BOX = ".FILE_BOX";
    public static final String INTENT_ACTION_MEDIA_SEARCH = ".MEDIA_SEARCH";
    public static final String INTENT_ACTION_SETTINGS = ".SETTINGS";
    public static final String INTENT_ACTION_MAKE_CALL = ".MAKE_CALL";
    public static final String INTENT_ACTION_STARTER = ".STARTER";
    public static final String INTENT_ACTION_END_CALL = ".END_CALL";

    public static final String INTENT_KEY_CONTACTS_CONTACT_ID = "CONTACT_ID";
    public static final String INTENT_KEY_CONTACTS_LOOKUP_KEY = "LOOKUP_KEY";
    public static final String INTENT_KEY_CONTACTS_PTT_NUMBER = "PTT_NUMBER";
    public static final String INTENT_KEY_CONTACTS_TYPICAL_ID = "TYPICAL_ID";
    public static final String INTENT_KEY_CONTACTS_IS_ADD_NEW = "IS_ADD_NEW";
    public static final String INTENT_KEY_CONTACTS_IS_SELECTABLE_MODE = "CONTACTS_IS_SELECTABLE_MODE";
    public static final String INTENT_KEY_CONTACTS_IS_CHANGED_TO_SELECTABLE = "CONTACTS_IS_CHANGED_TO_SELECTABLE";
    public static final String INTENT_KEY_CONTACTS_SELECTABLE_BUTTON_TYPE = "CONTACTS_SELECTABLE_BUTTON_TYPE";
    public static final String INTENT_KEY_CONTACTS_PRESELECTED_LIST = "CONTACTS_PRESELECTED_LIST";
    public static final String INTENT_KEY_CONTACTS_SELECTED_LIST = "CONTACTS_SELECTED_LIST";
    public static final String INTENT_KEY_CONTACTS_IS_PRE_ARRANGED = "CONTACTS_IS_PRE_ARRANGED";
    public static final String INTENT_KEY_CONTACTS_IS_HDX = "CONTACTS_IS_HDX";
    public static final String INTENT_KEY_CONTACTS_IS_VOICE = "CONTACTS_IS_VOICE";
    public static final String INTENT_KEY_CONTACTS_GROUP_IS_SELECTABLE = "CONTACTS_GROUP_IS_SELECTABLE";
    public static final String INTENT_KEY_CONTACTS_GROUP_PRESELECTED_GROUP_LIST = "CONTACTS_GROUP_PRESELECTED_GROUP_LIST";
    public static final String INTENT_KEY_CONTACTS_GROUP_PRESELECTED_MEMBER_LIST = "CONTACTS_GROUP_PRESELECTED_MEMBER_LIST";
    public static final String INTENT_KEY_CONTACTS_GROUP_SELECTED_LIST = "CONTACTS_GROUP_SELECTED_LIST";
    public static final String INTENT_KEY_CONTACTS_GROUP_ID = "CONTACTS_GROUP_ID";
    public static final String INTENT_KEY_CONTACTS_QOE = "QOE";
    public static final String INTENT_KEY_SESSION_KEY = "SESSION_KEY";

    public static final String INTENT_KEY_CALL_LOG_IS_SELECTABLE_MODE = "CALL_LOG_IS_SELECTABLE_MODE";
    public static final String INTENT_KEY_CALL_LOG_ID = "CALL_LOG_ID";
    public static final String INTENT_KEY_CALL_LOG_IS_OEM_CALL = "CALL_LOG_IS_OEM_CALL";

    public static final int INTENT_REQUEST_MAIN = 1000;
    public static final int INTENT_REQUEST_CALL_DIALER = 1001;
    public static final int INTENT_REQUEST_CONTACTS = 1002;
    public static final int INTENT_REQUEST_SLELECTABLE_GROUPS = 1003;
    public static final int INTENT_REQUEST_SLELECTABLE_MEMBERS = 1004;
    public static final int INTENT_REQUEST_CONTACTS_DETAIL = 1005;
    public static final int INTENT_REQUEST_CONTACTS_EDIT = 1006;
    public static final int INTENT_REQUEST_CALL_LOG = 1007;
    public static final int INTENT_REQUEST_CALL_LOG_DETAIL_EVENT = 1008;
    public static final int INTENT_REQUEST_MESSAGE = 1009;
    public static final int INTENT_REQUEST_MESSAGE_SEARCH = 1010;
    public static final int INTENT_REQUEST_OVERLAY_MESSAGE_WTITER = 1011;
    public static final int INTENT_REQUEST_FILE_BOX = 1012;
    public static final int INTENT_REQUEST_MEDIA_SEARCH = 1013;
    public static final int INTENT_REQUEST_SETTINGS = 1014;
    public static final int INTENT_REQUEST_SELECTABLE_CONTACTS_FOR_ADD_SESSION = 1015;
    public static final int INTENT_REQUEST_SELECTABLE_CONTACTS_FOR_ADD_MEMBER = 1016;
    public static final int INTENT_REQUEST_SELECTABLE_CONTACTS_FOR_WRITE_MESSAGE = 1017;

    public static final int CONTACTS_SELECTABLE_BUTTON_TYPE_DONE_RETURN_GROUP_ID_ALSO = 101; // Return only Group ID if user selected one group.
    public static final int CONTACTS_SELECTABLE_BUTTON_TYPE_DONE_RETURN_MEMBER_ID_ONLY = 102; // Return just Member ID list.
    public static final int CONTACTS_SELECTABLE_BUTTON_TYPE_OPTION = 103;
}
