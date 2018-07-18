package com.example.mytusshar.biotechv2.signin_pkg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by mytusshar on 21-Mar-17.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "android_biotech";
    // Login table name
    private static final String TABLE_USER_DETAILS = "user_details";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String IMAGE = "image";


////////////////////////////////////////////////////////////////////////////////////////////////////
    public SQLiteHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER_DETAILS + "("
                + USER_ID + " TEXT PRIMARY KEY,"
                + USER_NAME + " TEXT,"
                + EMAIL + " TEXT,"
                + PASSWORD + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "Database table created");
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);
        // Create tables again
        onCreate(db);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    //Storing user details in database
    public void addUser(String userID, String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, userID); // userid
        values.put(USER_NAME, username); // first name
        values.put(EMAIL, email); // email
        values.put(PASSWORD, password); // password

        // Inserting Row
        long id = db.insert(TABLE_USER_DETAILS, null, values);
        db.close();

        // Closing database connection
        Log.d(TAG, "*****user data inserted" + " ="+id);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
     //Getting user data from database

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> values = new HashMap<String, String>();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_DETAILS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToNext();
        if (cursor.getCount() > 0) {
            values.put(USER_ID, cursor.getString(0)); // userid
            values.put(USER_NAME, cursor.getString(1)); // first name
            values.put(EMAIL, cursor.getString(2)); // email
            values.put(PASSWORD, cursor.getString(3)); // password
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "********************Fetching user from Sqlite: " + values.toString());

        return values;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    //Re crate database Delete all tables and create them again

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER_DETAILS, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
}
