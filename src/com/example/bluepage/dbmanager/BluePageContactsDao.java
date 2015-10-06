package com.example.bluepage.dbmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.content.Loader;

import com.example.bluepage.BluePageConfig;
import com.example.bluepage.model.BluePageContactsModel;
import com.example.bluepage.utils.Log;
import com.example.bluepage.utils.UtilHangul;

public class BluePageContactsDao {

    public static final int TYPE_UNSELECTED = 0;
    public static final int TYPE_PTT = 1;
    public static final int TYPE_PHONE = 2;
    public static final int TYPE_EMAIL = 3;
    public static final int TYPE_WEBSITE = 4;
    public static final int TYPE_COMPANY_NAME = 5;
    public static final int TYPE_COMPANY_DEPARTMENT = 6;
    public static final int TYPE_COMPANY_TITLE = 7;
    public static final int TYPE_OTHER = 8;

    public static final String ID = "_id";
    public static final String CONTACT_ID = "contactId";
    public static final String LOOKUP_KEY = "lookupKey";
    public static final String DISPLAY_NAME = "displayName";
    public static final String FAMILY_NAME = "famileyName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String GIVEN_NAME = "givenName";
    public static final String NICK_NAME = "nickName";
    public static final String PHONE_MOBILE1 = "phoneMobile1";
    public static final String PHONE_MOBILE2 = "phoneMobile2";
    public static final String PHONE_MOBILE3 = "phoneMobile3";
    public static final String PHONE_HOME1 = "phoneHome1";
    public static final String PHONE_HOME2 = "phoneHome2";
    public static final String PHONE_WORK1 = "phoneWork1";
    public static final String PHONE_WORK2 = "phoneWork2";
    public static final String FAX_HOME = "faxHome";
    public static final String FAX_WORK = "faxWork";
    public static final String PAGER = "pager";
    public static final String OTHER_PHONE1 = "otherPhone1";
    public static final String OTHER_PHONE2 = "otherPhone2";
    public static final String CUSTOM_PHONE = "customPhone";
    public static final String CUSTOM_PHONE_TYPE = "customPhoneType";
    public static final String HOME_EMAIL = "homeEmail";
    public static final String WORK_EMAIL = "workEmail";
    public static final String OTHER_EMAIL = "otherEmail";
    public static final String CUSTOM_EMAIL = "customEmail";
    public static final String CUSTOM_EMAIL_TYPE = "customEmailType";
    public static final String HOME_ADDRESS = "homeAddress";
    public static final String WORK_ADDRESS = "workAddress";
    public static final String OTHER_ADDRESS = "otherAddress";
    public static final String CUSTOM_ADDRESS = "customAddress";
    public static final String CUSTOM_ADDRESS_TYPE = "customAddressType";
    public static final String BIRTHDAY_EVENT = "birthdayEvent";
    public static final String ANNIVERSARY_EVENT = "anniversaryEvent";
    public static final String OTHER_EVENT = "otherEvent";
    public static final String CUSTOM_EVENT = "customEvent";
    public static final String CUSTOM_EVENT_TYPE = "customEventType";
    public static final String COMPANY_NAME = "companyName";
    public static final String COMPANY_TITLE = "companyTitle";
    public static final String COMPANY_DEPARTMENT = "companyDepartment";
    public static final String WEB_SITE = "website";
    public static final String MESSENGER = "messenger";
    public static final String NOTE = "note";
    public static final String RELATION = "relation";
    public static final String PHOTO_URI = "photoUri";

    public static final String PTT_NUMBER = "pttNumber";
    public static final String PTT_PHOTO_URI = "pttPhotoUri";
    public static final String PTT_PRESENCE = "pttPresence";
    public static final String PTT_FAVORITE = "favorite";
    public static final String PTT_ADHOC_MEMBER = "adhocMember";
    public static final String PTT_UPDATED = "updated";

    public static final String SEARCH_KEY = "searchKey";
    public static final String LIST_LABEL = "listLabel";

    public static final Uri CONTENT_URI = Uri.parse("content://" + BluePageContactsDao.class.getName());
    public static final String TABLE = "CONTACTS_MEMBERS";
    final ConcurrentHashMap<String, BluePageContactsModel> mModelMap = new ConcurrentHashMap<String, BluePageContactsModel>();
    protected Handler mCallbackHandler = new Handler(Looper.getMainLooper());
    private static BluePageContactsDao sInstance;
    private final Context mAppContext;

    public interface ContactsDaoCallBack<T> {
        void onCompleted(T data);

        void onFailure(String data);
    }

    static void onDbCreate(SQLiteDatabase db) throws SQLException {
        // @formatter:on
        String sql = "CREATE TABLE " + TABLE + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CONTACT_ID + " TEXT,"
            + LOOKUP_KEY + " TEXT,"
            + DISPLAY_NAME + " TEXT,"
            + FAMILY_NAME + " TEXT,"
            + MIDDLE_NAME + " TEXT,"
            + GIVEN_NAME + " TEXT,"
            + NICK_NAME + " TEXT,"
            + PHONE_MOBILE1 + " TEXT,"
            + PHONE_MOBILE2 + " TEXT,"
            + PHONE_MOBILE3 + " TEXT,"
            + PHONE_HOME1 + " TEXT,"
            + PHONE_HOME2 + " TEXT,"
            + PHONE_WORK1 + " TEXT,"
            + PHONE_WORK2 + " TEXT,"
            + FAX_HOME + " TEXT,"
            + FAX_WORK + " TEXT,"
            + PAGER + " TEXT,"
            + OTHER_PHONE1 + " TEXT,"
            + OTHER_PHONE2 + " TEXT,"
            + CUSTOM_PHONE + " TEXT,"
            + CUSTOM_PHONE_TYPE + " TEXT,"
            + HOME_EMAIL + " TEXT,"
            + WORK_EMAIL + " TEXT,"
            + OTHER_EMAIL + " TEXT,"
            + CUSTOM_EMAIL + " TEXT,"
            + CUSTOM_EMAIL_TYPE + " TEXT,"
            + HOME_ADDRESS + " TEXT,"
            + WORK_ADDRESS + " TEXT,"
            + OTHER_ADDRESS + " TEXT,"
            + CUSTOM_ADDRESS + " TEXT,"
            + CUSTOM_ADDRESS_TYPE + " TEXT,"
            + BIRTHDAY_EVENT + " TEXT,"
            + ANNIVERSARY_EVENT + " TEXT,"
            + OTHER_EVENT + " TEXT,"
            + CUSTOM_EVENT + " TEXT,"
            + CUSTOM_EVENT_TYPE + " TEXT,"
            + COMPANY_NAME + " TEXT,"
            + COMPANY_TITLE + " TEXT,"
            + COMPANY_DEPARTMENT + " TEXT,"
            + WEB_SITE + " TEXT,"
            + MESSENGER + " TEXT,"
            + NOTE + " TEXT,"
            + RELATION + " TEXT,"
            + PHOTO_URI + " TEXT,"

            + PTT_NUMBER + " TEXT,"
            + PTT_PHOTO_URI + " TEXT,"
            + PTT_PRESENCE + " TEXT,"
            + PTT_FAVORITE + " INTEGER DEFAULT 0,"
            + PTT_ADHOC_MEMBER + " INTEGER DEFAULT 0,"
            + PTT_UPDATED + " INTEGER DEFAULT 0,"

            + SEARCH_KEY + " TEXT,"
            + LIST_LABEL + " TEXT);";
        db.execSQL(sql);
    }

