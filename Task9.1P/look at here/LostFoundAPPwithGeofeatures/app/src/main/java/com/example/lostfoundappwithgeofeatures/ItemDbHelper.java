package com.example.lostfoundappwithgeofeatures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ITEMS = "items";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "location";

    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE " + TABLE_ITEMS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TYPE + " TEXT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_LOCATION + " TEXT)";

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public boolean insertAdvert(Item item, ItemDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemDbHelper.COLUMN_TYPE, item.getType());
        values.put(ItemDbHelper.COLUMN_NAME, item.getName());
        values.put(ItemDbHelper.COLUMN_DESCRIPTION, item.getDescription());
        values.put(ItemDbHelper.COLUMN_DATE, String.valueOf(item.getDate()));
        values.put(ItemDbHelper.COLUMN_LOCATION, item.getLocation());

        long newRowId = db.insert(ItemDbHelper.TABLE_ITEMS, null, values);
        db.close();
        return newRowId != -1;
    }

    public void fetchItemList(List<Item> itemList) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ItemDbHelper.COLUMN_ID,
                ItemDbHelper.COLUMN_TYPE,
                ItemDbHelper.COLUMN_NAME,
                ItemDbHelper.COLUMN_DESCRIPTION,
                ItemDbHelper.COLUMN_DATE,
                ItemDbHelper.COLUMN_LOCATION
        };

        Cursor cursor = db.query(
                ItemDbHelper.TABLE_ITEMS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String itemId = cursor.getString(cursor.getColumnIndexOrThrow(ItemDbHelper.COLUMN_ID));
            String itemType = cursor.getString(cursor.getColumnIndexOrThrow(ItemDbHelper.COLUMN_TYPE));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(ItemDbHelper.COLUMN_NAME));
            String itemDescription = cursor.getString(cursor.getColumnIndexOrThrow(ItemDbHelper.COLUMN_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(ItemDbHelper.COLUMN_DATE));
            String itemLocation = cursor.getString(cursor.getColumnIndexOrThrow(ItemDbHelper.COLUMN_LOCATION));

            Item item = new Item(itemType, itemName, itemDescription, date, itemLocation);
            itemList.add(item);
        }
        cursor.close();
        db.close();
    }

    public boolean remove(String itemName, String itemLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_NAME + " = ? AND " + COLUMN_LOCATION + " = ?";
        String[] selectionArgs = {itemName, itemLocation};

        int rowsDeleted = db.delete(TABLE_ITEMS, selection, selectionArgs);
        db.close();

        return rowsDeleted > 0;
    }

    public List<String> fetchLocationList() {
        List<String> locationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ItemDbHelper.COLUMN_LOCATION
        };

        Cursor cursor = db.query(
                ItemDbHelper.TABLE_ITEMS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String location = cursor.getString(cursor.getColumnIndexOrThrow(ItemDbHelper.COLUMN_LOCATION));
            locationList.add(location);
        }
        cursor.close();
        db.close();

        return locationList;
    }
}