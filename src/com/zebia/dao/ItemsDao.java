package com.zebia.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import com.google.gson.Gson;
import com.zebia.model.Item;
import com.zebia.model.ZebiaResponse;

import java.util.ArrayList;
import java.util.List;

public class ItemsDao {
    private static ItemsDao instance;
    private Gson gson;

    private StorageItemsHelper storageItemsHelper;

    public ItemsDao(StorageItemsHelper storageItemsHelper) {
        this.storageItemsHelper = storageItemsHelper;
        this.gson = new Gson();
    }

    public static ItemsDao init(StorageItemsHelper storageItemsHelper) {
        if (instance == null) {
            instance = new ItemsDao(storageItemsHelper);
        }
        return instance;
    }

    public static ItemsDao getInstance() {
        return instance;
    }

    public interface ItemEntry extends BaseColumns {
        String TABLE_NAME = "items";

        String COLUMN_JSON = "json";

        int COLUMN_INDEX_ID = 0;
        int COLUMN_INDEX_JSON = 1;

        String[] COLUMNS = {_ID, COLUMN_JSON};
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                    ItemEntry._ID + " INTEGER PRIMARY KEY," +
                    ItemEntry.COLUMN_JSON + " TEXT" +
                    " )";

    private ContentValues serialize(ZebiaResponse zebiaResponse) {
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_JSON, gson.toJson(zebiaResponse));
        return values;
    }

    private ZebiaResponse deserialize(Cursor cursor) {
        String json = cursor.getString(ItemEntry.COLUMN_INDEX_JSON);
        return gson.fromJson(json, ZebiaResponse.class);
    }

    public void save(ZebiaResponse zebiaResponse) {
        SQLiteDatabase sqLiteDatabase = storageItemsHelper.getWritableDatabase();
        sqLiteDatabase.insert(ItemEntry.TABLE_NAME, null, serialize(zebiaResponse));
    }

    public ZebiaResponse restore() {
        Cursor cursor = null;
        try {
            cursor = storageItemsHelper.getReadableDatabase().query(ItemEntry.TABLE_NAME, ItemEntry.COLUMNS, null, null, null, null, null);
        } catch (Exception e) {
            Log.e("Zebia", e.getStackTrace().toString());
        }

        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }

        return deserialize(cursor);
    }

}
