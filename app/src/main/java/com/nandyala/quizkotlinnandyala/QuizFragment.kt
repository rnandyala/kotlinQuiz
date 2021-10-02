package com.nandyala.quizkotlinnandyala

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Contacts
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nandyala.quizkotlinnandyala.model.Quiz
import com.nandyala.quizkotlinnandyala.model.QuizResult
import com.nandyala.quizkotlinnandyala.util.ConnectivityManager
import com.nandyala.quizkotlinnandyala.viewmodel.QuizViewModel
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "QuizFragment"
private const val SCORE = "Score"
private const val DATE = "Date"
private const val UUID = "uuid"

class QuizFragment : Fragment() {


    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mFirebaseAuthentication: FirebaseAuth
    private lateinit var mQuizGroup: RadioGroup
    private lateinit var mFragment: ResultFragment
    private lateinit var mFragmentTransaction: FragmentTransaction
    private lateinit var mFragmentManager: FragmentManager
    lateinit var mResults: MaterialButton
    lateinit var mView: View
    lateinit var mCurrentDateTime: String
    private var isInternet: Boolean? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mNavigationView: NavigationView
    private lateinit var mQuizViewModel: QuizViewModel
    private lateinit var mQuestion: MaterialTextView
    private lateinit var mOption1: MaterialRadioButton
    private lateinit var mOption2: MaterialRadioButton
    private lateinit var mOption3: MaterialRadioButton
    private lateinit var mOption4: MaterialRadioButton
    private lateinit var mNext: MaterialButton
    private var mCounter: Int = 0
    private var mScore: Int = 0
    private lateinit var mTimeinSeconds: MaterialTextView
    private lateinit var mTotalQuestions: MaterialTextView
    private lateinit var mPresentQuestion: MaterialTextView
    private lateinit var timer: CountDownTimer
    private lateinit var mMutableQuizList: MutableList<Quiz>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ConnectivityManager.registerNetworkCallback(this.context)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            //  private lateinit var mDatabase: FirebaseFirestore
            //private lateinit var mFirebaseAuthentication:FirebaseAuth

            mDatabase = FirebaseInstance.getFirebaseFireStore()
            mFirebaseAuthentication = FirebaseInstance.getFirebaseAuth()

