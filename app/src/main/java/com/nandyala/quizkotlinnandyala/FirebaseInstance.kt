package com.nandyala.quizkotlinnandyala

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object  FirebaseInstance {

    var mFirebaseAuth: FirebaseAuth? = null
    var mFireStore: FirebaseFirestore? = null


    fun getFirebaseAuth(): FirebaseAuth {
        if (mFirebaseAuth == null) {
            mFirebaseAuth = FirebaseAuth.getInstance()
            return mFirebaseAuth!!
        } else {
            return mFirebaseAuth!!
        }
    }

    fun getFirebaseFireStore(): FirebaseFirestore {

        if (mFireStore == null) {
            mFireStore = FirebaseFirestore.getInstance()
            return mFireStore!!
        } else {
            return mFireStore!!
        }
    }


}