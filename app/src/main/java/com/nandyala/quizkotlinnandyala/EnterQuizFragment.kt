package com.nandyala.quizkotlinnandyala

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.google.type.Date
import com.google.type.DateOrBuilder
import kotlinx.android.synthetic.main.fragment_enter_quiz.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EnterQuiz.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterQuizFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mGo: MaterialButton
    lateinit var mInstruction: MaterialTextView

    lateinit var mView: View


    lateinit var mFragment: Fragment
    lateinit var mFragmentTransaction: FragmentTransaction
    lateinit var mFragmentManager: FragmentManager

    lateinit var mNavigationview: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_enter_quiz, container, false)
        setView(mView)
        setInstructions()
        setGo()
        return mView
    }


    private fun setInstructions() {

//if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        mInstruction.setText(
            HtmlCompat.fromHtml(
                resources.getString(R.string.instructions),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        )
//}
        //  else{
        //mEnterQuiz.setText(HtmlCompat.fromHtml("", HtmlCompat.FROM_HTML_MODE_COMPACT))
        //}

    }

    private fun setGo() {
        mGo.setOnClickListener {
            mFragment = QuizFragment.newInstance("", "");
            mFragmentTransaction =
                mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, mFragment)
            mFragmentTransaction.commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // mFragment = QuizFragment.newInstance("", "")
        mFragmentManager = activity?.supportFragmentManager!!
       // mNavigationview.menu.findItem(R.id.take_quiz).setVisible(false)
        //mNavigationview.menu.findItem(R.id.test_results).setVisible(true)
    }


    private fun setView(mView: View?) {
        mNavigationview = activity?.findViewById<NavigationView>(R.id.mNavigationView)!!
        mGo = mView?.findViewById(R.id.mGo) as MaterialButton;
        mInstruction = mView?.findViewById(R.id.mEnterQuiz) as MaterialTextView

        /*
        Snackbar
    .make(mView, Date.,Snackbar.LENGTH_LONG)
*/
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EnterQuiz.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EnterQuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}