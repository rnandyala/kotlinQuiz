package com.nandyala.quizkotlinnandyala.util

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.preference.PreferenceManager
import com.facebook.share.Share


object Util {
    private lateinit var mEditor: SharedPreferences.Editor;
    private val ACESS_TOKEN_FACEBOOK = "Facebook_Token"
    private val INTERNET_STATUS = "internet_status"


    fun setEditor(mContext: Context): SharedPreferences.Editor {

        var mSharedPref: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(mContext)
        mEditor = mSharedPref.edit();
        return mEditor;
    }


    fun setInternetSharedPref(mContext: Context, isInternet: Boolean) {
        mEditor = setEditor(mContext)
        mEditor.putBoolean(INTERNET_STATUS, isInternet)
        mEditor.commit()

    }


    fun setLoginSharedPref(
        mContext: Context,
        mAccessToken: String
    ) {

        mEditor = setEditor(mContext)
        mEditor.putString(ACESS_TOKEN_FACEBOOK, mAccessToken);
        mEditor.commit();

    }


    fun getLoginSharedPref(mContext: Context): String? {
        var mSharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        return mSharedPref.getString(ACESS_TOKEN_FACEBOOK, "");
    }


    fun getIsInternetSharedPref(mContext: Context): Boolean? {
        var mSharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        return mSharedPref.getBoolean(INTERNET_STATUS, true)
    }


}