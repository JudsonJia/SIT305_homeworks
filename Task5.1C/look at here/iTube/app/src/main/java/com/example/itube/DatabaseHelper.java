package com.example.itube;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "iTubeDatabase";
    private static final int DATABASE_VERSION = 1;

    // 用户表
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // 播放列表表
    private static final String TABLE_PLAYLISTS = "playlists";
    private static final String COLUMN_PLAYLIST_ID = "playlist_id";
    private static final String COLUMN_PLAYLIST_NAME = "playlist_name";
    private static final String COLUMN_USER_ID_FK = "user_id_fk";
    private static final String COLUMN_VIDEO_URL = "video_url";

    // Create an SQL statement for the user table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS +
            "(" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_FULL_NAME + " TEXT," +
            COLUMN_USERNAME + " TEXT," +
            COLUMN_PASSWORD + " TEXT" +
            ")";

    // Create an SQL statement for a playlist table
    private static final String CREATE_TABLE_PLAYLISTS = "CREATE TABLE " + TABLE_PLAYLISTS +
            "(" +
            COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_PLAYLIST_NAME + " TEXT," +
            COLUMN_USER_ID_FK + " INTEGER," +
            COLUMN_VIDEO_URL + " TEXT," +
            "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a user table and a playlist table
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PLAYLISTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If the database version number changes, you can add the corresponding processing logic here
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public boolean addUser(String fullName, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long userId = db.insert(TABLE_USERS, null, values);
        db.close();
        if (userId != -1) {

            return true;
        } else {

            return false;
        }
    }

    @SuppressLint("Range")
    public ArrayList<String> retrievePlaylist(String username) {
        ArrayList<String> playlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the user table to obtain the user ID
        String queryUserId = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursorUserId = db.rawQuery(queryUserId, new String[]{username});

        int userId = -1;

        // Check the query result and get the user ID
        if (cursorUserId != null && cursorUserId.moveToFirst()) {
            userId = cursorUserId.getInt(cursorUserId.getColumnIndex(COLUMN_USER_ID));
            cursorUserId.close();
        }

        // If a matching user ID is found, the playlist table is queried for the corresponding video URL
        if (userId != -1) {
            String queryPlaylist = "SELECT " + COLUMN_VIDEO_URL + " FROM " + TABLE_PLAYLISTS +
                    " WHERE " + COLUMN_USER_ID_FK + " = ?";
            Cursor cursorPlaylist = db.rawQuery(queryPlaylist, new String[]{String.valueOf(userId)});

            // Iterate through the query results to add the video URL to the playlist
            if (cursorPlaylist != null && cursorPlaylist.moveToFirst()) {
                do {
                    String videoUrl = cursorPlaylist.getString(cursorPlaylist.getColumnIndex(COLUMN_VIDEO_URL));
                    playlist.add(videoUrl);
                } while (cursorPlaylist.moveToNext());
                cursorPlaylist.close();
            }
        }


        db.close();

        return playlist;
    }

    @SuppressLint("Range")
    public void addUrlToPlaylist(String username, String videoUrl) {
        SQLiteDatabase db = this.getWritableDatabase();


        String queryUserId = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursorUserId = db.rawQuery(queryUserId, new String[]{username});

        int userId = -1;


        if (cursorUserId != null && cursorUserId.moveToFirst()) {
            userId = cursorUserId.getInt(cursorUserId.getColumnIndex(COLUMN_USER_ID));
            cursorUserId.close();
        }


        if (userId != -1) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PLAYLIST_NAME, "Default playlist"); // Here you can set the playlist name as you like
            values.put(COLUMN_USER_ID_FK, userId);
            values.put(COLUMN_VIDEO_URL, videoUrl);


            long newRowId = db.insert(TABLE_PLAYLISTS, null, values);
            if (newRowId != -1) {
                // Insertion successful

            } else {
                System.exit(0);
            }
        }


        db.close();
    }
}