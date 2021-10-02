package com.nandyala.quizkotlinnandyala.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


object ConnectivityManager : LiveData<Boolean>() {

    val TAG = "C-Manager"
    private lateinit var mNetworkcallback: ConnectivityManager.NetworkCallback
    private lateinit var mConnectivityManager: ConnectivityManager
    val mNetworkSet: MutableSet<Network> = HashSet<Network>()
    private var isInternetOld: Boolean = false
    fun checkInternetOldVersion() {
        postValue(isInternetOld)
    }

    fun checkInternetAvailability() {
        postValue(mNetworkSet.size > 0)
    }


    fun registerNetworkCallback(mContext: Context?) {
        mNetworkcallback = createNetworkCallBack()
        mConnectivityManager =
            mContext?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val mNetworkRequest = NetworkRequest.Builder()
              //  .addCapability(NET_CAPABILITY_INTERNET)
                .addCapability(NET_CAPABILITY_VALIDATED)
                /*
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)*/

                .build()
            mConnectivityManager.registerNetworkCallback(mNetworkRequest, mNetworkcallback)
        } else {
            if (mConnectivityManager.activeNetworkInfo?.isConnected == true) {
                CoroutineScope(Dispatchers.IO).launch {

// val isInternet = NetworkInternetTest.execute(network.socketFactory)
                    isInternetOld = NetworkInternetTesForConnectivityManager().execute(mContext!!)
                    checkInternetOldVersion()
                }
            }
        }


    }

    fun unRegisterNetworkCallback(mContext: Context?) {
        try {
            mConnectivityManager.unregisterNetworkCallback(mNetworkcallback)
        } catch (ex: Exception) {
            ex.message
        }

    }


    private fun createNetworkCallBack() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            //super.onAvailable(network)
            Log.d(TAG, "onAvailable: ${network}")
            val mNetworkCapabilities = mConnectivityManager.getNetworkCapabilities(network)
            val hasInternetCapabilities = mNetworkCapabilities?.hasCapability(
                NET_CAPABILITY_INTERNET
            )
            Log.d(TAG, "onAvailable: ${network}, ${hasInternetCapabilities}")

            if (hasInternetCapabilities == true) {

                CoroutineScope(Dispatchers.IO).launch {

                    val isInternet = NetworkInternetTest.execute(network.socketFactory)
                    if (isInternet) {
                        withContext(Dispatchers.Main) {
                            mNetworkSet.add(network)
                            checkInternetAvailability()
                        }
                    }


                }


            }


        }

        override fun onLost(network: Network) {
            //super.onLost(network)

            Log.d(TAG, "onLost: ${network}")
            mNetworkSet.remove(network)
            checkInternetAvailability()

        }
    }


}
