package com.nandyala.quizkotlinnandyala.util

import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

object NetworkInternetTest {

    val TAG = "internetTest"
    fun execute(mSocketFactory: SocketFactory): Boolean {
        return try {
            Log.d(TAG, "PINGING google.")

            val mSocket = mSocketFactory.createSocket() ?: throw IOException("socket is null")
            mSocket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            mSocket.close()
            Log.d(TAG, "PING success.")
            true
        } catch (e: IOException) {
            Log.e(TAG, "No internet connection. ${e}")
            false
        }
    }


}