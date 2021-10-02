package com.nandyala.quizkotlinnandyala

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.nandyala.quizkotlinnandyala.model.QuizResult
import com.nandyala.quizkotlinnandyala.resultAdapter.ResultAdapter
import com.nandyala.quizkotlinnandyala.util.ConnectivityManager
import com.nandyala.quizkotlinnandyala.util.IBundle
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.fragment_result.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val SCORE = "Score"
private const val DATE = "Date"

private const val TAG = "ResultFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [result.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {


    /*
            mFragment = QuizFragment.newInstance("", "");
            mFragmentTransaction =
                mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, mFragment)
            mFragmentTransaction.commit()

// Create an executor that executes tasks in the main thread.
val mainExecutor: Executor = ContextCompat.getMainExecutor(this)

// Create an executor that executes tasks in a background thread.
val backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

     */
    private lateinit var mProgressbar: AVLoadingIndicatorView
    private lateinit var mUiexecutor: Executor
    private lateinit var mBackgroundExecutor: java.util.concurrent.ExecutorService


    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mNavigationView: NavigationView
    private lateinit var mQuizList: List<QuizResult>
    private lateinit var mFragment: Fragment
    private lateinit var mFragmentTransaction: FragmentTransaction
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mIBundle: IBundle

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mRecyclerView: RecyclerView
    lateinit var mFloatingActionButton: FloatingActionButton
    lateinit var mView: View
    lateinit var mConstraintLayout: CoordinatorLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mDatabase = FirebaseInstance.getFirebaseFireStore()

        getNetworkStatus()

        mUiexecutor = ContextCompat.getMainExecutor(this.context)
        mBackgroundExecutor = Executors.newFixedThreadPool(2)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mIBundle = context as IBundle

    }

    private fun getNetworkStatus() {
        ConnectivityManager.observe(this, {
            updateUser(it)
        })
    }

    private fun updateUser(isInternet: Boolean) {
        if (!isInternet) {

            Snackbar.make(
                mRootlayout,
                "Internet is required to show the quiz results",
                Snackbar.LENGTH_LONG
            ).show()

        }
    }

    override fun onStart() {
        super.onStart()
        // ConnectivityManager.registerNetworkCallback(this.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_result, container, false)
        setView(mView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAnimation()
        mRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.visibility = View.GONE
        getData()
        mQuizList = ArrayList<QuizResult>()

        mFloatingActionButton.setOnClickListener { deleteData() }

        mIBundle.passData(false, true)

    }

    private fun deleteData() {
        mRecyclerView.visibility = View.GONE
        mProgressbar.show()
        mDatabase.collection("Quiz Result")
            .whereEqualTo("muuid", FirebaseInstance.getFirebaseAuth().uid)
            .get(Source.SERVER)
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (mDocumentSnapshot in it) {
                        mDocumentSnapshot.reference.delete()
                    }
                    mQuizList = ArrayList<QuizResult>()
                    mRecyclerView.adapter = ResultAdapter(mQuizList)
                    mRecyclerView.adapter!!.notifyDataSetChanged()
                    mProgressbar.hide()
                    mRecyclerView.visibility = View.VISIBLE
                    Snackbar.make(mView, "data deleted", Snackbar.LENGTH_LONG).show()
                    Log.i(
                        TAG,
                        "entire collection is deleted from database for:: " + FirebaseInstance.getFirebaseAuth().uid.toString()
                    )

                } else {

                    mProgressbar.hide()
                }


            }

            .addOnFailureListener {
                Log.e(TAG, it.message.toString())

            }
    }

    private fun getData() {
        mProgressbar.show()
        //    .orderBy("mscore", com.google.firebase.firestore.Query.Direction.DESCENDING)
        mDatabase.collection("Quiz Result")
            .whereEqualTo("muuid", FirebaseInstance.getFirebaseAuth().uid)

            .get(Source.SERVER)
            .addOnSuccessListener {

                if (!it.isEmpty) {

                    mDatabase.collection("Quiz Result")
                        .orderBy("mscore", com.google.firebase.firestore.Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener {

                            for (mDocumentSnapshot in it) {
                                var mQuizResult: QuizResult

                                //private lateinit var mUiexecutor: Executor
                                // private lateinit var mBackgroundExecutor : java.util.concurrent.ExecutorService

                                mBackgroundExecutor.execute {
                                    mQuizResult = mDocumentSnapshot.toObject(QuizResult::class.java)
                                    (mQuizList as ArrayList<QuizResult>).add(mQuizResult)
                                    mUiexecutor.execute {
                                        mProgressbar.hide()
                                        mRecyclerView.visibility = View.VISIBLE
                                        mRecyclerView.adapter = ResultAdapter(mQuizList)
                                        Log.i(TAG, mQuizList.toString())
                                    }

                                }


                            }
                        }

                } else {
                    mProgressbar.hide()
                }


            }

            .addOnFailureListener {
                Log.e(TAG, it.message.toString())
                mProgressbar.hide()

            }

    }

    private fun setView(v: View) {
        mFragmentManager = activity?.supportFragmentManager!!
        mRecyclerView = v.findViewById(R.id.mTestResults)
        mFloatingActionButton = v.findViewById(R.id.mDelete)
        mConstraintLayout = v.findViewById(R.id.mRootlayout)
        mProgressbar = v.findViewById(R.id.mProgressbar)

        var mToolbar: Toolbar = activity?.findViewById(R.id.mEnterQuizToolbar) as Toolbar
        mToolbar.visibility = View.VISIBLE

        mNavigationView = activity?.findViewById(R.id.mNavigationView)!!
        mNavigationView.menu.findItem(R.id.take_quiz).setVisible(true)
        mNavigationView.menu.findItem(R.id.test_results).setChecked(true)
    }

    private fun setAnimation() {
        var mAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.fab_animation)
        mFloatingActionButton.startAnimation(mAnimation)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment result.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStop() {
        super.onStop()
        //   ConnectivityManager.unRegisterNetworkCallback(this.context)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBackgroundExecutor.shutdown()

    }
}