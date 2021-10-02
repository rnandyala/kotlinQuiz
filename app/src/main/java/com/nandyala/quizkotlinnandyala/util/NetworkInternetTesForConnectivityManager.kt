package com.nandyala.quizkotlinnandyala.util

import android.content.Context
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NetworkInternetTesForConnectivityManager {

    private val TAG = "connectivity_manager"
    fun execute(mContext: Context): Boolean {
        return try {
            var mHttpURl: HttpURLConnection =
                URL("http://clients3.google.com/generate_204").openConnection() as HttpURLConnection
            mHttpURl.addRequestProperty("User-Agent", "Test")
            mHttpURl.addRequestProperty("Connection", "close")
            mHttpURl.setConnectTimeout(1500)
            mHttpURl.connect()
             (mHttpURl.responseCode == 204)

        } catch (ex: IOException) {
            Log.e(TAG, ex.message.toString())
            return false
        }
    }


}