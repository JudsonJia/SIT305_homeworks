package com.example.task61d;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    //table users
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "User";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    //table user interests
    private static final String TABLE_USER_INTERESTS = "user_interests";
    private static final String COLUMN_USER_INTEREST_USERNAME = "username";
    private static final String COLUMN_USER_INTEREST_INTEREST_NAME = "interest_name";


    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_PASSWORD + " TEXT)";

    private static final  String createUserInterestsTableQuery = "CREATE TABLE " + TABLE_USER_INTERESTS + " (" +
            COLUMN_USER_INTEREST_USERNAME + " TEXT," +
            COLUMN_USER_INTEREST_INTEREST_NAME + " TEXT," +
            "PRIMARY KEY(" + COLUMN_USER_INTEREST_USERNAME + ", " + COLUMN_USER_INTEREST_INTEREST_NAME + ")," +
            "FOREIGN KEY(" + COLUMN_USER_INTEREST_USERNAME + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_USERNAME + "))";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(createUserInterestsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        long userId = db.insert(TABLE_NAME, null, values);
        db.close();
        return userId != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public void insertUserInterest(String username, String interestName) {
        DBHelper dbHelper = this;
        if (!dbHelper.isInterestExist(username, interestName)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_INTEREST_USERNAME, username);
            values.put(COLUMN_USER_INTEREST_INTEREST_NAME, interestName);
            db.insert(TABLE_USER_INTERESTS, null, values);
            db.close();
        }

    }

    public List<String> getUserInterests(String username) {
        List<String> interests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_INTERESTS, new String[]{COLUMN_USER_INTEREST_INTEREST_NAME},
                COLUMN_USER_INTEREST_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String interestName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_INTEREST_INTEREST_NAME));
                interests.add(interestName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return interests;
    }

    public boolean isInterestExist(String username, String interestName) {
        String query = "SELECT 1 FROM user_interests WHERE username = ? AND interest_name = ? LIMIT 1";
        Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{username, interestName});
        boolean exist = cursor.moveToFirst();
        cursor.close();
        return exist;
    }
}
