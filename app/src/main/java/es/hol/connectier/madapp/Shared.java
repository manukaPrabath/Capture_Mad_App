package es.hol.connectier.madapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rexton on 2017-05-26.
 */

public class Shared {
    private static Shared mInstance;
    private static Context mCtx;
    private static final String SHARED_PREF_NAME="mysharedpref";
    private static final String KEY_USERNAME="username";
    private static final String KEY_NAME="fullname";
    private static final String KEY_USER_EMAIL="useremail";
    private static final String KEY_USER_ID="userid";

    private Shared(Context context) {
        mCtx = context;
    }

    public static synchronized Shared getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Shared(context);
        }
        return mInstance;
    }
    public boolean userLogin(int id,String username,String email,String name)
    {

        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(KEY_USER_ID,id);
        editor.putString(KEY_USER_EMAIL,email);
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_NAME,name);
        editor.apply();
        return true;
    }
    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME,null) !=null)
        {
            return true;
        }
        return false;
    }
    public boolean logOut()
    {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
    public String getUsername()
    {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME,null);
    }
    public String getFullname()
    {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME,null);
    }
    public String getUserEmail()
    {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL,null);
    }
    public int getUserid()
    {
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID,0);
    }
}
