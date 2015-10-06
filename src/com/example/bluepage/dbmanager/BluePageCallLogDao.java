package com.example.bluepage.dbmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
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
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.content.Loader;

import com.example.bluepage.BluePageConfig;
import com.example.bluepage.R;
import com.example.bluepage.model.BluePageCallLogModel;
import com.example.bluepage.model.BluePageContactsModel;
import com.example.bluepage.utils.Log;

public class BluePageCallLogDao {

    public static final int CAll_TYPE_OEM_INCOMING = CallLog.Calls.INCOMING_TYPE; // 1
    public static final int CAll_TYPE_OEM_OUTGOING = CallLog.Calls.OUTGOING_TYPE; // 2
    public static final int CAll_TYPE_OEM_MISSED = CallLog.Calls.MISSED_TYPE; // 3
    public static final int CAll_TYPE_OEM_VOICEMAIL = CallLog.Calls.VOICEMAIL_TYPE; // 4
    public static final int CAll_TYPE_OEM_OTHER = 10;

    public static final int CAll_TYPE_PTT_INCOMING = 11;
    public static final int CAll_TYPE_PTT_OUTGOING = 12;
    public static final int CAll_TYPE_PTT_MISSED = 13;
    public static final int CAll_TYPE_PTT_OTHER = 14;

    public static final int CAll_KIND_NONE = 0;
    public static final int CAll_KIND_VOICE_PTT = 101;
    public static final int CAll_KIND_VIDEO_PTT = 102;
    public static final int CAll_KIND_VOICE_CALL = 103;
    public static final int CAll_KIND_VIDEO_CALL = 104;
    public static final int CAll_KIND_MESSAGE = 105;
    public static final int CAll_KIND_OEM_VOICE_CALL = 106;
    public static final int CAll_KIND_OEM_VIDEO_CALL = 107;
    public static final int CAll_KIND_OTHER = 110;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OEM_CALL_ID = "call_id"; // OEM Call Log has only.
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_START = "date";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_CALL_TYPE = "call_type";
    public static final String COLUMN_CALL_KIND = "call_kind";
    public static final String COLUMN_COUNTRY_ISO = "countryiso";
    public static final String COLUMN_VOICEMAIL_URI = "voicemail_uri";
    public static final String COLUMN_GEOCODED_LOCATION = "geocoded_location";
    public static final String COLUMN_NUMBER_TYPE = "numbertype";
    public static final String COLUMN_NUMBER_LABEL = "numberlabel";
    public static final String COLUMN_LOOKUP_URI = "lookup_uri";
    public static final String COLUMN_MACHED_NUMBER = "matched_number";
    public static final String COLUMN_NORMALIZED_NUMBER = "normalized_number";
    public static final String COLUMN_PHOTO_ID = "photo_id";
    public static final String COLUMN_FORMATTED_NUMBER = "formatted_number";
    public static final String COLUMN_PRESENTATION = "presentation";
    public static final String COLUMN_CONTACTS_ID = "contacts_id";
    public static final String COLUMN_IS_NEW = "new";
    public static final String COLUMN_IS_PRE_ARGD = "is_pre_argd";
    public static final String COLUMN_SIP_URI = "sip_uri";
    public static final String COLUMN_SERVER_SESSION_ID = "server_session_id";
    public static final String COLUMN_SESSION_ID = "session_id";

    public static final int SORT_BY_DATE = 0;
    public static final int SORT_BY_GROUP = 1;
    public static final int SORT_BY_ABC = 2;

    public static final Uri CONTENT_URI = Uri.parse("content://" + BluePageCallLogDao.class.getName());
    public static final String TABLE = "CALLLOG";
    final ConcurrentHashMap<Integer, BluePageCallLogModel> mModelMap = new ConcurrentHashMap<Integer, BluePageCallLogModel>();
    private static BluePageCallLogDao sInstance;
    private final Context mAppContext;
    protected Handler mCallbackHandler = new Handler(Looper.getMainLooper());
    private int mSortingType = SORT_BY_DATE;

