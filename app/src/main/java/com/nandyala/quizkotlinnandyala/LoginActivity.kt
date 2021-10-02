package com.nandyala.quizkotlinnandyala

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import com.nandyala.quizkotlinnandyala.model.QuizResult
import com.nandyala.quizkotlinnandyala.resultAdapter.ResultAdapter
import com.nandyala.quizkotlinnandyala.util.Util
import com.wang.avi.AVLoadingIndicatorView
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LoginActivity : AppCompatActivity() {

    var mAccessToken: AccessToken? = null
    val TAG = "FirebaseAuth"

    // CallbackManager for facebook
    lateinit var mCallbackManager: CallbackManager
    private lateinit var mFirebaseAuth: FirebaseAuth

    val EMAIL: String = "email"
    val PROFILE: String = "public_profile"
    val GOOGLE = 202

    var mCurrentUser: FirebaseUser? = null
    lateinit var mGoogleSigninClient: GoogleSignInClient
    lateinit var mGoogleSignIn: MaterialButton;
    lateinit var mFacebooklogin: LoginButton
    lateinit var mGoogleSignOut: Button
    lateinit var mCoordinateLayout: CoordinatorLayout
    lateinit var mFacebookSignin: MaterialButton
    var isInternet: Boolean = false
    lateinit var mProgressbar: AVLoadingIndicatorView
    lateinit var mFacebookIcon: AppCompatImageView
    lateinit var mGoogleIcon: AppCompatImageView

    var mExecutor: ExecutorService = Executors.newFixedThreadPool(4)
    lateinit var mExecuteMain: Executor
    override fun onDestroy() {
        super.onDestroy()


    }


    // I donot even need this onstart because once I get the accesstoken I will get the data immediately
    override fun onStart() {

        com.nandyala.quizkotlinnandyala.util.ConnectivityManager.registerNetworkCallback(this.applicationContext)
        super.onStart()
        /*
        if (mCurrentUser == null) {
            mFirebaseAuth.currentUser
            getCurrentUser(mFirebaseAuth.currentUser)
        } else {
            // on success callback of facebook login btn
            Log.d(TAG, "current user details are already filled")
        }*/
    }

    override fun onStop() {

        com.nandyala.quizkotlinnandyala.util.ConnectivityManager.unRegisterNetworkCallback(this.applicationContext)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mExecuteMain = ContextCompat.getMainExecutor(this)
        //   checkInternetConnection()

        mFirebaseAuth =
            FirebaseAuth.getInstance()
        mFacebooklogin = findViewById(R.id.login_button) as LoginButton
        mGoogleSignIn = findViewById<SignInButton>(R.id.sign_in_button) as MaterialButton
        mFacebookSignin = findViewById(R.id.mFacebook_login) as MaterialButton
        mCoordinateLayout =
            findViewById<CoordinatorLayout>(R.id.mSnack_login) as CoordinatorLayout
        mCallbackManager = CallbackManager.Factory.create()
        mFacebooklogin.setPermissions(Arrays.asList(PROFILE, EMAIL))
        mProgressbar = findViewById(R.id.mProgressbar) as AVLoadingIndicatorView
        mFacebookIcon = findViewById(R.id.mFacebook_icon)
        mGoogleIcon = findViewById(R.id.mGoogle_icon)
        //check internet connection

        // initialize google client before making startactivityforresult
        setGoogleClient();
        // set listeners for facebook login button I get accesstoken here
        setMFaceBookSignIn()
        // actual call to start activity for result
        setOnClickFacebook()
        setGoogleSignInListener()

        // setGoogleSignOut()
        //setNoInternetMessage()


        getNetworkStatus()


    }

    private fun setloginGone() {
        mFacebookSignin.visibility = View.GONE
        mGoogleSignIn.visibility = View.GONE
        mFacebookIcon.visibility = View.GONE
        mGoogleIcon.visibility = View.GONE


    }

    private fun setloginVisible() {
        mFacebookSignin.visibility = View.VISIBLE
        mGoogleSignIn.visibility = View.VISIBLE
        mFacebookIcon.visibility = View.VISIBLE
        mGoogleIcon.visibility = View.VISIBLE
    }

    private fun setOnClickFacebook() {

        mFacebookSignin.setOnClickListener {
            if (isInternet && Util.getIsInternetSharedPref(it.context) == false) {
                mFirebaseAuth = FirebaseAuth.getInstance()
                mFirebaseAuth.signOut()
                LoginManager.getInstance().logOut()
            //    setGoogleSignOut()
                mProgressbar.show()
                setloginGone()
                mFacebooklogin.performClick()
                mFacebooklogin.loginBehavior = LoginBehavior.DIALOG_ONLY
                mProgressbar.show()
                Util.setInternetSharedPref(this, true)

            } else if (isInternet) {
                mFacebooklogin.performClick()
                mFacebooklogin.loginBehavior = LoginBehavior.DIALOG_ONLY
                mProgressbar.show()
                setloginGone()
            } else {
                Snackbar.make(
                    mCoordinateLayout,
                    "Internet is required for facebook login",
                    Snackbar.LENGTH_LONG
                ).show()
            }


        }


    }

    private fun getNetworkStatus() {
        com.nandyala.quizkotlinnandyala.util.ConnectivityManager.observe(

            this,
            {
                updatedUser(it)
            }
            //


        )
    }

    private fun updatedUser(isInternet: Boolean) {
        this.isInternet = isInternet
        if (!isInternet) {
            Snackbar.make(
                mCoordinateLayout,
                "Internet is required to login to the quiz",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }


    private fun setNoInternetMessage() {
        var isConnected = isInternetAvailable(this)

        if (!isConnected) {
            Snackbar.make(
                this,
                mCoordinateLayout,
                "Please connect to internet or else you cannot login or enter the app",
                Snackbar.LENGTH_SHORT
            )
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun isInternetAvailable(mContext: Context): Boolean {
        var result = false
        val mConnectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = mConnectivityManager.activeNetwork ?: return false
            val mActiveNetwork =
                mConnectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            var result = when {

                mActiveNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                mActiveNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                mActiveNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false

            }
        } else {
            mConnectivityManager.run {
                mConnectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }


            }
        }

        return result

    }


    // check other ways
    private fun checkInternetConnection(): Boolean {
        val mConnectivityManager: ConnectivityManager
        val mActiveNetworkInfo: NetworkInfo
        mConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        mActiveNetworkInfo = mConnectivityManager.activeNetworkInfo!!
        return mActiveNetworkInfo != null && mActiveNetworkInfo.isConnected()
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

    private fun setMFaceBookSignIn() {


        mFacebooklogin.run {
            registerCallback(
                mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        // user successfully loggedin
                        mAccessToken = result?.accessToken
                        result?.accessToken?.isExpired

                        //       result?.accessToken?.isExpired
                        // result?.accessToken?.
                        handleFacebookAccessToken(mAccessToken)

                    }

                    override fun onCancel() {
                        Log.d(TAG, "user cancelled login for some reason")
                        mProgressbar.hide()
                        setloginVisible()

                    }

                    override fun onError(error: FacebookException?) {
                        // something went wrong try again
                        Log.d(TAG, "" + error?.message)

                    }
                }
            )
        }

        /*
          else{
            //  mFacebooklogin.run{false}
          }*/
    }

    private fun setGoogleSignInListener() {

        mGoogleSignIn.setOnClickListener {


            if (isInternet && Util.getIsInternetSharedPref(this) == false) {

                mFirebaseAuth = FirebaseAuth.getInstance()
                mFirebaseAuth.signOut()
                LoginManager.getInstance().logOut()
               setGoogleSignOut()
                mProgressbar.show()
                setloginGone()
                var mGoogleIntent: Intent = mGoogleSigninClient.signInIntent
                startActivityForResult(mGoogleIntent, GOOGLE)
            } else if (isInternet) {
                mProgressbar.show()
                setloginGone()
                var mGoogleIntent: Intent = mGoogleSigninClient.signInIntent
                startActivityForResult(mGoogleIntent, GOOGLE)
            } else {
                Snackbar.make(
                    mCoordinateLayout,
                    "Internet is required for google login",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }


    }


    private fun setGoogleClient() {
        // initialize google signin
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_google))
            .requestEmail()
            .build()

        mGoogleSigninClient = GoogleSignIn.getClient(this, gso);





// once loggedin and user is not loggedout I can use this code to retrieve loggedin user details in any activity
        // var mAccount :GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(applicationContext)


    }

    //exchange facebook accesstoken with firebase auth credentials and login
    private fun handleFacebookAccessToken(mAccesstoken: AccessToken?) {
        Log.d(TAG, "handleFacebookAccessToken:$mAccesstoken")

        Util.setLoginSharedPref(this, mAccessToken!!.token)

        val mCredential = FacebookAuthProvider.getCredential(mAccesstoken!!.token)
        mExecutor.execute {
            var mTaskResult = mFirebaseAuth.signInWithCredential(mCredential)



            mTaskResult.addOnCompleteListener(this, {

                mExecuteMain.execute {
                    if (it.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mFirebaseAuth.currentUser
                        user?.photoUrl
                        setActivity(user?.photoUrl, user?.email)
                    } else {
                        Log.w(
                            TAG,
                            "signInWithCredential:failure:: FirebaseAuthentication failed even though accesstoken present",
                            it.exception
                        )
                    }
                }
            }).addOnFailureListener(this) {

                mExecuteMain.execute {

                    com.google.android.material.snackbar.Snackbar.make(
                        mCoordinateLayout,
                        "Authentication failed facebook" + it.message.toString(),
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }

            }


/*

         //   mExecuteMain.execute {
                mTaskResult.addOnCompleteListener(this) { it ->
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        getCurrentUser(mFirebaseAuth.currentUser)
                    } else {
                        // If sign in fails, display a message to the user.// unable to retrieve details of the user
                        Log.w(
                            TAG,
                            "signInWithCredential:failure:: FirebaseAuthentication failed even though accesstoken present",
                            it.exception
                        )
                        Snackbar.make(
                            mCoordinateLayout,
                            "Authentication failed facebook" + it.exception.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener(this) {
                    Snackbar.make(
                        mCoordinateLayout,
                        "Authentication failed facebook" + it.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }*/
            // }


        }
    }


    /*
    else {

        // If sign in fails, display a message to the user.// unable to retrieve details of the user
        Log.w(TAG, "signInWithCredential:failure", it.exception)
        Toast.makeText(
            baseContext, "Authentication failed.",
            Toast.LENGTH_SHORT
        ).show()
        //updateUI(null)
    }*/


    //}


    // google sign results are obtained in onActivity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSigninResult(task);

        }
        super.onActivityResult(requestCode, resultCode, data)


    }

    // obtain accesstoken
    private fun handleSigninResult(task: Task<GoogleSignInAccount>) {
        try {

            var mGAccount: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
//mGAccount.idToken
            firebaseAuthWithGoogle(mGAccount.idToken!!)
            Log.e("GoogleAccount", "" + mGAccount)


            // updateUI (mGAccount)
        } catch (ex: Exception) {
            Log.w(TAG, "user denied to login this is user specific action not an exception")
            mProgressbar.hide()
            setloginVisible()

        }


    }

    // exchange google accesstoken with firebase auth credentials and login
    private fun firebaseAuthWithGoogle(idToken: String) {
        val mCredential = GoogleAuthProvider.getCredential(idToken, null)
        /* mBackgroundExecutor.execute {
             mQuizResult = mDocumentSnapshot.toObject(QuizResult::class.java)
             (mQuizList as ArrayList<QuizResult>).add(mQuizResult)
             mUiexecutor.execute {
                 mProgressbar.hide()
                 mRecyclerView.visibility = View.VISIBLE
                 mRecyclerView.adapter = ResultAdapter(mQuizList)
                 Log.i(TAG, mQuizList.toString())
             }

         }*/


        mExecutor.execute {

            var mTaskAuthResult = mFirebaseAuth.signInWithCredential(mCredential)
            mExecuteMain.execute {
                mTaskAuthResult.addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Log.d("SignIn", "log in with credentials was successful")
                        val user = mFirebaseAuth.currentUser
                        user?.photoUrl
                        setActivity(user?.photoUrl, user?.email)

                    } else {

                        mProgressbar.hide()
                        setloginVisible()
                        Log.d(
                            TAG,
                            "signInWithCredential:failure:: FirebaseAuthentication failed even though accesstoken present",
                            it.exception
                        )
                    }

                }.addOnFailureListener({
                    it.message
                    mProgressbar.hide()
                    setloginVisible()
                    Snackbar.make(
                        mCoordinateLayout,
                        "google signin failed::" + it.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                })
            }
        }
        //mFirebaseAuth.signInWithCredential()
    }

    // For signout I should call Firebase.auth.signOut()
// As well as signout from facebook
    private fun getCurrentUser(mCurrentUser: FirebaseUser?) {
        this.mCurrentUser = mCurrentUser
        // Sign in success, update UI with the signed-in user's information
        Log.d(TAG, "signInWithCredential:success")
        val user = mCurrentUser
        user?.email
        user?.displayName
        user?.phoneNumber
        user?.photoUrl
        user?.providerId
        user?.uid
        setActivity(user?.photoUrl, user?.email)
        // Enter QuizActivity
    }

    private fun setActivity(mPhotoUrl: Uri?, mEmail: String?) {
        mFirebaseAuth.currentUser?.getIdToken(true)?.addOnCompleteListener {

            it.result.token
        }
        var intent: Intent
        intent = Intent(this, EnterQuizActivity::class.java)
        intent.putExtra("photo", mPhotoUrl.toString())
        intent.putExtra("email", mEmail)
        intent.putExtra("enterQuiz", true)
        intent.putExtra("accessToken", mAccessToken.toString())
        startActivity(intent)
        mProgressbar.hide()
        finish()

    }

    override fun onBackPressed() {
        super.onBackPressed()


    }
}

