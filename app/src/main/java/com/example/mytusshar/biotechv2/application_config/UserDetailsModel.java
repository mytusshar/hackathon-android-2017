package com.example.mytusshar.biotechv2.application_config;

import android.content.Context;

import com.example.mytusshar.biotechv2.signin_pkg.SQLiteHandler;

import java.util.HashMap;

/**
 * Created by mytusshar on 21-Mar-17.
 */
public class UserDetailsModel {

    private String USER_ID;
    private String USER_NAME;
    private String EMAIL;
    private String PASSWORD;


    SQLiteHandler database;

////////////////////////////////////////////////////////////////////////////////////////////////////


    public UserDetailsModel(Context context){
        database = new SQLiteHandler(context);
        HashMap<String, String> user = database.getUserDetails();

        USER_ID = user.get(SQLiteHandler.USER_ID);
        USER_NAME = user.get(SQLiteHandler.USER_NAME);
        EMAIL = user.get(SQLiteHandler.EMAIL);
        PASSWORD = user.get(SQLiteHandler.PASSWORD);

    }

    public String getUSER_ID(){
        return USER_ID;
    }

    public String getUSER_NAME(){
        return USER_NAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public String getEMAIL(){
        return EMAIL;
    }




}