            ConnectivityManager.observe(this, {

                updateUser(it)

            })

        }
    }

    private fun updateUser(isInternet: Boolean) {
        this.isInternet = isInternet
        if (!isInternet) {
            Snackbar.make(
                mRootlayout_quiz,
                "Internet is Required to save quiz result",
                Snackbar.LENGTH_LONG
            ).show()


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mQuizViewModel =
            ViewModelProvider(this).get(QuizViewModel::class.java)

        mView = inflater.inflate(R.layout.fragment_quiz, container, false)
        mQuizViewModel.initQuizInstance()
        setView()
        GlobalScope.launch(Dispatchers.Main) { }
        return mView
    }

    private fun setView() {
        //  visibility of toolbar to gone
        activity?.findViewById<Toolbar>(R.id.mEnterQuizToolbar)?.visibility = View.GONE
        activity?.findViewById<NavigationView>(R.id.mNavigationView)?.visibility = View.GONE
        mResults = mView.findViewById<MaterialButton>(R.id.mResults)

        mQuestion = mView.findViewById(R.id.mQuestion)
        mOption1 = mView.findViewById(R.id.option1)
        mOption2 = mView.findViewById(R.id.option2)
        mOption3 = mView.findViewById(R.id.option3)
        mOption4 = mView.findViewById(R.id.option4)
        mTimeinSeconds = mView.findViewById(R.id.mTimeInSeconds)
        mTotalQuestions = mView.findViewById(R.id.mTotalQuestions)
        mPresentQuestion = mView.findViewById(R.id.mPresentQuestion)
        mQuizGroup = mView.findViewById(R.id.mListOfOptions)
        mNext = mView.findViewById(R.id.mGoToNext)


    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNext.visibility = View.VISIBLE

        mNext.setOnClickListener {
            if (getSelectedOption() != -1) {
                // Toast.makeText(context, getSelectedOption().toString(), Toast.LENGTH_LONG).show()

                if (getSelectedOption() == (mMutableQuizList.get(mCounter).mCorrect).toInt()) {
                    mScore++
                    Toast.makeText(
                        activity,
                        "your current score is:: " + mScore.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
                mCounter++
                loadQuestion()


            } else {
                Snackbar.make(view, "Please select an option", Snackbar.LENGTH_SHORT).show()
            }
        }








        mResults.setOnClickListener {

            mFragment = ResultFragment.newInstance("", "");
            mFragmentManager = activity?.supportFragmentManager!!
            mFragmentTransaction =
                mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, mFragment)
            mFragmentTransaction.commit()

        }

        mQuizViewModel.getQuizList().observe(viewLifecycleOwner, Observer {
            mMutableQuizList = it

            mQuestion.setText(
                HtmlCompat.fromHtml(
                    mMutableQuizList.get(mCounter).mQuestion,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            )
            mOption1.setText(
                HtmlCompat.fromHtml(
                    mMutableQuizList.get(mCounter).mOption1,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            )
            mOption2.setText(
                HtmlCompat.fromHtml(
                    mMutableQuizList.get(mCounter).mOption2,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            )
            mOption3.setText(
                HtmlCompat.fromHtml(
                    mMutableQuizList.get(mCounter).mOption3,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mOption4.setText(
                    Html.fromHtml(
                        mMutableQuizList.get(mCounter).mOption4,
                        Html.FROM_HTML_MODE_LEGACY
                    )
                )
            } else {
                mOption4.setText(
                    Html.fromHtml(
                        mMutableQuizList.get(mCounter).mOption4
                    )
                )

            }

            mTotalQuestions.setText("/" + it.size)
            var mPresentQuestionCounter = mCounter
            ++mPresentQuestionCounter
            mPresentQuestion.setText(mPresentQuestionCounter.toString())
        })

        // assign as well as start timer
        setCountDownTimer()


    }

    private fun setCountDownTimer() {
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeinSeconds.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                if (getSelectedOption() != -1) {
                    // Toast.makeText(context, getSelectedOption().toString(), Toast.LENGTH_LONG).show()
                    if (getSelectedOption() == (mMutableQuizList.get(mCounter).mCorrect).toInt()) {
                        mScore++

                      /*
                        Toast.makeText(
                            activity,
                            "your current score is:: " + mScore.toString(),
                            Toast.LENGTH_LONG
                        ).show()*/
                    }
                    mCounter++
                    loadQuestion()


                } else {
                    Toast.makeText(activity, "time is up buddy", Toast.LENGTH_LONG)
                        .show()
                    mCounter++
                    loadQuestion()
                }


            }

        }
        timer.start()

    }

    private fun loadQuestion() {
        if (mCounter < mMutableQuizList.size) {
            mQuestion.setText(
                HtmlCompat.fromHtml(
                    mMutableQuizList.get(mCounter).mQuestion,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            )
            mOption1.setText(
                HtmlCompat.fromHtml(
                    mMutableQuizList.get(mCounter).mOption1,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            )
            mOption2.setText(
                HtmlCompat.fromHtml(
                    mMutableQuizList.get(mCounter).mOption2,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            )
            mOption3.setText(
                HtmlCompat.fromHtml(
                    mMutableQuizList.get(mCounter).mOption3,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            )

            mOption4.setText(
                HtmlCompat.fromHtml(
                    mMutableQuizList.get(mCounter).mOption4,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            )

            var mPresentQuestionCounter = mCounter
            ++mPresentQuestionCounter

            mPresentQuestion.setText(mPresentQuestionCounter.toString())
            timer.cancel()
            timer.start()

        } else {
            timer.cancel()
            setDateTime()
            saveData()


        }
    }

    private fun saveData() {

        mNext.visibility = View.GONE
        var mQuizResult: QuizResult = QuizResult()
        mQuizResult.mDate = mCurrentDateTime
        mQuizResult.mScore = mScore.toString()
        mQuizResult.mUUID = mFirebaseAuthentication.uid.toString()

        if (isInternet!!) {
            mDatabase.collection("Quiz Result").document().set(mQuizResult).addOnSuccessListener {

                //   Snackbar.make(mView, "successfully saved data", Snackbar.LENGTH_SHORT).show()

                setResultFragment()
                Toast.makeText(activity, "Great quiz completed", Toast.LENGTH_LONG)
                    .show()

                Log.i(TAG, "data saved::" + mQuizResult.toString())


            }.addOnFailureListener {
                it.message
                setResultFragment()

                Toast.makeText(activity, "Great quiz completed", Toast.LENGTH_LONG)
                    .show()


                Snackbar.make(mView, "error data not saved: " + it.message, Snackbar.LENGTH_SHORT)
                    .show()

                Log.e(TAG, "error data not saved" + it.message)
            }
        } else {
            setResultFragment()

            Snackbar.make(
                mView,
                "internet error data not saved but quiz is complete",
                Snackbar.LENGTH_SHORT
            )
                .show()


        }

    }

    fun setResultFragment() {

        var mQuizData: Bundle = Bundle()
        mQuizData.putBoolean("takeQuiz", false)
        mQuizData.putBoolean("resultQuiz", true)

        mFragment = ResultFragment.newInstance("", "")
        mFragment.arguments = mQuizData
        mFragmentManager = activity?.supportFragmentManager!!
        mFragmentTransaction =
            mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, mFragment)
        mFragmentTransaction.commit()
    }


    private fun setOrSaveDate() {
        val mQuizResults: HashMap<String, String> = HashMap()
        mQuizResults.put(SCORE, mScore.toString())
        mQuizResults.put(DATE, mCurrentDateTime)
        mQuizResults.put(UUID, mFirebaseAuthentication.uid.toString())
        mDatabase.collection("Quiz Result").document().set(mQuizResults).addOnSuccessListener {
            Log.d(TAG, "success")
        }.addOnFailureListener {
            Log.e(TAG, it.message.toString())
        }
    }

    private fun setDateTime() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mCurrentDateTime = LocalDateTime.now()
            var mFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

            this.mCurrentDateTime =
                mCurrentDateTime.format(mFormatter).toString().replace(" ", "\n")


        } else {

            val mDate = Date()
            val mFormatter = SimpleDateFormat("dd/MMM/yyy HH:mm", Locale.US)
            this.mCurrentDateTime = mFormatter.format(mDate).toString().replace(" ", "\n")

        }


    }

    private fun getSelectedOption(): Int {
        var radioButtonId = mQuizGroup.checkedRadioButtonId
        if (radioButtonId != -1) {

            var radioButton: View = mQuizGroup.findViewById(radioButtonId)
            var index: Int = mQuizGroup.indexOfChild(radioButton)
            return ++index
        } else {
            return radioButtonId
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        //      ConnectivityManager.registerNetworkCallback(this.context)

    }

    override fun onStop() {
        super.onStop()
//        com.nandyala.quizkotlinnandyala.util.ConnectivityManager.unRegisterNetworkCallback(this.context)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        //com.nandyala.quizkotlinnandyala.util.ConnectivityManager.unRegisterNetworkCallback(this.context)
    }
}