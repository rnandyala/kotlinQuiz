package com.nandyala.quizkotlinnandyala

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Gravity.START
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration.MAX_AD_CONTENT_RATING_G
import com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.nandyala.quizkotlinnandyala.util.ConnectivityManager
import com.nandyala.quizkotlinnandyala.util.IBundle
import com.nandyala.quizkotlinnandyala.util.Util

class EnterQuizActivity : AppCompatActivity(), IBundle {
    var isInternet: Boolean = false
    lateinit var mIntent: Intent
    lateinit var mNavigationDrawer: DrawerLayout
    lateinit var mActionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var mToolbar: Toolbar
    lateinit var mFragment: Fragment;
    lateinit var mFragmentManager: FragmentManager;
    lateinit var mFragmentTransaction: FragmentTransaction
    lateinit var mNavigationView: NavigationView
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var mCoordinateLayout: CoordinatorLayout
    lateinit var mProfilePic: AppCompatImageView
    lateinit var mEmail: TextView
    var isTestResult = false
    var isEnterQuiz: Boolean = false
    var mAccessToken: String? = null

    override fun onResume() {
        super.onResume()

    }

    override fun onStart() {
        super.onStart()
        ConnectivityManager.registerNetworkCallback(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        ConnectivityManager.unRegisterNetworkCallback(this)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_quiz)
        MobileAds.initialize(this) {}

        val config = MobileAds.getRequestConfiguration().toBuilder()
            .setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
            .setMaxAdContentRating(MAX_AD_CONTENT_RATING_G).build()
        MobileAds.setRequestConfiguration(config)



        var mAdView: AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAccessToken = Util.getLoginSharedPref(this)

        ConnectivityManager.observe(
            this, {
                updateUser(it)
            }
        )



        mNavigationView = findViewById(R.id.mNavigationView)
        mNavigationDrawer = findViewById(R.id.mDrawerLayout)
        mToolbar = findViewById(R.id.mEnterQuizToolbar) as Toolbar
        setToolbar()
        mActionBarDrawerToggle = ActionBarDrawerToggle(
            this, mNavigationDrawer, mToolbar,
            R.string.start, R.string.end
        )
        mNavigationDrawer.addDrawerListener(mActionBarDrawerToggle)
        mActionBarDrawerToggle.syncState()
        mFragmentManager = supportFragmentManager
        mCoordinateLayout = findViewById(R.id.mSnack_enter_quiz)
        setEnterFragment()
        setNavigationView()
        setDataNavigationView(savedInstanceState)

    }

    private fun setDataNavigationView(mSavedState: Bundle?) {

        mProfilePic = mNavigationView.getHeaderView(0).findViewById(R.id.mProfile)
        mEmail = mNavigationView.getHeaderView(0).findViewById(R.id.mUserName)
        mFirebaseAuth = FirebaseAuth.getInstance()
        var mPhoto = mFirebaseAuth.currentUser?.photoUrl.toString()
        var mEmailId = mFirebaseAuth.currentUser?.displayName
        var mExtendedUrl = "?height=500&width=500&type=large&access_token=" + mAccessToken
        mPhoto = mPhoto + mExtendedUrl
        Glide.with(this)
            .load(mPhoto)
            .into(mProfilePic)

        if (mEmail != null) {
            mEmail.setText(mEmailId)
        }
    }

    private fun updateUser(isInternet: Boolean) {
        this.isInternet = isInternet

        if (!isInternet) {
            Snackbar.make(
                mCoordinateLayout,
                "Internet is required to update quiz results to the server",
                Snackbar.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun setFirebaseAuthLogout() {

        if (isInternet) {
            var mIntent: Intent
            mFirebaseAuth = FirebaseAuth.getInstance()
            mFirebaseAuth.signOut()
            LoginManager.getInstance().logOut()
            setGoogleSignOut()
            mIntent = Intent(this, LoginActivity::class.java)
            startActivity(mIntent)
            Util.setInternetSharedPref(this, isInternet)
        } else {
            //set Sharedpreference from here
            mIntent = Intent(this, LoginActivity::class.java)
            startActivity(mIntent)
            Util.setInternetSharedPref(this, isInternet)
        }

    }

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


    private fun setNavigationView() {
        mNavigationView.setCheckedItem(R.id.take_quiz)

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener { it ->

                when (it.itemId) {
                    R.id.take_quiz -> {
                        if (!isEnterQuiz) {
                            setEnterFragment()
                            mNavigationDrawer.closeDrawer(GravityCompat.START)
                            mNavigationView.setCheckedItem(R.id.take_quiz)
                            isEnterQuiz = true
                            isTestResult = false
                        } else {
                            mNavigationView.setCheckedItem(R.id.take_quiz)
                            mNavigationDrawer.closeDrawer(GravityCompat.START)
                        }
                        true
                    }


                    R.id.test_results -> {
                        //     val i: Intent = Intent(this, ResultFragment::class.java)
                        //   startActivity(i);
                        if (!isTestResult) {
                            setResultFragment()
                            mNavigationDrawer.closeDrawer(GravityCompat.START)
                            mNavigationView.setCheckedItem(R.id.test_results)
                            isTestResult = true
                            isEnterQuiz = false
                        } else {
                            mNavigationDrawer.closeDrawer(GravityCompat.START)
                            mNavigationView.setCheckedItem(R.id.test_results)
                        }
                        true
                    }


                    R.id.logout -> {
                        setFirebaseAuthLogout()
                        mNavigationDrawer.closeDrawer(GravityCompat.START)
                        mNavigationView.setCheckedItem(R.id.logout)
                        //finish()
                        true
                    }
// default case like switchcase android
                    else -> {
                        false
                    }


                }

            }


        }

    }


    private fun setEnterFragment() {

        mFragmentTransaction = mFragmentManager.beginTransaction()
        var mEnterQuizFragment: Fragment = EnterQuizFragment.newInstance("", "")
        mFragmentTransaction.replace(R.id.fragmentContainer, mEnterQuizFragment)
        mFragmentTransaction.commit()

    }


    private fun setResultFragment() {

        mFragmentTransaction = mFragmentManager.beginTransaction()
        var mResultFragment: Fragment = ResultFragment.newInstance("", "")
        mFragmentTransaction.replace(R.id.fragmentContainer, mResultFragment)
        mFragmentTransaction.commit()

    }


    private fun setToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun passData(isTakeQuiz: Boolean, isResult: Boolean) {
        isTestResult = isResult
        this.isEnterQuiz = isTakeQuiz
    }
}