    public static synchronized BluePageContactsDao getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BluePageContactsDao(context);
        }
        return sInstance;
    }

    public BluePageContactsDao(Context context) {
        mAppContext = context.getApplicationContext();
    }

    private BluePageContactsModel getModelInstance(String key) {
        if (key != null) {
            return mModelMap.get(key);
        }

        return null;
    }

    private void putModelInstance(BluePageContactsModel model) {
        mModelMap.put(model.getTypicalId(), model);
    }

    private void removeModelInstance(String key) {
        if (key != null) {
            mModelMap.remove(key);
        }
    }

    public Loader<ArrayList<BluePageContactsModel>> getAllDataLoader() {
        return new BluePageContactsModelLoader<ArrayList<BluePageContactsModel>>(mAppContext, CONTENT_URI) {
            @Override
            public ArrayList<BluePageContactsModel> loadInBackground() {
                return loadAll();
            }
        };
    }

    public Loader<ArrayList<BluePageContactsModel>> getOemContactsLoader() {
        return new BluePageContactsModelLoader<ArrayList<BluePageContactsModel>>(mAppContext, CONTENT_URI) {
            @Override
            public ArrayList<BluePageContactsModel> loadInBackground() {
                return loadAllOemContactsOnly();
            }
        };
    }

    public Loader<ArrayList<BluePageContactsModel>> getPttMembersLoader() {
        return new BluePageContactsModelLoader<ArrayList<BluePageContactsModel>>(mAppContext, CONTENT_URI) {
            @Override
            public ArrayList<BluePageContactsModel> loadInBackground() {
                return loadAllPttMembersOnly();
            }
        };
    }

    public synchronized ArrayList<BluePageContactsModel> loadAll() {
        ArrayList<BluePageContactsModel> result = new ArrayList<BluePageContactsModel>();
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {
            try {
                db.beginTransaction();
                Cursor cursor = db.query(TABLE, null, null, null, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst() == true) {
                        do {
                            BluePageContactsModel model = fromCursor(cursor);

                            if (model != null) {
                                result.add(model);
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        return result;
    }

    public synchronized ArrayList<BluePageContactsModel> loadAllOemContactsOnly() {
        ArrayList<BluePageContactsModel> result = new ArrayList<BluePageContactsModel>();
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {
            try {
                final String where = "COALESCE (" + LOOKUP_KEY + ", '') <> ''";

                db.beginTransaction();
                Cursor cursor = db.query(TABLE, null, where, null, null, null,
                    DISPLAY_NAME + " COLLATE LOCALIZED ASC");
                if (cursor != null) {
                    if (cursor.moveToFirst() == true) {
                        do {
                            BluePageContactsModel model = fromCursor(cursor);
                            if (model != null) {
                                result.add(model);
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        return result;
    }

    public synchronized ArrayList<BluePageContactsModel> loadAllPttMembersOnly() {
        ArrayList<BluePageContactsModel> result = new ArrayList<BluePageContactsModel>();
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {
            try {
                final String where = "COALESCE (" + PTT_NUMBER + ", '') <> ''";

                db.beginTransaction();
                Cursor cursor = db.query(TABLE, null, where, null, null, null,
                    DISPLAY_NAME + " COLLATE LOCALIZED ASC");

                if (cursor != null) {
                    if (cursor.moveToFirst() == true) {
                        do {
                            BluePageContactsModel model = fromCursor(cursor);
                            if (model != null) {
                                result.add(model);
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        return result;
    }

    public synchronized ArrayList<BluePageContactsModel> loadAllPttAdhocMembersOnly() {
        ArrayList<BluePageContactsModel> result = new ArrayList<BluePageContactsModel>();
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {
            try {
                final String where = PTT_ADHOC_MEMBER + "=?";
                final String[] whereArgs = { String.valueOf(1) };

                db.beginTransaction();
                Cursor cursor = db.query(TABLE, null, where, whereArgs, null, null,
                    DISPLAY_NAME + " COLLATE LOCALIZED ASC");

                if (cursor != null) {
                    if (cursor.moveToFirst() == true) {
                        do {
                            BluePageContactsModel model = fromCursor(cursor);
                            if (model != null) {
                                result.add(model);
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                db.endTransaction();
            }
        }

        return result;
    }

    public void saveOneContactsMember(BluePageContactsModel model) {
        if (model == null) {
            return;
        }

        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                saveOneSafe(db, model);
            } finally {
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
            }
        }
    }

    public void saveContactsMembers(ArrayList<BluePageContactsModel> list, final ContactsDaoCallBack<Void> callback) {
        if (list == null) {
            return;
        }

        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                db.beginTransaction();
                for (BluePageContactsModel contact : list) {
                    saveOneSafe(db, contact);
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                notifyCallbackFailure(callback, e.getMessage());
                Log.e("", e.getMessage(), e);
            }finally {
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                db.endTransaction();
                notifyCallbackCompleted(callback, null);
            }
        }
    }

    private void saveOneSafe(SQLiteDatabase db, BluePageContactsModel contactModel) {
        if (contactModel == null) {
            return;
        }

        BluePageContactsModel model = null;
        ContentValues values = new ContentValues();
        String where = null;
        String[] whereArgs = null;

        if (StringUtils.isNotEmpty(contactModel.getLookupKey())) {
            if (StringUtils.isNotEmpty(contactModel.getPttNumber())) {
                model = loadOneByPttNumber(contactModel.getPttNumber(), false);
                where = PTT_NUMBER + "=?";
                whereArgs = new String[] { contactModel.getPttNumber() };

                if (StringUtils.isEmpty(contactModel.getDisplayName())) {
                    contactModel.setDisplayName(contactModel.getPttNumber());
                }
            } else {
                model = loadOneByLookupKey(contactModel.getLookupKey(), false);
                where = LOOKUP_KEY + "=?";
                whereArgs = new String[] { contactModel.getLookupKey() };

                if (StringUtils.isEmpty(contactModel.getDisplayName())) {
                    contactModel.setDisplayName(contactModel.getPhonePrimary());
                }
            }
        } else if (StringUtils.isNotEmpty(contactModel.getPttNumber())) {
            if (contactModel.getId() > 0) {
                model = loadOneById(contactModel.getId());
                where = ID + "=?";
                whereArgs = new String[] { String.valueOf(contactModel.getId()) };
            } else {
                model = loadOneByPttNumber(contactModel.getPttNumber(), false);
                where = PTT_NUMBER + "=?";
                whereArgs = new String[] { contactModel.getPttNumber() };
            }

            if (StringUtils.isEmpty(contactModel.getDisplayName())) {
                contactModel.setDisplayName(contactModel.getPttNumber());
            }
        } else {
            return;
        }

        if (model != null) {
            values.put(ID, model.getId());
        }
        values.put(CONTACT_ID, contactModel.getContactId());
        values.put(LOOKUP_KEY, contactModel.getLookupKey());
        values.put(DISPLAY_NAME, contactModel.getDisplayName());
        values.put(FAMILY_NAME, contactModel.getFamilyName());
        values.put(MIDDLE_NAME, contactModel.getMiddleName());
        values.put(GIVEN_NAME, contactModel.getGivenName());
        values.put(NICK_NAME, contactModel.getNickName());
        values.put(PHONE_MOBILE1, contactModel.getPhoneMobile1());
        values.put(PHONE_MOBILE2, contactModel.getPhoneMobile2());
        values.put(PHONE_MOBILE3, contactModel.getPhoneMobile3());
        values.put(PHONE_HOME1, contactModel.getPhoneHome1());
        values.put(PHONE_HOME2, contactModel.getPhoneHome2());
        values.put(PHONE_WORK1, contactModel.getPhoneWork1());
        values.put(PHONE_WORK2, contactModel.getPhoneWork2());
        values.put(FAX_HOME, contactModel.getFaxHome());
        values.put(FAX_WORK, contactModel.getFaxWork());
        values.put(PAGER, contactModel.getPager());
        values.put(OTHER_PHONE1, contactModel.getOtherPhone1());
        values.put(OTHER_PHONE2, contactModel.getOtherPhone2());
        values.put(CUSTOM_PHONE, contactModel.getCustomPhone());
        values.put(CUSTOM_PHONE_TYPE, contactModel.getCustomPhoneType());
        values.put(HOME_EMAIL, contactModel.getHomeEmail());
        values.put(WORK_EMAIL, contactModel.getWorkEmail());
        values.put(OTHER_EMAIL, contactModel.getOtherEmail());
        values.put(CUSTOM_EMAIL, contactModel.getCustomEmail());
        values.put(CUSTOM_EMAIL_TYPE, contactModel.getCustomEmailType());
        values.put(HOME_ADDRESS, contactModel.getHomeAddress());
        values.put(WORK_ADDRESS, contactModel.getWorkAddress());
        values.put(OTHER_ADDRESS, contactModel.getOtherAddress());
        values.put(CUSTOM_ADDRESS, contactModel.getCustomAddress());
        values.put(CUSTOM_ADDRESS_TYPE, contactModel.getCustomAddressType());
        values.put(BIRTHDAY_EVENT, contactModel.getBirthdayEvent());
        values.put(ANNIVERSARY_EVENT, contactModel.getAnniversaryEvent());
        values.put(OTHER_EVENT, contactModel.getOtherEvent());
        values.put(CUSTOM_EVENT, contactModel.getCustomEvent());
        values.put(CUSTOM_EVENT_TYPE, contactModel.getCustomEventType());
        values.put(COMPANY_NAME, contactModel.getCompanyName());
        values.put(COMPANY_TITLE, contactModel.getCompanyTitle());
        values.put(COMPANY_DEPARTMENT, contactModel.getCompanyDepartment());
        values.put(WEB_SITE, contactModel.getWebsite());
        values.put(MESSENGER, contactModel.getMessenger());
        values.put(NOTE, contactModel.getNote());
        values.put(RELATION, contactModel.getRelation());
        values.put(PHOTO_URI, contactModel.getPhotoUri());

        values.put(PTT_NUMBER, contactModel.getPttNumber());
        values.put(PTT_PHOTO_URI, contactModel.getPttPhotoUri());
        values.put(PTT_PRESENCE, contactModel.getPttPresence());
        values.put(PTT_FAVORITE, contactModel.isFavorite() == true ? 1 : 0);
        values.put(PTT_ADHOC_MEMBER, contactModel.isAdhocMember() == true ? 1 : 0);
        values.put(PTT_UPDATED, 1);

        values.put(SEARCH_KEY, UtilHangul.getHangulInitialSound(contactModel.getDisplayName()));
        values.put(LIST_LABEL, UtilHangul.getHangulInitialSound(contactModel.getDisplayName().substring(0, 1)));

        int updatedCount = db.update(TABLE, values, where, whereArgs);
        if (updatedCount == 0) {
            db.insert(TABLE, null, values);
        }

        if (model != null) {
            removeModelInstance(getTypicalMemberId(model));
        } else {
            removeModelInstance(getTypicalMemberId(contactModel));
        }
    }

    public BluePageContactsModel loadOneById(int _id) {
        BluePageContactsModel model = null;
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {
            try {
                final String where = ID + "=?";
                final String[] whereArgs = { String.valueOf(_id) };

                db.beginTransaction();
                Cursor cursor = db.query(TABLE, null, where, whereArgs, null, null, null, "1");
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        model = fromCursor(cursor);
                    }
                    cursor.close();
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                db.endTransaction();
            }
        }

        return model;
    }

    public ArrayList<BluePageContactsModel> loadByIds(String _ids) {
        ArrayList<BluePageContactsModel> result = new ArrayList<BluePageContactsModel>();
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();
        ArrayList<String> idList = null;

        if (db != null) {
            if (_ids != null) {
                String[] array = StringUtils.splitByWholeSeparatorPreserveAllTokens(_ids, ";");
                if (array != null) {
                    idList = new ArrayList<String>(Arrays.asList(array));
                }
            }

            try {

                db.beginTransaction();
                if (idList != null) {
                    for (String _id : idList) {
                        BluePageContactsModel model = null;

                        if (model == null) {
                            final String where = ID + "=?";
                            final String[] whereArgs = { _id };

                            Cursor cursor = db.query(TABLE, null, where, whereArgs, null, null, null, "1");
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    model = fromCursor(cursor);
                                }
                                cursor.close();
                            }
                        }

                        if (model != null) {
                            result.add(model);
                        }
                    }
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        return result;
    }

    public BluePageContactsModel loadOneByContactId(String contactId, boolean bClone) {

        BluePageContactsModel model = null;
        if (!bClone) {
            model = getModelInstance(contactId);
            if (model != null) {
                return model;
            }
        }

        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {
            try {
                final String where = CONTACT_ID + "=?";
                final String[] whereArgs = { contactId };

                db.beginTransaction();
                Cursor cursor = db.query(TABLE, null, where, whereArgs, null, null, null, "1");
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        model = fromCursor(cursor);
                    }
                    cursor.close();
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                db.endTransaction();
            }
        }

        return model;
    }

    public BluePageContactsModel loadOneByLookupKey(String lookupKey, boolean bClone) {

        BluePageContactsModel model = null;
        if (!bClone) {
            model = getModelInstance(lookupKey);
            if (model != null) {
                return model;
            }
        }

        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {
            try {
                final String where = LOOKUP_KEY + "=?";
                final String[] whereArgs = { lookupKey };

                db.beginTransaction();
                Cursor cursor = db.query(TABLE, null, where, whereArgs, null, null, null, "1");
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        model = fromCursor(cursor);
                    }
                    cursor.close();
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                db.endTransaction();
            }
        }

        return model;
    }

    public ArrayList<BluePageContactsModel> loadByLookupKeys(String lookupKeys, boolean bClone) {
        ArrayList<BluePageContactsModel> result = new ArrayList<BluePageContactsModel>();
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();
        ArrayList<String> lookupKeyList = null;

        if (db != null) {
            if (lookupKeys != null) {
                String[] array = StringUtils.splitByWholeSeparatorPreserveAllTokens(lookupKeys, ";");
                if (array != null) {
                    lookupKeyList = new ArrayList<String>(Arrays.asList(array));
                }
            }

            try {
                db.beginTransaction();

                if (lookupKeyList != null) {
                    for (String lookupKey : lookupKeyList) {
                        BluePageContactsModel model = null;

                        if (!bClone) {
                            model = getModelInstance(lookupKey);
                        }

                        if (model == null) {
                            final String where = LOOKUP_KEY + "=?";
                            final String[] whereArgs = { lookupKey };

                            Cursor cursor = db.query(TABLE, null, where, whereArgs, null, null, null, "1");
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    model = fromCursor(cursor);
                                }
                                cursor.close();
                            }
                        }

                        if (model != null) {
                            result.add(model);
                        }
                    }
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        return result;
    }

    public BluePageContactsModel loadOneByPttNumber(String pttNumber, boolean bClone) {
        BluePageContactsModel model = null;

        if (!bClone) {
            model = getModelInstance(pttNumber);
            if (model != null) {
                return model;
            }
        }

        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {
            try {
                final String where = PTT_NUMBER + "=?";
                final String[] whereArgs = { pttNumber };

                db.beginTransaction();
                Cursor cursor = db.query(TABLE, null, where, whereArgs, null, null, null, "1");
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        model = fromCursor(cursor);
                    }
                    cursor.close();
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                db.endTransaction();
            }
        }

        return model;
    }

    public ArrayList<BluePageContactsModel> loadByPttNumbers(String pttNumbers, boolean bClone) {
        ArrayList<BluePageContactsModel> result = new ArrayList<BluePageContactsModel>();
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();
        ArrayList<String> pttNumberList = null;

        if (db != null) {
            if (pttNumbers != null) {
                String[] array = StringUtils.splitByWholeSeparatorPreserveAllTokens(pttNumbers, ";");
                if (array != null) {
                    pttNumberList = new ArrayList<String>(Arrays.asList(array));
                }
            }

            try {

                db.beginTransaction();
                if (pttNumberList != null) {
                    for (String pttNumber : pttNumberList) {
                        BluePageContactsModel model = null;

                        if (!bClone) {
                            model = getModelInstance(pttNumber);
                        }

                        if (model == null) {
                            final String where = PTT_NUMBER + "=?";
                            final String[] whereArgs = { pttNumber };

                            Cursor cursor = db.query(TABLE, null, where, whereArgs, null, null, null, "1");
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    model = fromCursor(cursor);
                                }
                                cursor.close();
                            }
                        }

                        if (model != null) {
                            result.add(model);
                        }
                    }
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        return result;
    }


    public void deleteOneById(int _id, final ContactsDaoCallBack<Void> callback) {
        int deletedCount = 0;

        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();
        BluePageContactsModel memberModel = loadOneById(_id);

        if (db != null) {
            try {
                final String where = ID + "=?";
                final String[] whereArgs = { String.valueOf(_id) };

                db.beginTransaction();
                deletedCount = db.delete(TABLE, where, whereArgs);
                if (deletedCount == 1) {
                    if (memberModel != null) {
                        removeModelInstance(memberModel.getTypicalId());
                    }
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                notifyCallbackCompleted(callback, null);
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                db.endTransaction();
            }
        }
    }

    public void deleteOneByLookupKey(String lookupKey, final ContactsDaoCallBack<Void> callback) {
        int deletedCount = 0;

        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                final String where = LOOKUP_KEY + "=?";
                final String[] whereArgs = { lookupKey };

                db.beginTransaction();
                deletedCount = db.delete(TABLE, where, whereArgs);
                if (deletedCount == 1) {
                    removeModelInstance(lookupKey);
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                notifyCallbackCompleted(callback, null);
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                db.endTransaction();
            }
        }
    }

    public void deleteByLookupKeyList(ArrayList<String> lookupKeyList, final ContactsDaoCallBack<Void> callback) {
        int deletedCount = 0;

        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                final String where = LOOKUP_KEY + "=?";

                db.beginTransaction();
                for (String lookupKey : lookupKeyList) {
                    final String[] whereArgs = { lookupKey };
                    deletedCount = db.delete(TABLE, where, whereArgs);
                    if (deletedCount == 1) {
                        removeModelInstance(lookupKey);
                    }
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                notifyCallbackCompleted(callback, null);
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                db.endTransaction();
            }
        }
    }

    public void deleteOneByPttNumber(String pttNumber, final ContactsDaoCallBack<Void> callback) {
        int deletedCount = 0;

        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                final String where = PTT_NUMBER + "=?";
                final String[] whereArgs = { pttNumber };

                db.beginTransaction();
                deletedCount = db.delete(TABLE, where, whereArgs);
                if (deletedCount == 1) {
                    removeModelInstance(pttNumber);
                }

                db.setTransactionSuccessful();

            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                notifyCallbackCompleted(callback, null);
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                db.endTransaction();
            }
        }
    }

    public void deleteByPttNumberList(ArrayList<String> pttNumberList, final ContactsDaoCallBack<Void> callback) {
        int deletedCount = 0;

        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                final String where = PTT_NUMBER + "=?";

                db.beginTransaction();
                for (String lookupKey : pttNumberList) {
                    final String[] whereArgs = { lookupKey };
                    deletedCount = db.delete(TABLE, where, whereArgs);
                    if (deletedCount == 1) {
                        removeModelInstance(lookupKey);
                    }
                }

                db.setTransactionSuccessful();

            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                notifyCallbackCompleted(callback, null);
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                db.endTransaction();
            }
        }
    }

    public void resetAllMembersUpdated() {
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();
        ContentValues values = new ContentValues();

        if (db != null) {
            try {
                db.beginTransaction();
                values.put(PTT_UPDATED, 0);
                db.update(TABLE, values, null, null);
                db.setTransactionSuccessful();
            } finally {
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                db.endTransaction();
            }
        }

        for (String groupId : mModelMap.keySet()) {
            BluePageContactsModel model = getModelInstance(groupId);
            model.setUpdated(0);
            putModelInstance(model);
        }
    }

    private BluePageContactsModel fromCursor(Cursor cursor) {
        BluePageContactsModel model = new BluePageContactsModel();
        String phonePrimary = "", typicalId = "";

        model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
        model.setContactId(cursor.getString(cursor.getColumnIndex(CONTACT_ID)));
        model.setLookupKey(cursor.getString(cursor.getColumnIndex(LOOKUP_KEY)));
        model.setDisplayName(cursor.getString(cursor.getColumnIndex(DISPLAY_NAME)));
        model.setFamilyName(cursor.getString(cursor.getColumnIndex(FAMILY_NAME)));
        model.setMiddleName(cursor.getString(cursor.getColumnIndex(MIDDLE_NAME)));
        model.setGivenName(cursor.getString(cursor.getColumnIndex(GIVEN_NAME)));
        model.setNickName(cursor.getString(cursor.getColumnIndex(NICK_NAME)));
        model.setPhoneMobile1(cursor.getString(cursor.getColumnIndex(PHONE_MOBILE1)));
        model.setPhoneMobile2(cursor.getString(cursor.getColumnIndex(PHONE_MOBILE2)));
        model.setPhoneMobile3(cursor.getString(cursor.getColumnIndex(PHONE_MOBILE3)));
        model.setPhoneHome1(cursor.getString(cursor.getColumnIndex(PHONE_HOME1)));
        model.setPhoneHome2(cursor.getString(cursor.getColumnIndex(PHONE_HOME2)));
        model.setPhoneWork1(cursor.getString(cursor.getColumnIndex(PHONE_WORK1)));
        model.setPhoneWork2(cursor.getString(cursor.getColumnIndex(PHONE_WORK2)));
        model.setFaxHome(cursor.getString(cursor.getColumnIndex(FAX_HOME)));
        model.setFaxWork(cursor.getString(cursor.getColumnIndex(FAX_WORK)));
        model.setPager(cursor.getString(cursor.getColumnIndex(PAGER)));
        model.setOtherPhone1(cursor.getString(cursor.getColumnIndex(OTHER_PHONE1)));
        model.setOtherPhone2(cursor.getString(cursor.getColumnIndex(OTHER_PHONE2)));
        model.setCustomPhone(cursor.getString(cursor.getColumnIndex(CUSTOM_PHONE)));
        model.setCustomPhoneType(cursor.getString(cursor.getColumnIndex(CUSTOM_PHONE_TYPE)));
        model.setHomeEmail(cursor.getString(cursor.getColumnIndex(HOME_EMAIL)));
        model.setWorkEmail(cursor.getString(cursor.getColumnIndex(WORK_EMAIL)));
        model.setOtherEmail(cursor.getString(cursor.getColumnIndex(OTHER_EMAIL)));
        model.setCustomEmail(cursor.getString(cursor.getColumnIndex(CUSTOM_EMAIL)));
        model.setCustomEmailType(cursor.getString(cursor.getColumnIndex(CUSTOM_EMAIL_TYPE)));
        model.setHomeAddress(cursor.getString(cursor.getColumnIndex(HOME_ADDRESS)));
        model.setWorkAddress(cursor.getString(cursor.getColumnIndex(WORK_ADDRESS)));
        model.setOtherAddress(cursor.getString(cursor.getColumnIndex(OTHER_ADDRESS)));
        model.setCustomAddress(cursor.getString(cursor.getColumnIndex(CUSTOM_ADDRESS)));
        model.setCustomAddressType(cursor.getString(cursor.getColumnIndex(CUSTOM_ADDRESS_TYPE)));
        model.setBirthdayEvent(cursor.getString(cursor.getColumnIndex(BIRTHDAY_EVENT)));
        model.setAnniversaryEvent(cursor.getString(cursor.getColumnIndex(ANNIVERSARY_EVENT)));
        model.setOtherEvent(cursor.getString(cursor.getColumnIndex(OTHER_EVENT)));
        model.setCustomEvent(cursor.getString(cursor.getColumnIndex(CUSTOM_EVENT)));
        model.setCustomEventType(cursor.getString(cursor.getColumnIndex(CUSTOM_EVENT_TYPE)));
        model.setCompanyName(cursor.getString(cursor.getColumnIndex(COMPANY_NAME)));
        model.setCompanyTitle(cursor.getString(cursor.getColumnIndex(COMPANY_TITLE)));
        model.setCompanyDepartment(cursor.getString(cursor.getColumnIndex(COMPANY_DEPARTMENT)));
        model.setWebsite(cursor.getString(cursor.getColumnIndex(WEB_SITE)));
        model.setMessenger(cursor.getString(cursor.getColumnIndex(MESSENGER)));
        model.setNote(cursor.getString(cursor.getColumnIndex(NOTE)));
        model.setRelation(cursor.getString(cursor.getColumnIndex(RELATION)));
        model.setPhotoUri(cursor.getString(cursor.getColumnIndex(PHOTO_URI)));

        model.setPttNumber(cursor.getString(cursor.getColumnIndex(PTT_NUMBER)));
        model.setPttPhotoUri(cursor.getString(cursor.getColumnIndex(PTT_PHOTO_URI)));
        model.setPttPresence(cursor.getString(cursor.getColumnIndex(PTT_PRESENCE)));

        model.setSearchKey(cursor.getString(cursor.getColumnIndex(SEARCH_KEY)));
        model.setListLabel(cursor.getString(cursor.getColumnIndex(LIST_LABEL)));
        model.setFavorite(cursor.getInt(cursor.getColumnIndex(PTT_FAVORITE)) == 1 ? true : false);
        model.setAdhocMember(cursor.getInt(cursor.getColumnIndex(PTT_ADHOC_MEMBER)) == 1 ? true : false);
        model.setUpdated(cursor.getInt(cursor.getColumnIndex(PTT_UPDATED)));

        phonePrimary = getPrimaryNumber(model);
        model.setPhonePrimary(phonePrimary);

        typicalId = getTypicalMemberId(model);
        model.setTypicalId(typicalId);

        model.setDataList();

        putModelInstance(model);

        return model;
    }

    public void notifyContentObserver() {
        mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
    }

    public void updateToOemContact(BluePageContactsModel newModel) {
        BluePageContactsModel oldModel;
        String contactId = "", lookupKey = newModel.getLookupKey();
        ArrayList<ContentProviderOperation> cpoList = new ArrayList<ContentProviderOperation>();
        Uri lookupUri;
        String[] projection = ArrayUtils.toArray(ContactsContract.Contacts._ID);
        Cursor cursor;



        if (StringUtils.isEmpty(lookupKey)) {
            return;
        }

        lookupUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
        cursor = mAppContext.getContentResolver().query(lookupUri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactId = Long.toString(cursor.getLong(0));
            }
            cursor.close();
        } else {
            return;
        }

        if (StringUtils.isEmpty(contactId)) {
            return;
        }

        oldModel = loadOneByLookupKey(lookupKey, true);
        if (oldModel == null) {
            return;
        }

        // Display Name
        if (!oldModel.getDisplayName().equals(newModel.getDisplayName())) {
            addContactNameCPO(cpoList, contactId, ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                oldModel.getDisplayName(), newModel.getDisplayName());
        }

        // Family Name
        if (!oldModel.getFamilyName().equals(newModel.getFamilyName())) {
            addContactNameCPO(cpoList, contactId, ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                oldModel.getFamilyName(), newModel.getFamilyName());
        }

        // Middle Name
        if (!oldModel.getMiddleName().equals(newModel.getMiddleName())) {
            addContactNameCPO(cpoList, contactId, ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME,
                oldModel.getMiddleName(), newModel.getMiddleName());
        }

        // Given Name
        if (!oldModel.getGivenName().equals(newModel.getGivenName())) {
            addContactNameCPO(cpoList, contactId, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                oldModel.getGivenName(), newModel.getGivenName());
        }

        // Phone Mobile1
        if (!oldModel.getPhoneMobile1().replaceAll("-", "").equals(newModel.getPhoneMobile1().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                oldModel.getPhoneMobile1(), newModel.getPhoneMobile1());
        }

        // Phone Mobile2
        if (!oldModel.getPhoneMobile2().replaceAll("-", "").equals(newModel.getPhoneMobile2().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                oldModel.getPhoneMobile2(), newModel.getPhoneMobile2());
        }

        // Phone Mobile3
        if (!oldModel.getPhoneMobile3().replaceAll("-", "").equals(newModel.getPhoneMobile3().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                oldModel.getPhoneMobile3(), newModel.getPhoneMobile3());
        }

        // Phone Home1
        if (!oldModel.getPhoneHome1().replaceAll("-", "").equals(newModel.getPhoneHome1().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_HOME,
                oldModel.getPhoneHome1(), newModel.getPhoneHome1());
        }

        // Phone Home2
        if (!oldModel.getPhoneHome2().replaceAll("-", "").equals(newModel.getPhoneHome2().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_HOME,
                oldModel.getPhoneHome2(), newModel.getPhoneHome2());
        }

        // Phone Work1
        if (!oldModel.getPhoneWork1().replaceAll("-", "").equals(newModel.getPhoneWork1().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_WORK,
                oldModel.getPhoneWork1(), newModel.getPhoneWork1());
        }

        // Phone Work2
        if (!oldModel.getPhoneWork2().replaceAll("-", "").equals(newModel.getPhoneWork2().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_WORK,
                oldModel.getPhoneWork2(), newModel.getPhoneWork2());
        }

        // Fax Home
        if (!oldModel.getFaxHome().replaceAll("-", "").equals(newModel.getFaxHome().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME,
                oldModel.getFaxHome(), newModel.getFaxHome());
        }

        // Fax Work
        if (!oldModel.getFaxWork().replaceAll("-", "").equals(newModel.getFaxWork().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK,
                oldModel.getFaxWork(), newModel.getFaxWork());
        }

        // Pager
        if (!oldModel.getPager().replaceAll("-", "").equals(newModel.getPager().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_PAGER,
                oldModel.getPager(), newModel.getPager());
        }

        // Phone Other1
        if (!oldModel.getOtherPhone1().replaceAll("-", "").equals(newModel.getOtherPhone1().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_OTHER,
                oldModel.getOtherPhone1(), newModel.getOtherPhone1());
        }

        // Phone Other2
        if (!oldModel.getOtherPhone2().replaceAll("-", "").equals(newModel.getOtherPhone2().replaceAll("-", ""))) {
            addContactPhoneCPO(cpoList, contactId, ContactsContract.CommonDataKinds.Phone.TYPE_OTHER,
                oldModel.getOtherPhone2(), newModel.getOtherPhone2());
        }

        // Phone Custom
        if (!oldModel.getCustomPhone().replaceAll("-", "").equals(newModel.getCustomPhone().replaceAll("-", ""))) {
            addContactCustomPhoneCPO(cpoList, contactId, oldModel.getCustomPhone(), newModel.getCustomPhone(),
                oldModel.getCustomPhoneType());
        }

        // ///////////////////////////////
        // Finally, Update or Insert
        // ///////////////////////////////
        if (cpoList.size() > 0) {
            try {
                mAppContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cpoList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // You have to delete and retrain this contact because lookupkey can be changed if you edit the name of contact.
                deleteOneByLookupKey(lookupKey, null);
            }
        }
    }

    private ArrayList<Long> getRawContactIdList(String contactId) {
        ArrayList<Long> rawContactIdList = new ArrayList<Long>();
        try {
            Cursor rawContactCursor = mAppContext.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                null,
                ContactsContract.Data.CONTACT_ID + " = ?",
                new String[] { contactId },
                null);

            if (rawContactCursor != null) {
                try {
                    while (rawContactCursor.moveToNext()) {
                        long rawId = rawContactCursor.getLong(rawContactCursor
                            .getColumnIndex(ContactsContract.RawContacts._ID));
                        rawContactIdList.add(rawId);
                    }
                } finally {
                    rawContactCursor.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return rawContactIdList;
    }

    private void addContactNameCPO(ArrayList<ContentProviderOperation> cpoList,
        String contactId, String nameType, String oldName, String newName) {

        ArrayList<Long> rawContactIdList = getRawContactIdList(contactId);

        if (rawContactIdList.size() > 0) {
            long rawContactId = 0, dataId = 0;

            for (long rawId : rawContactIdList) {
                // Make sure the "last checked" RawContactId is set locally for use in insert & update.
                rawContactId = rawId;
                String sel = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND "
                    + ContactsContract.Data.MIMETYPE + " = ? AND "
                    + nameType + " = ? ";
                String[] selArg = new String[] { Long.toString(rawContactId),
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                    oldName };
                Cursor dataCursor = mAppContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,
                    sel, selArg, null);

                if (dataCursor != null) {
                    if (dataCursor.getCount() > 0) {
                        dataCursor.moveToFirst();
                        dataId = dataCursor.getLong(dataCursor.getColumnIndex(ContactsContract.Data._ID));
                        dataCursor.close();
                        break;
                    } else {
                        dataCursor.close();
                    }
                }
            }

            try {
                if (dataId > 0) {
                    // Update
                    cpoList.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = ?",
                            new String[] { Long.toString(rawContactId) })
                            .withSelection(ContactsContract.Data._ID + " = ?", new String[] { Long.toString(dataId) })
                            .withValue(nameType, newName)
                            .build());
                } else {
                    // Insert
                    cpoList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValue(ContactsContract.Data.RAW_CONTACT_ID, Long.toString(rawContactId))
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(nameType, newName)
                            .build());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addContactPhoneCPO(ArrayList<ContentProviderOperation> cpoList,
        String contactId, int phoneType, String oldNumber, String newNumber) {

        ArrayList<Long> rawContactIdList = getRawContactIdList(contactId);

        if (rawContactIdList.size() > 0) {
            long rawContactId = 0, dataId = 0;

            for (long rawId : rawContactIdList) {
                // Make sure the "last checked" RawContactId is set locally for use in insert & update.
                rawContactId = rawId;
                String sel = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND "
                    + ContactsContract.Data.MIMETYPE + " = ? AND "
                    + ContactsContract.CommonDataKinds.Phone.TYPE + " = ? AND "
                    + ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?";
                String[] selArg = new String[] { Long.toString(rawContactId),
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                    String.valueOf(phoneType),
                    oldNumber };
                Cursor dataCursor = mAppContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,
                    sel, selArg, null);

                if (dataCursor != null) {
                    if (dataCursor.getCount() > 0) {
                        dataCursor.moveToFirst();
                        dataId = dataCursor.getLong(dataCursor.getColumnIndex(ContactsContract.Data._ID));
                        dataCursor.close();
                        break;
                    } else {
                        dataCursor.close();
                    }
                }
            }

            try {
                if (dataId > 0) {
                    // Update
                    cpoList.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = ?",
                            new String[] { Long.toString(rawContactId) })
                            .withSelection(ContactsContract.Data._ID + " = ?", new String[] { Long.toString(dataId) })
                            .withSelection(ContactsContract.CommonDataKinds.Phone.TYPE + " = ?",
                                new String[] { String.valueOf(phoneType) })
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newNumber)
                                .build());
                } else {
                    // Insert
                    cpoList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValue(ContactsContract.Data.RAW_CONTACT_ID, Long.toString(rawContactId))
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newNumber)
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, String.valueOf(phoneType))
                            .build());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addContactCustomPhoneCPO(ArrayList<ContentProviderOperation> cpoList,
        String contactId, String oldNumber, String newNumber, String customType) {

        ArrayList<Long> rawContactIdList = getRawContactIdList(contactId);
        long rawContactId = 0, dataId = 0;

        if (rawContactIdList.size() > 0) {
            for (long rawId : rawContactIdList) {
                // Make sure the "last checked" RawContactId is set locally for use in insert & update.
                rawContactId = rawId;
                String sel = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND "
                    + ContactsContract.Data.MIMETYPE + " = ? AND "
                    + ContactsContract.CommonDataKinds.Phone.TYPE + " = ? AND "
                    + ContactsContract.CommonDataKinds.Phone.NUMBER + " = ? AND "
                    + ContactsContract.CommonDataKinds.Phone.LABEL + " = ? ";
                String[] selArg = new String[] { Long.toString(rawContactId),
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                    String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM),
                    oldNumber,
                    customType };
                Cursor dataCursor = mAppContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,
                    sel, selArg, null);

                if (dataCursor != null) {
                    if (dataCursor.getCount() > 0) {
                        dataCursor.moveToFirst();
                        dataId = dataCursor.getLong(dataCursor.getColumnIndex(ContactsContract.Data._ID));
                        dataCursor.close();
                        break;
                    } else {
                        dataCursor.close();
                    }
                }
            }

            try {
                if (dataId > 0) {
                    // Update
                    cpoList.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = ?",
                            new String[] { Long.toString(rawContactId) })
                            .withSelection(ContactsContract.Data._ID + " = ?", new String[] { Long.toString(dataId) })
                            .withSelection(ContactsContract.CommonDataKinds.Phone.TYPE + " = ?",
                                new String[] { String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) })
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newNumber)
                                .build());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doCheckOemContactsUpdated(long lastUpdate, final ContactsDaoCallBack<Void> callback) {
        OemContactsUpdateCheckTask updateChecker = new OemContactsUpdateCheckTask(callback);
        updateChecker.execute(lastUpdate);
    }

    private class OemContactsUpdateCheckTask extends AsyncTask<Long, Void, Void> {
        ContactsDaoCallBack<Void> callback;

        public OemContactsUpdateCheckTask(final ContactsDaoCallBack<Void> cb) {
            callback = cb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Long... params) {
            checkOnlyUpdatedOemContacts(params[0]);
            checkOnlyDeletedOemContacts();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            PreferenceManager.getDefaultSharedPreferences(mAppContext).edit()
                .putLong(BluePageConfig.PREF_KEY_CONTACTS_MEMBERS_UPDATE_TIMESTAMP, System.currentTimeMillis()).apply();
            notifyCallbackCompleted(callback, null);
        }
    }

    @SuppressLint("InlinedApi")
    private void checkOnlyUpdatedOemContacts(long lastUpdated) {
        Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
        ArrayList<BluePageContactsModel> resultList = new ArrayList<BluePageContactsModel>();
        String selection = ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + ">?" + " AND "
            + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=?";

        Cursor contactsCursor = mAppContext.getContentResolver().query(contactsUri, null, selection,
            new String[] { Long.toString(lastUpdated), Integer.toString(1) },
            ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC ");
        if (contactsCursor != null) {
            if (contactsCursor.moveToFirst()) {
                do {
                    BluePageContactsModel model = new BluePageContactsModel();

                    String contactId = Long.toString(contactsCursor.getLong(contactsCursor
                        .getColumnIndex(ContactsContract.Contacts._ID)));
                    String lookupKey = contactsCursor.getString(contactsCursor
                        .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    String displayName = "";
                    String familyName = "";
                    String middleName = "";
                    String givenName = "";
                    String nickName = "";
                    String phoneMobile1 = "";
                    String phoneMobile2 = "";
                    String phoneMobile3 = "";
                    String phoneHome1 = "";
                    String phoneHome2 = "";
                    String phoneWork1 = "";
                    String phoneWork2 = "";
                    String faxWork = "";
                    String faxHome = "";
                    String pager = "";
                    String otherPhone1 = "";
                    String otherPhone2 = "";
                    String customPhone = "";
                    String customPhoneType = "";
                    String homeEmail = "";
                    String workEmail = "";
                    String otherEmail = "";
                    String customEmail = "";
                    String customEmailType = "";
                    String homeAddress = "";
                    String workAddress = "";
                    String otherAddress = "";
                    String customAddress = "";
                    String customAddressType = "";
                    String birthdayEvent = "";
                    String anniversaryEvent = "";
                    String otherEvent = "";
                    String customEvent = "";
                    String customEventType = "";
                    String companyName = "";
                    String companyTitle = "";
                    String companyDepartment = "";
                    String website = "";
                    String messenger = "";
                    String note = "";
                    String relation = "";
                    String photoUri = contactsCursor.getString(contactsCursor
                        .getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                    String phonePrimary = "";
                    String searchKey = "";
                    String listLabel = "";
                    String typicalId = "";
                    String pttNumber = "";

                    Cursor dataCursor = mAppContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,
                        ContactsContract.Data.CONTACT_ID + "=" + contactId, null, null);

                    if (dataCursor != null) {
                        if (dataCursor.moveToFirst()) {
                            // <대표 이름>
                            displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

                            if (StringUtils.isNotEmpty(displayName)) {
                                do {
                                    // <Structured Name>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                                        if (StringUtils.isEmpty(familyName)) {
                                            familyName = dataCursor.getString(dataCursor
                                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                                        }
                                        if (StringUtils.isEmpty(middleName)) {
                                            middleName = dataCursor.getString(dataCursor
                                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                                        }
                                        if (StringUtils.isEmpty(givenName)) {
                                            givenName = dataCursor.getString(dataCursor
                                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                                            if (StringUtils.isEmpty(givenName)) {
                                                givenName = displayName;
                                            }
                                        }

                                        continue;
                                    }

                                    // <별명>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
                                        if (StringUtils.isEmpty(nickName)) {
                                            nickName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                        }

                                        continue;
                                    }

                                    // <통화>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                                        switch (dataCursor.getInt(dataCursor.getColumnIndex(ContactsContract.Data.DATA2))) {
                                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                                if (StringUtils.isEmpty(phoneMobile1)) {
                                                    phoneMobile1 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                } else if (StringUtils.isEmpty(phoneMobile2)) {
                                                    phoneMobile2 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                } else if (StringUtils.isEmpty(phoneMobile3)) {
                                                    phoneMobile3 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                                if (StringUtils.isEmpty(phoneHome1)) {
                                                    phoneHome1 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                } else if (StringUtils.isEmpty(phoneHome2)) {
                                                    phoneHome2 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                                if (StringUtils.isEmpty(phoneWork1)) {
                                                    phoneWork1 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                } else if (StringUtils.isEmpty(phoneWork2)) {
                                                    phoneWork2 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                                                if (StringUtils.isEmpty(faxWork)) {
                                                    faxWork = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                                                if (StringUtils.isEmpty(faxHome)) {
                                                    faxHome = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                                                if (StringUtils.isEmpty(pager)) {
                                                    pager = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                                if (StringUtils.isEmpty(otherPhone1)) {
                                                    otherPhone1 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                } else if (StringUtils.isEmpty(otherPhone2)) {
                                                    otherPhone2 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                                                if (StringUtils.isEmpty(customPhone)) {
                                                    customPhone = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                    customPhoneType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA3));
                                                }
                                                break;
                                        }

                                        continue;
                                    }

                                    // <이메일>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                                        switch (dataCursor.getInt(dataCursor.getColumnIndex(ContactsContract.Data.DATA2))) {
                                            case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                                if (StringUtils.isEmpty(homeEmail)) {
                                                    homeEmail = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                                if (StringUtils.isEmpty(workEmail)) {
                                                    workEmail = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                                                if (StringUtils.isEmpty(otherEmail)) {
                                                    otherEmail = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM:
                                                if (StringUtils.isEmpty(customEmail)) {
                                                    customEmail = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                    customEmailType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA3));
                                                }
                                                break;
                                        }

                                        continue;
                                    }

                                    // <주소>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)) {
                                        switch (dataCursor.getInt(dataCursor.getColumnIndex(ContactsContract.Data.DATA2))) {
                                            case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME:
                                                if (StringUtils.isEmpty(homeAddress)) {
                                                    homeAddress = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK:
                                                if (StringUtils.isEmpty(workAddress)) {
                                                    workAddress = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER:
                                                if (StringUtils.isEmpty(otherAddress)) {
                                                    otherAddress = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM:
                                                if (StringUtils.isEmpty(customAddress)) {
                                                    customAddress = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                    customAddressType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA3));
                                                }
                                                break;

                                        }

                                        continue;
                                    }

                                    // <이벤트>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)) {
                                        switch (dataCursor.getInt(dataCursor.getColumnIndex(ContactsContract.Data.DATA2))) {
                                            case ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY:
                                                if (StringUtils.isEmpty(birthdayEvent)) {
                                                    birthdayEvent = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY:
                                                if (StringUtils.isEmpty(anniversaryEvent)) {
                                                    anniversaryEvent = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Event.TYPE_OTHER:
                                                if (StringUtils.isEmpty(otherEvent)) {
                                                    otherEvent = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                }
                                                break;
                                            case ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM:
                                                if (StringUtils.isEmpty(customEvent)) {
                                                    customEvent = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                                    customEventType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA3));
                                                }
                                                break;

                                        }

                                        continue;
                                    }

                                    // <조직>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                                        if (StringUtils.isEmpty(companyName)) {
                                            companyName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                        }
                                        if (StringUtils.isEmpty(companyTitle)) {
                                            companyTitle = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA4));
                                        }
                                        if (StringUtils.isEmpty(companyDepartment)) {
                                            companyDepartment = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA5));
                                        }

                                        continue;
                                    }

                                    // <홈페이지>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)) {
                                        if (StringUtils.isEmpty(website)) {
                                            website = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                        }

                                        continue;
                                    }

                                    // <메신저>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)) {
                                        if (StringUtils.isEmpty(messenger)) {
                                            messenger = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                        }

                                        continue;
                                    }

                                    // <노트>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)) {
                                        if (StringUtils.isEmpty(note)) {
                                            note = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                        }

                                        continue;
                                    }

                                    // <관계>
                                    if (dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                        .equals(ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE)) {
                                        if (StringUtils.isEmpty(relation)) {
                                            relation = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                                        }

                                        continue;
                                    }

                                } while (dataCursor.moveToNext());

                                // <초성검색>
                                searchKey = UtilHangul.getHangulInitialSound(displayName);

                                // <리스트 레이블>
                                listLabel = UtilHangul.getHangulInitialSound(displayName.substring(0, 1));

                                // 각 필드 데이터 저장
                                model.setContactId(contactId);
                                model.setLookupKey(lookupKey);
                                model.setDisplayName(displayName);
                                model.setFamilyName(familyName);
                                model.setMiddleName(middleName);
                                model.setGivenName(givenName);
                                model.setNickName(nickName);
                                model.setPhoneMobile1(phoneMobile1);
                                model.setPhoneMobile2(phoneMobile2);
                                model.setPhoneMobile3(phoneMobile3);
                                model.setPhoneWork1(phoneWork1);
                                model.setPhoneWork2(phoneWork2);
                                model.setPhoneHome1(phoneHome1);
                                model.setPhoneHome2(phoneHome2);
                                model.setFaxWork(faxWork);
                                model.setFaxHome(faxHome);
                                model.setPager(pager);
                                model.setOtherPhone1(otherPhone1);
                                model.setOtherPhone2(otherPhone2);
                                model.setCustomPhone(customPhone);
                                model.setCustomPhoneType(customPhoneType);
                                model.setHomeEmail(homeEmail);
                                model.setWorkEmail(workEmail);
                                model.setOtherEmail(otherEmail);
                                model.setCustomEmail(customEmail);
                                model.setCustomEmailType(customEmailType);
                                model.setHomeAddress(homeAddress);
                                model.setWorkAddress(workAddress);
                                model.setOtherAddress(otherAddress);
                                model.setCustomAddress(customAddress);
                                model.setCustomAddressType(customAddressType);
                                model.setBirthdayEvent(birthdayEvent);
                                model.setAnniversaryEvent(anniversaryEvent);
                                model.setOtherEvent(otherEvent);
                                model.setCustomEvent(customEvent);
                                model.setCustomEventType(customEventType);
                                model.setCompanyName(companyName);
                                model.setCompanyTitle(companyTitle);
                                model.setCompanyDepartment(companyDepartment);
                                model.setWebsite(website);
                                model.setMessenger(messenger);
                                model.setNote(note);
                                model.setRelation(relation);
                                model.setPhotoUri(photoUri);

                                model.setPttNumber(pttNumber);
                                model.setPttPhotoUri("");
                                model.setPttPresence("");
                                model.setFavorite(false);
                                model.setAdhocMember(StringUtils.isNotEmpty(pttNumber) ? true : false);
                                model.setUpdated(0);

                                model.setSearchKey(searchKey);
                                model.setListLabel(listLabel);

                                phonePrimary = getPrimaryNumber(model);
                                model.setPhonePrimary(phonePrimary);

                                typicalId = getTypicalMemberId(model);
                                model.setTypicalId(typicalId);

                                resultList.add(model);
                            }
                        }
                        dataCursor.close();
                    }
                } while (contactsCursor.moveToNext());
            }
            contactsCursor.close();

            if (resultList.size() > 0) {
                saveContactsMembers(resultList, null);
            }
        }
    }

    private void checkOnlyDeletedOemContacts() {
        HashSet<String> lookupKeySet = new HashSet<String>();
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();
        Cursor localCursor;
        String lookupKey = null;

        if (db != null) {
            try {
                db.beginTransaction();
                localCursor = db.query(TABLE, new String[] { LOOKUP_KEY }, null, null, null, null, null);

                // 1. Get lookupKeys on local contacts DB.
                if (localCursor != null) {
                    if (localCursor.moveToFirst()) {
                        do {
                            lookupKey = localCursor.getString(0);
                            if (StringUtils.isNotEmpty(lookupKey)) {
                                lookupKeySet.add(lookupKey);
                            }
                        } while (localCursor.moveToNext());

                    }
                    localCursor.close();
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

            // 2. Get lookupKeys on OEM contacts DB.
            Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
            Cursor oemContactsCursor = mAppContext.getContentResolver().query(contactsUri,
                new String[] { ContactsContract.Contacts.LOOKUP_KEY }, null, null, null);

            if (oemContactsCursor != null) {
                if (oemContactsCursor.moveToFirst()) {
                    do {
                        lookupKey = oemContactsCursor.getString(0);
                        if (StringUtils.isNotEmpty(lookupKey)) {
                            lookupKeySet.remove(lookupKey);
                        }
                    } while (oemContactsCursor.moveToNext());
                }
                oemContactsCursor.close();
            }

            // 3. Finally, delete it if OEM contact was deleted.
            if (lookupKeySet.size() > 0) {
                deleteByLookupKeyList(new ArrayList<String>(lookupKeySet), null);
            } else {
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
            }
        }
    }

    public void doUpdateToOemContacts(BluePageContactsModel newModel, final ContactsDaoCallBack<Void> callback) {
        UpdateToOemContactsTask oemUpdater = new UpdateToOemContactsTask(callback);
        oemUpdater.execute(newModel);
    }

    private class UpdateToOemContactsTask extends AsyncTask<BluePageContactsModel, Void, Void> {
        ContactsDaoCallBack<Void> callback;

        public UpdateToOemContactsTask(final ContactsDaoCallBack<Void> cb) {
            callback = cb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(BluePageContactsModel... params) {
            updateToOemContact(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            notifyCallbackCompleted(callback, null);
        }
    }

    private String getPrimaryNumber(BluePageContactsModel model) {
        String primaryNumber;

        if (StringUtils.isNotEmpty(model.getPttNumber())) {
            primaryNumber = model.getPttNumber();
        } else if (StringUtils.isNotEmpty(model.getPhoneMobile1())) {
            primaryNumber = model.getPhoneMobile1();
        } else if (StringUtils.isNotEmpty(model.getPhoneMobile2())) {
            primaryNumber = model.getPhoneMobile2();
        } else if (StringUtils.isNotEmpty(model.getPhoneMobile3())) {
            primaryNumber = model.getPhoneMobile3();
        } else if (StringUtils.isNotEmpty(model.getPhoneHome1())) {
            primaryNumber = model.getPhoneHome1();
        } else if (StringUtils.isNotEmpty(model.getPhoneHome2())) {
            primaryNumber = model.getPhoneHome2();
        } else if (StringUtils.isNotEmpty(model.getPhoneWork1())) {
            primaryNumber = model.getPhoneWork1();
        } else if (StringUtils.isNotEmpty(model.getPhoneWork2())) {
            primaryNumber = model.getPhoneWork2();
        } else if (StringUtils.isNotEmpty(model.getOtherPhone1())) {
            primaryNumber = model.getOtherPhone1();
        } else if (StringUtils.isNotEmpty(model.getOtherPhone2())) {
            primaryNumber = model.getOtherPhone2();
        } else if (StringUtils.isNotEmpty(model.getCustomPhone())) {
            primaryNumber = model.getCustomPhone();
        } else if (StringUtils.isNotEmpty(model.getFaxWork())) {
            primaryNumber = model.getFaxWork();
        } else if (StringUtils.isNotEmpty(model.getFaxHome())) {
            primaryNumber = model.getFaxHome();
        } else if (StringUtils.isNotEmpty(model.getPager())) {
            primaryNumber = model.getPager();
        } else {
            primaryNumber = "";
        }

        return primaryNumber;
    }

    private String getTypicalMemberId(BluePageContactsModel model) {
        String typicalId = "";

        if (StringUtils.isNotEmpty(model.getLookupKey())) {
            typicalId = model.getLookupKey();
        }

        if (StringUtils.isNotEmpty(model.getPttNumber())) {
            typicalId = model.getPttNumber();
        }

        return typicalId;
    }

    private <T> void notifyCallbackCompleted(final BluePageContactsDao.ContactsDaoCallBack<T> l, final T data) {
        if (l == null) {
            return;
        }
        mCallbackHandler.post(new Runnable() {
            @Override
            public void run() {
                l.onCompleted(data);
            }
        });
    }

    private void notifyCallbackFailure(final BluePageContactsDao.ContactsDaoCallBack<?> l, final String data) {
        if (l == null) {
            return;
        }
        mCallbackHandler.post(new Runnable() {
            @Override
            public void run() {
                l.onFailure(data);
            }
        });
    }

    /**
     * find members containing search string from ptt_number, display_name
     * @param searchStr
     * @return
     */
    public ArrayList<BluePageContactsModel> search(String searchStr) {
        ArrayList<BluePageContactsModel> result = new ArrayList<BluePageContactsModel>();
        BluePageContactsDBManager dbMgr = BluePageContactsDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {

            db.beginTransaction();
            try {
                StringBuffer strbuf = new StringBuffer();
                strbuf.append("select * from ").append(TABLE).append(" ")
                .append(" where ").append(PTT_NUMBER).append(" like '%").append(searchStr).append("%' ")
                .append(" or ").append(DISPLAY_NAME).append(" like '%").append(searchStr).append("%' ");

                Cursor cursor = db.rawQuery(strbuf.toString(), null);

                if(cursor.moveToFirst() == false) {
                    cursor.close();
                    return result;
                }

                while(!cursor.isAfterLast()) {
                    result.add(fromCursor(cursor));
                    cursor.moveToNext();
                }
                cursor.close();
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        return result;
    }

}
