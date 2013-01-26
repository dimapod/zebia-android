package com.zebia.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StorageItemsHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 9;
    //public static final String DATABASE_NAME = "ItemsStorage.db";

    public StorageItemsHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SerialCashDao.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SerialCashDao.ItemEntry.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

}