    public interface BluePageCallLogDaoCallBack<T> {
        void onCompleted(T data);

        void onFailure(String data);
    }

    static void onDbCreate(SQLiteDatabase db) throws SQLException {
        // @formatter:on
        String sql = "CREATE TABLE " + TABLE + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_OEM_CALL_ID + " TEXT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_NUMBER + " TEXT,"
            + COLUMN_START + " INTEGER DEFAULT 0,"
            + COLUMN_DURATION + " INTEGER DEFAULT 0,"
            + COLUMN_CALL_TYPE + " INTEGER DEFAULT 0,"
            + COLUMN_CALL_KIND + " INTEGER DEFAULT 0,"
            + COLUMN_COUNTRY_ISO + " TEXT,"
            + COLUMN_VOICEMAIL_URI + " TEXT,"
            + COLUMN_GEOCODED_LOCATION + " TEXT,"
            + COLUMN_NUMBER_TYPE + " TEXT,"
            + COLUMN_NUMBER_LABEL + " TEXT,"
            + COLUMN_LOOKUP_URI + " TEXT,"
            + COLUMN_MACHED_NUMBER + " TEXT,"
            + COLUMN_NORMALIZED_NUMBER + " TEXT,"
            + COLUMN_PHOTO_ID + " TEXT,"
            + COLUMN_FORMATTED_NUMBER + " TEXT,"
            + COLUMN_PRESENTATION + " INTEGER DEFAULT 0,"
            + COLUMN_CONTACTS_ID + " TEXT,"
            + COLUMN_IS_NEW + " INTEGER DEFAULT 0,"
            + COLUMN_IS_PRE_ARGD + " INTEGER DEFAULT 0,"
            + COLUMN_SIP_URI + " TEXT,"
            + COLUMN_SERVER_SESSION_ID + " TEXT,"
            + COLUMN_SESSION_ID + " TEXT);";

        db.execSQL(sql);
    }

