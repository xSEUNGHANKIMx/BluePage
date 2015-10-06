package com.example.bluepage.dbmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bluepage.BluePageConfig;

public class BluePageCallLogDBManager {

    private static BluePageCallLogDBManager sInstance;

    private final Context mAppContext;
    private volatile BluePageCallLogDatabaseHelper mHelper;
    private volatile SQLiteDatabase mReadbleDB, mWritableDB;

    public synchronized static BluePageCallLogDBManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BluePageCallLogDBManager(context);
        }
        return sInstance;
    }

    private BluePageCallLogDBManager(Context context) {
        mAppContext = context.getApplicationContext();
        mHelper = new BluePageCallLogDatabaseHelper(mAppContext);
        openDB();
    }

    private void openDB() throws SQLException {
        mReadbleDB = mHelper.getReadableDatabase();
        mWritableDB = mHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDB() {
        if ((mHelper != null) && (mReadbleDB != null)) {
            return mReadbleDB;
        } else {
            return null;
        }
    }

    public SQLiteDatabase getWritableDB() {
        if ((mHelper != null) && (mWritableDB != null)) {
            return mWritableDB;
        } else {
            return null;
        }
    }

    public void deleteOldData(SQLiteDatabase db) {
        db.delete(BluePageCallLogDao.TABLE, null, null);
    }

    static class BluePageCallLogDatabaseHelper extends SQLiteOpenHelper {

        protected static final String DB_NAME = BluePageConfig.BLUEPAGE_CALL_LOG_DB_NAME;
        protected static final int DB_VERSION = 5;

        BluePageCallLogDatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                BluePageCallLogDao.onDbCreate(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion) {
                case 1:
                    db.execSQL("DROP TABLE IF EXISTS " + BluePageCallLogDao.TABLE);
                    onCreate(db);
                    // fall through
                case 2:
                    addColumn(db, BluePageCallLogDao.TABLE, BluePageCallLogDao.COLUMN_SIP_URI, "TEXT", "NULL");
                    // fall through
                case 3:
                    addColumn(db, BluePageCallLogDao.TABLE, BluePageCallLogDao.COLUMN_SERVER_SESSION_ID, "TEXT", "NULL");
                    // fall through
                case 4:
                    addColumn(db, BluePageCallLogDao.TABLE, BluePageCallLogDao.COLUMN_SESSION_ID, "TEXT", "NULL");
                    // fall through
            }
        }

        private void addColumn(SQLiteDatabase db, String table, String column, String type, String defValue) {
            Cursor c = db.query(table, null, null, null, null, null, null);

            if (c != null) {
                if (c.getColumnIndex(column) == -1) {
                    String sql = "ALTER TABLE " + table + " ADD COLUMN " + column + " " + type + " DEFAULT " + defValue;
                    db.execSQL(sql);
                }
                c.close();
            } else {
                Log.v(this.toString(), "Failed to add " + column + " into " + table);
            }
        }
    }
}