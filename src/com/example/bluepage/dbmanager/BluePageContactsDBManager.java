package com.example.bluepage.dbmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bluepage.BluePageConfig;
import com.example.bluepage.utils.Log;

public class BluePageContactsDBManager {

    private static BluePageContactsDBManager sInstance;

    private final Context mAppContext;
    private volatile BluePageContactsDatabaseHelper mHelper;
    private volatile SQLiteDatabase mReadbleDB, mWritableDB;

    public synchronized static BluePageContactsDBManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BluePageContactsDBManager(context);
        }
        return sInstance;
    }

    private BluePageContactsDBManager(Context context) {
        mAppContext = context.getApplicationContext();
        mHelper = new BluePageContactsDatabaseHelper(mAppContext);
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
        db.delete(BluePageContactsDao.TABLE, null, null);
    }

    static class BluePageContactsDatabaseHelper extends SQLiteOpenHelper {

        protected static final int DB_VERSION = 1;

        BluePageContactsDatabaseHelper(Context context) {
            super(context, BluePageConfig.BLUEPAGE_CONTACTS_DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                BluePageContactsDao.onDbCreate(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion) {
                case 1:
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