    public static synchronized BluePageCallLogDao getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BluePageCallLogDao(context);
        }
        return sInstance;
    }

    public BluePageCallLogDao(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public void setSortingType(int type) {
        mSortingType = type;
    }

    public Loader<List<BluePageCallLogModel>> getLoader() {
        return new BluePageCallLogModelLoader<List<BluePageCallLogModel>>(mAppContext, CONTENT_URI) {
            @Override
            public List<BluePageCallLogModel> loadInBackground() {
                return loadAll();
            }
        };
    }



    public void saveServerSessionId(String serverSessionId, String sessionId) {
        if (StringUtils.isEmpty(serverSessionId) || StringUtils.isEmpty(sessionId)
            || StringUtils.isEmpty(BluePageConfig.mPendingServerSessionId)) {
            return;
        }

        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();
        int updateCount = 0;

        if (db != null) {
            try {
                ContentValues values = new ContentValues();
                String where = COLUMN_START + "=?" + " AND " + COLUMN_SESSION_ID + "=?";
                String[] whereArgs = new String[] { BluePageConfig.mPendingServerSessionId, sessionId };

                db.beginTransaction();
                values.put(COLUMN_SERVER_SESSION_ID, serverSessionId);
                updateCount = db.update(TABLE, values, where, whereArgs);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                if (updateCount > 0) {
                    mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                    mModelMap.clear();
                }
                db.endTransaction();
            }
        }

        BluePageConfig.mPendingServerSessionId = "";
    }

    public void saveCallLog(BluePageCallLogModel model) {
        if (model == null) {
            return;
        }

        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                db.beginTransaction();
                saveOneSafe(db, model);
                db.setTransactionSuccessful();
            } finally {
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                db.endTransaction();
            }
        }
    }

    public void saveCallLogs(ArrayList<BluePageCallLogModel> list) {
        if (list == null) {
            return;
        }

        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                db.beginTransaction();
                for (BluePageCallLogModel calllog : list) {
                    saveOneSafe(db, calllog);
                }

                db.setTransactionSuccessful();
            } finally {
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                db.endTransaction();
            }
        }
    }

    public void saveOneSafe(SQLiteDatabase db, BluePageCallLogModel calllog) {
        if (calllog == null) {
            return;
        }

        BluePageCallLogModel model = loadOneByCallLogId(calllog.getId());
        final String where = COLUMN_ID + "=?";
        final String[] whereArgs = new String[] { String.valueOf(calllog.getId()) };
        ContentValues values = new ContentValues();

        if (model != null) {
            values.put(COLUMN_ID, model.getId());
        }

        values.put(COLUMN_OEM_CALL_ID, calllog.getOemCallId());
        values.put(COLUMN_NAME, calllog.getName());
        values.put(COLUMN_NUMBER, calllog.getNumber());
        values.put(COLUMN_START, calllog.getStartTimeMillis());
        values.put(COLUMN_DURATION, calllog.getDurationMillis());
        values.put(COLUMN_CALL_TYPE, calllog.getCallType());
        values.put(COLUMN_CALL_KIND, calllog.getCallKind());
        values.put(COLUMN_COUNTRY_ISO, calllog.getCountryiso());
        values.put(COLUMN_VOICEMAIL_URI, calllog.getVoicemail_uri());
        values.put(COLUMN_GEOCODED_LOCATION, calllog.getGeocoded_location());
        values.put(COLUMN_NUMBER_TYPE, calllog.getNumbertype());
        values.put(COLUMN_NUMBER_LABEL, calllog.getNumberlabel());
        values.put(COLUMN_LOOKUP_URI, calllog.getLookup_uri());
        values.put(COLUMN_MACHED_NUMBER, calllog.getMatched_number());
        values.put(COLUMN_NORMALIZED_NUMBER, calllog.getNormalized_number());
        values.put(COLUMN_PHOTO_ID, calllog.getPhoto_id());
        values.put(COLUMN_FORMATTED_NUMBER, calllog.getFormatted_number());
        values.put(COLUMN_PRESENTATION, calllog.getPresentation());
        values.put(COLUMN_CONTACTS_ID, calllog.getContacts_id());
        values.put(COLUMN_IS_NEW, calllog.isNew() ? 1 : 0);
        values.put(COLUMN_IS_PRE_ARGD, calllog.isPreArgd() ? 1 : 0);
        values.put(COLUMN_SERVER_SESSION_ID, calllog.getServerSessionId());
        values.put(COLUMN_SESSION_ID, calllog.getSessionId());

        int updatedCount = db.update(TABLE, values, where, whereArgs);
        if (updatedCount == 0) {
            db.insert(TABLE, null, values);
        }
    }

    public BluePageCallLogModel loadOneByCallLogId(int _id) {
        BluePageCallLogModel model = null;
        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {
            try {
                final String where = COLUMN_ID + "=?";
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

    public BluePageCallLogModel loadOneByOemCallLogId(String oemCallLogId) {
        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();
        BluePageCallLogModel model = null;

        if (db != null) {
            try {
                final String where = COLUMN_OEM_CALL_ID + "=?";
                final String[] whereArgs = { oemCallLogId };

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

    public List<BluePageCallLogModel> loadAll() {
        List<BluePageCallLogModel> result = new ArrayList<BluePageCallLogModel>();
        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();

        if (db != null) {
            try {
                db.beginTransaction();

                switch (mSortingType) {
                    case SORT_BY_DATE:
                        Cursor cursor = db.query(TABLE, null, null, null, null, null, COLUMN_START + " DESC");
                        if (cursor != null) {
                            if (cursor.moveToFirst() == true) {
                                do {
                                    BluePageCallLogModel model = fromCursor(cursor);
                                    if (model != null) {
                                        result.add(model);
                                    }
                                } while (cursor.moveToNext());
                            }
                            cursor.close();
                        }
                        break;
                    case SORT_BY_GROUP:
                        // 1. Query Group Pre-arranged call
                        StringBuilder prearrangedSQL = new StringBuilder();
                        prearrangedSQL.append("SELECT * FROM CALLLOG ");
                        prearrangedSQL.append("WHERE " + COLUMN_IS_PRE_ARGD + "=1 ");
                        prearrangedSQL.append("AND (" + COLUMN_OEM_CALL_ID + " IS NULL OR " + COLUMN_OEM_CALL_ID + "='') ");
                        prearrangedSQL.append("ORDER BY " + COLUMN_NAME + " COLLATE LOCALIZED;");
                        Cursor prearrangedCursor = db.rawQuery(prearrangedSQL.toString(), null);
                        if (prearrangedCursor != null) {
                            if (prearrangedCursor.moveToFirst() == true) {
                                do {
                                    BluePageCallLogModel model = fromCursor(prearrangedCursor);
                                    if (model != null) {
                                        result.add(model);
                                    }
                                } while (prearrangedCursor.moveToNext());
                            }
                            prearrangedCursor.close();
                        }

                        // 2. Query Ad-hoc call
                        StringBuilder adhocSQL = new StringBuilder();
                        adhocSQL.append("SELECT * FROM CALLLOG ");
                        adhocSQL.append("WHERE " + COLUMN_IS_PRE_ARGD + "=0 ");
                        adhocSQL.append("AND (" + COLUMN_OEM_CALL_ID + " IS NULL OR " + COLUMN_OEM_CALL_ID + "='') ");
                        adhocSQL.append("ORDER BY " + COLUMN_NAME + " COLLATE LOCALIZED;");
                        Cursor adhocCursor = db.rawQuery(adhocSQL.toString(), null);
                        if (adhocCursor != null) {
                            if (adhocCursor.moveToFirst() == true) {
                                do {
                                    BluePageCallLogModel model = fromCursor(adhocCursor);
                                    if (model != null) {
                                        result.add(model);
                                    }
                                } while (adhocCursor.moveToNext());
                            }
                            adhocCursor.close();
                        }

                        // 3. Query OEM call
                        StringBuilder oemSQL = new StringBuilder();
                        oemSQL.append("SELECT * FROM CALLLOG ");
                        oemSQL.append("WHERE " + COLUMN_IS_PRE_ARGD + "=0 ");
                        oemSQL.append("AND (" + COLUMN_OEM_CALL_ID + " IS NOT NULL AND " + COLUMN_OEM_CALL_ID + "!='') ");
                        oemSQL.append("ORDER BY " + COLUMN_NAME + " COLLATE LOCALIZED;");
                        Cursor oemCursor = db.rawQuery(oemSQL.toString(), null);
                        if (oemCursor != null) {
                            if (oemCursor.moveToFirst() == true) {
                                do {
                                    BluePageCallLogModel model = fromCursor(oemCursor);
                                    if (model != null) {
                                        result.add(model);
                                    }
                                } while (oemCursor.moveToNext());
                            }
                            oemCursor.close();
                            cursor = null;
                        }
                        break;
                    case SORT_BY_ABC:
                        cursor = db.query(TABLE, null, null, null, null, null, COLUMN_NAME + " COLLATE LOCALIZED");
                        if (cursor != null) {
                            if (cursor.moveToFirst() == true) {
                                do {
                                    BluePageCallLogModel model = fromCursor(cursor);
                                    if (model != null) {
                                        result.add(model);
                                    }
                                } while (cursor.moveToNext());
                            }
                            cursor.close();
                            cursor = null;
                        }
                        break;
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        return result;
    }

    public void deleteOneByCallLogId(int _id, BluePageCallLogDaoCallBack<Void> callback) {
        int deletedCount = 0;

        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                final String where = COLUMN_ID + "=?";
                final String[] whereArgs = { String.valueOf(_id) };

                db.beginTransaction();
                deletedCount = db.delete(TABLE, where, whereArgs);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                notifyCallbackFailure(callback, null);
                Log.e("", e.getMessage(), e);
            } finally {
                notifyCallbackCompleted(callback, null);
                if (deletedCount > 0) {
                    mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                }
                db.endTransaction();
            }
        }
    }

    public void deleteByCallLogIdList(ArrayList<Integer> callLogIDList, BluePageCallLogDaoCallBack<Void> callback) {
        int deletedCount = 0;

        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                final String where = COLUMN_ID + "=?";
                db.beginTransaction();
                for (int callLogId : callLogIDList) {
                    BluePageCallLogModel model = loadOneByCallLogId(callLogId);
                    if (model != null) {
                        if (StringUtils.isNotEmpty(model.getOemCallId())) {
                            deleteOneByOemCallLogId(model.getOemCallId());
                        } else {
                            final String[] whereArgs = { String.valueOf(callLogId) };
                            deletedCount = db.delete(TABLE, where, whereArgs);
                        }
                    }
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                notifyCallbackFailure(callback, null);
                Log.e("", e.getMessage(), e);
            } finally {
                notifyCallbackCompleted(callback, null);
                if (deletedCount > 0) {
                    mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                }
                db.endTransaction();
            }
        }
    }

    public void deleteOneByOemCallLogId(String oemCallLogId) {
        int deletedCount = 0;

        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                final String where = COLUMN_OEM_CALL_ID + "=?";
                final String[] whereArgs = { oemCallLogId };

                db.beginTransaction();
                deletedCount = db.delete(TABLE, where, whereArgs);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                if (deletedCount > 0) {
                    mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                }
                db.endTransaction();
            }
        }
    }

    public void deleteByOemCallLogIdList(ArrayList<String> oemCallLogIDList) {
        int deletedCount = 0;

        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                final String where = COLUMN_OEM_CALL_ID + "=?";
                db.beginTransaction();
                for (String oemCallLogId : oemCallLogIDList) {
                    final String[] whereArgs = { oemCallLogId };

                    deletedCount = db.delete(TABLE, where, whereArgs);
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                if (deletedCount > 0) {
                    mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                }
                db.endTransaction();
            }
        }
    }

    public void removeNewTags() {
        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                db.beginTransaction();
                for (BluePageCallLogModel calllog : loadAll()) {
                    if (calllog.isNew()) {
                        calllog.setNew(false);
                        saveOneSafe(db, calllog);
                    }
                }

                db.setTransactionSuccessful();
            } finally {
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                db.endTransaction();
            }
        }
    }

    public void deleteCallLogWithoutSessionInfo() {
        int deletedCount = 0;

        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getWritableDB();

        if (db != null) {
            try {
                final String where = "(" + COLUMN_SERVER_SESSION_ID + " IS NULL OR " + COLUMN_SERVER_SESSION_ID + "='') "
                    + "AND (" + COLUMN_OEM_CALL_ID + " IS NULL OR " + COLUMN_OEM_CALL_ID + "='') ";

                db.beginTransaction();
                deletedCount = db.delete(TABLE, where, null);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                if (deletedCount > 0) {
                    mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
                }
                db.endTransaction();
            }
        }
    }

    private BluePageCallLogModel fromCursor(Cursor cursor) {
        BluePageCallLogModel model = new BluePageCallLogModel();

        model.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        model.setOemCallId(cursor.getString(cursor.getColumnIndex(COLUMN_OEM_CALL_ID)));
        model.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        model.setNumber(cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)));
        model.setStartTimeMillis(cursor.getLong(cursor.getColumnIndex(COLUMN_START)));
        model.setDurationMillis(cursor.getLong(cursor.getColumnIndex(COLUMN_DURATION)));
        model.setCallType(cursor.getInt(cursor.getColumnIndex(COLUMN_CALL_TYPE)));
        model.setCallKind(cursor.getInt(cursor.getColumnIndex(COLUMN_CALL_KIND)));
        model.setCountryiso(cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_ISO)));
        model.setVoicemail_uri(cursor.getString(cursor.getColumnIndex(COLUMN_VOICEMAIL_URI)));
        model.setGeocoded_location(cursor.getString(cursor.getColumnIndex(COLUMN_GEOCODED_LOCATION)));
        model.setNumbertype(cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER_TYPE)));
        model.setNumberlabel(cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER_LABEL)));
        model.setLookup_uri(cursor.getString(cursor.getColumnIndex(COLUMN_LOOKUP_URI)));
        model.setMatched_number(cursor.getString(cursor.getColumnIndex(COLUMN_MACHED_NUMBER)));
        model.setNormalized_number(cursor.getString(cursor.getColumnIndex(COLUMN_NORMALIZED_NUMBER)));
        model.setPhoto_id(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_ID)));
        model.setFormatted_number(cursor.getString(cursor.getColumnIndex(COLUMN_FORMATTED_NUMBER)));
        model.setPresentation(cursor.getInt(cursor.getColumnIndex(COLUMN_PRESENTATION)));
        model.setContacts_id(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACTS_ID)));
        model.setNew(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_NEW)) == 1 ? true : false);
        model.setPreArgd(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_PRE_ARGD)) == 1 ? true : false);
        model.setServerSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SERVER_SESSION_ID)));
        model.setSessionId(cursor.getString(cursor.getColumnIndex(COLUMN_SESSION_ID)));

        long start = model.getStartTimeMillis();
        long duration = model.getDurationMillis();
        long end = start + duration;

        model.setEndTimeMillis(end);
        model.setStartTimeString(getFormattedTimeString(start));
        model.setEndTimeString(getFormattedTimeString(end));
        model.setDurationString(getFormattedDurationString(duration * 1000));
        model.setListLabel(getFormattedListLabelString(start));

        return model;
    }

    private String getLookupKeyFromLookupUri(Uri uri) {
        String lookupKey = "";
        String[] projection = ArrayUtils.toArray(ContactsContract.Contacts.LOOKUP_KEY);
        Cursor cursor = mAppContext.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                lookupKey = cursor.getString(0);
            }
            cursor.close();
        }

        return lookupKey;
    }

    public void notifyContentObserver() {
        mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
    }

    private String getFormattedDurationString(long milliseconds) {
        String duration = "";
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        if (hours < 1) {
            duration = minutes + "분 " + seconds + "초";
        } else {
            duration = hours + "시간 " + minutes + "분 " + seconds + "초";
        }
        return duration;
    }

    private String getFormattedTimeString(long milliseconds) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("M월dd일\na hh:mm");
        String sDate = sdfDate.format(milliseconds);
        return sDate;
    }

    private String getFormattedListLabelString(long milliseconds) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy.MM.dd");
        String sDate = sdfDate.format(milliseconds);
        String sToday = sdfDate.format(System.currentTimeMillis());

        if (StringUtils.equals(sDate, sToday)) {
            return mAppContext.getString(R.string.calllog_list_section_label_today);
        } else {
            return sDate;
        }
    }

    public void doCheckOemCallLogUpdated(long lastUpdate, final BluePageCallLogDaoCallBack<Void> callback) {
        OemCallLogUpdateChecker updateChecker = new OemCallLogUpdateChecker(callback);
        updateChecker.execute(lastUpdate);
    }

    private class OemCallLogUpdateChecker extends AsyncTask<Long, Void, Void> {
        BluePageCallLogDaoCallBack<Void> callback;

        public OemCallLogUpdateChecker(final BluePageCallLogDaoCallBack<Void> cb) {
            callback = cb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Long... params) {
            checkOnlyOEMUpdatedCallLog(params[0]);
            checkOnlyOEMDeletedCallLog();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            PreferenceManager.getDefaultSharedPreferences(mAppContext).edit()
            .putLong(BluePageConfig.PREF_KEY_CALLLOG_UPDATE_TIMESTAMP, System.currentTimeMillis()).apply();
            notifyCallbackCompleted(callback, null);
        }
    }

    @SuppressLint("InlinedApi")
    private void checkOnlyOEMUpdatedCallLog(Long lastUpdated) {
        ArrayList<BluePageCallLogModel> resultList = new ArrayList<BluePageCallLogModel>();
        Cursor callLogCursor = mAppContext.getContentResolver()
            .query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.DATE + " > ?",
                new String[] { Long.toString(lastUpdated) }, CallLog.Calls.DEFAULT_SORT_ORDER);

        if (callLogCursor != null) {
            while (callLogCursor.moveToNext()) {
                BluePageCallLogModel model = new BluePageCallLogModel();

                String oemCallId = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls._ID));
                String name = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.CACHED_NAME));
                String cacheNumber = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL));
                String number = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.NUMBER));
                long dateTimeMillis = callLogCursor.getLong(callLogCursor
                    .getColumnIndex(CallLog.Calls.DATE));
                long durationMillis = callLogCursor.getLong(callLogCursor
                    .getColumnIndex(CallLog.Calls.DURATION));
                int callType = callLogCursor.getInt(callLogCursor
                    .getColumnIndex(CallLog.Calls.TYPE));
                int callKind = CAll_KIND_OEM_VOICE_CALL; // Temp. Video call is not an action. It is different by Manufacture.

                String durationString = getFormattedDurationString(durationMillis * 1000);
                String startTimeString = getFormattedTimeString(dateTimeMillis);
                String endTimeString = getFormattedTimeString(dateTimeMillis + durationMillis);
                if (cacheNumber == null) {
                    cacheNumber = number;
                }

                String countryiso = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.COUNTRY_ISO));
                String voicemail_uri = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.VOICEMAIL_URI));
                String geocoded_location = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.GEOCODED_LOCATION));
                String numbertype = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.CACHED_NUMBER_TYPE));
                String numberlabel = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL));
                String lookup_uri = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI));
                String matched_number = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.CACHED_MATCHED_NUMBER));
                String normalized_number = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.CACHED_NORMALIZED_NUMBER));
                String photo_id = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.CACHED_PHOTO_ID));
                String formatted_number = callLogCursor.getString(callLogCursor
                    .getColumnIndex(CallLog.Calls.CACHED_FORMATTED_NUMBER));
                int presentation = callLogCursor.getInt(callLogCursor
                    .getColumnIndex(CallLog.Calls.NUMBER_PRESENTATION));
                String contacts_id = ""; /*= callLogCursor.getString(12); // Oops!! */
                boolean isNew = false;
                boolean isPreArgd = false;

                /*                if (StringUtils.isNotEmpty(lookup_uri)) {
                                                        if (StringUtils.isEmpty(photo_id) || photo_id.equals("0")) {
                                        String[] projection = ArrayUtils.toArray(ContactsContract.Contacts.PHOTO_ID);
                                        Cursor cursor = mAppContext.getContentResolver()
                                            .query(Uri.parse(lookup_uri), projection, null, null, null);
                                        if ((cursor != null) && cursor.moveToFirst()) {
                                            photo_id = Long.toString(cursor.getLong(0));
                                            cursor.close();
                                        }
                                    }

                                    if (StringUtils.isEmpty(contacts_id) || contacts_id.equals("0")) {
                                        String[] projection = ArrayUtils.toArray(ContactsContract.Contacts._ID);
                                        Cursor cursor = mAppContext.getContentResolver()
                                            .query(Uri.parse(lookup_uri), projection, null, null, null);
                                        if ((cursor != null) && cursor.moveToFirst()) {
                                            contacts_id = Long.toString(cursor.getLong(0));
                                            cursor.close();
                                        }
                                    }
                                }*/

                model.setOemCallId(oemCallId);
                model.setName(name);
                model.setNumber(cacheNumber);
                model.setStartTimeString(startTimeString);
                model.setEndTimeString(endTimeString);
                model.setDurationString(durationString);
                model.setCallType(callType);
                model.setCallKind(callKind);
                model.setCountryiso(countryiso);
                model.setVoicemail_uri(voicemail_uri);
                model.setGeocoded_location(geocoded_location);
                model.setNumbertype(numbertype);
                model.setNumberlabel(numberlabel);
                model.setLookup_uri(lookup_uri);
                model.setMatched_number(matched_number);
                model.setNormalized_number(normalized_number);
                model.setPhoto_id(photo_id);
                model.setFormatted_number(formatted_number);
                model.setPresentation(presentation);
                model.setContacts_id(contacts_id);
                model.setNew(isNew);
                model.setPreArgd(isPreArgd);

                resultList.add(model);
            }
            callLogCursor.close();
        }

        if (resultList.size() > 0) {
            saveCallLogs(resultList);
        }
    }

    private void checkOnlyOEMDeletedCallLog() {
        HashSet<String> oemCallLogIdSet = new HashSet<String>();
        BluePageCallLogDBManager dbMgr = BluePageCallLogDBManager.getInstance(mAppContext);
        SQLiteDatabase db = dbMgr.getReadableDB();
        String oemCallLogId = null;

        if (db != null) {
            // 1. Get OemCallIds from Local DB.
            try {
                db.beginTransaction();
                Cursor localCursor = db.query(TABLE, new String[] { COLUMN_OEM_CALL_ID }, null, null, null, null, null);
                if (localCursor != null) {
                    if (localCursor.moveToFirst()) {
                        do {
                            oemCallLogId = localCursor.getString(0);
                            if (StringUtils.isNotEmpty(oemCallLogId)) {
                                oemCallLogIdSet.add(oemCallLogId);
                            }
                        } while (localCursor.moveToNext());
                    }
                    localCursor.close();
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

            // 2. Get OemCallLogIds from OEM DB.
            Uri callLogUri = CallLog.Calls.CONTENT_URI;
            Cursor oemCallLogCursor = mAppContext.getContentResolver().query(callLogUri,
                new String[] { CallLog.Calls._ID }, null, null, null);

            if (oemCallLogCursor != null) {
                if (oemCallLogCursor.moveToFirst()) {
                    do {
                        oemCallLogId = oemCallLogCursor.getString(0);
                        if (StringUtils.isNotEmpty(oemCallLogId)) {
                            oemCallLogIdSet.remove(oemCallLogId);
                        }
                    } while (oemCallLogCursor.moveToNext());
                }
                oemCallLogCursor.close();
            }

            // 3. Finally, delete it if OEM call log was deleted.
            if (oemCallLogIdSet.size() > 0) {
                deleteByOemCallLogIdList(new ArrayList<String>(oemCallLogIdSet));
            } else {
                mAppContext.getContentResolver().notifyChange(CONTENT_URI, null);
            }
        }
    }

    public ArrayList<String> getMemberIds(ArrayList<BluePageContactsModel> memberList) {
        ArrayList<String> idList = new ArrayList<String>();

        for (BluePageContactsModel model : memberList) {
            idList.add(model.getTypicalId());
        }

        return idList;
    }

    private <T> void notifyCallbackCompleted(final BluePageCallLogDao.BluePageCallLogDaoCallBack<T> l, final T data) {
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

    private void notifyCallbackFailure(final BluePageCallLogDao.BluePageCallLogDaoCallBack<?> l, final String data) {
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
}