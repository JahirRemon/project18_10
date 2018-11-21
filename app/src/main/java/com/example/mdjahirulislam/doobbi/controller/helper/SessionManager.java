package com.example.mdjahirulislam.doobbi.controller.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class SessionManager {


    private static String TAG = SessionManager.class.getSimpleName();


    private SharedPreferences pref;

    private SharedPreferences.Editor editor;
    private Context _context;


    private int PRIVATE_MODE = 0;


    private static final String PREF_NAME = "Doobbi";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_UNIQUE_ID="userUniqueId";
    private static final String KEY_IS_SET_UP="isSetUp";
    private static final String KEY_TOTAL_PRICE="totalPrice";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
//        editor.putString(KEY_USER_UNIQUE_ID,userId);

        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setUp(boolean isSetUp){
        editor.putBoolean(KEY_IS_SET_UP,isSetUp);
        editor.commit();
        Log.d(TAG, "SetUp session modified!"+" ### ID="+isSetUp);
    }




    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isSetUp(){
        return pref.getBoolean(KEY_IS_SET_UP, false);
    }
    public String getUserId(){
        return pref.getString(KEY_USER_UNIQUE_ID, null);
    }


    public void setTotalPrice(String price){
        editor.putString( KEY_TOTAL_PRICE,price );

        editor.commit();
        Log.d( TAG, "setTotalPrice: " +price);

    }

    public String getTotalPrice(){
        return pref.getString( KEY_TOTAL_PRICE,null);
    }




}
