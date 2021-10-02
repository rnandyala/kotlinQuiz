package com.nandyala.quizkotlinnandyala

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.nandyala.quizkotlinnandyala.util.ConnectivityManager
import com.nandyala.quizkotlinnandyala.util.Util

class SplashActivity : AppCompatActivity() {

    lateinit var mFirebaseAuth: FirebaseAuth
    var isInternet: Boolean? = false
    private fun setGoogleSignOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_google))
            .requestEmail()
            .build()
        var mGoogleSigninClient = GoogleSignIn.getClient(this, gso);
        mGoogleSigninClient.signOut().addOnCompleteListener(
            this, {
                it.isSuccessful;
            }
        )


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConnectivityManager.observe(
            this, {
                isInternet = it
            }
        )
        mFirebaseAuth = FirebaseAuth.getInstance()
        if (mFirebaseAuth.currentUser != null && Util.getIsInternetSharedPref(this) == true) {
            val i: Intent = Intent(this, EnterQuizActivity::class.java)
            startActivity(i);
            finish()
        } else if (Util.getIsInternetSharedPref(this) == false && isInternet == true) {
            mFirebaseAuth = FirebaseAuth.getInstance()
            mFirebaseAuth.signOut()
            LoginManager.getInstance().logOut()
            setGoogleSignOut()
            Util.setInternetSharedPref(this, true)
        } else {
            val i: Intent = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()

        }


        // check if user is already logged in if that is true then take him directly to
        // EnterQuizActivity hint: use sharedpreference

    }


    override fun onStart() {
        super.onStart()
        ConnectivityManager.registerNetworkCallback(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ConnectivityManager.unRegisterNetworkCallback(this)
    }
}