package com.nandyala.quizkotlinnandyala.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nandyala.quizkotlinnandyala.R
import com.nandyala.quizkotlinnandyala.model.Quiz
import kotlinx.android.synthetic.main.fragment_quiz.*

//var myList: MutableList<Int> = mutableListOf<Int>()
class QuizViewModel(application: Application) : AndroidViewModel(application) {
    var mQuizList: MutableLiveData<MutableList<Quiz>> = MutableLiveData()
    var mListOfQuestion: MutableList<Quiz> = mutableListOf<Quiz>()
    fun initQuizInstance() {
        var mQuiz = mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question1),
                getApplication<Application>().getString(R.string.a),
                getApplication<Application>().getString(R.string.b),
                getApplication<Application>().getString(R.string.c),
                getApplication<Application>().getString(R.string.d),
                getApplication<Application>().getString(R.string.correct1)
            )
        );

        mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question2),
                getApplication<Application>().getString(R.string.a2),
                getApplication<Application>().getString(R.string.b2),
                getApplication<Application>().getString(R.string.c2),
                getApplication<Application>().getString(R.string.d2),
                getApplication<Application>().getString(R.string.correct2)
            )
        );

        mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question3),
                getApplication<Application>().getString(R.string.a3),
                getApplication<Application>().getString(R.string.b3),
                getApplication<Application>().getString(R.string.c4),
                getApplication<Application>().getString(R.string.d4),
                getApplication<Application>().getString(R.string.correct3)
            )
        );
        mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question4),
                getApplication<Application>().getString(R.string.a4),
                getApplication<Application>().getString(R.string.b4),
                getApplication<Application>().getString(R.string.c4),
                getApplication<Application>().getString(R.string.d4),
                getApplication<Application>().getString(R.string.correct4)
            )
        );
        mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question5),
                getApplication<Application>().getString(R.string.a5),
                getApplication<Application>().getString(R.string.b5),
                getApplication<Application>().getString(R.string.c5),
                getApplication<Application>().getString(R.string.d5),
                getApplication<Application>().getString(R.string.correct5)
            )
        );
        mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question6),
                getApplication<Application>().getString(R.string.a6),
                getApplication<Application>().getString(R.string.b6),
                getApplication<Application>().getString(R.string.c6),
                getApplication<Application>().getString(R.string.d6),
                getApplication<Application>().getString(R.string.correct6)
            )
        );
        mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question7),
                getApplication<Application>().getString(R.string.a7),
                getApplication<Application>().getString(R.string.b7),
                getApplication<Application>().getString(R.string.c7),
                getApplication<Application>().getString(R.string.d7),
                getApplication<Application>().getString(R.string.correct7)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question8),
                getApplication<Application>().getString(R.string.a8),
                getApplication<Application>().getString(R.string.b8),
                getApplication<Application>().getString(R.string.c8),
                getApplication<Application>().getString(R.string.d8),
                getApplication<Application>().getString(R.string.correct8)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question9),
                getApplication<Application>().getString(R.string.a9),
                getApplication<Application>().getString(R.string.b9),
                getApplication<Application>().getString(R.string.c9),
                getApplication<Application>().getString(R.string.d9),
                getApplication<Application>().getString(R.string.correct9)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question10),
                getApplication<Application>().getString(R.string.a10),
                getApplication<Application>().getString(R.string.b10),
                getApplication<Application>().getString(R.string.c10),
                getApplication<Application>().getString(R.string.d10),
                getApplication<Application>().getString(R.string.correct10)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question11),
                getApplication<Application>().getString(R.string.a11),
                getApplication<Application>().getString(R.string.b11),
                getApplication<Application>().getString(R.string.c11),
                getApplication<Application>().getString(R.string.d11),
                getApplication<Application>().getString(R.string.correct11)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question12),
                getApplication<Application>().getString(R.string.a12),
                getApplication<Application>().getString(R.string.b12),
                getApplication<Application>().getString(R.string.c12),
                getApplication<Application>().getString(R.string.d12),
                getApplication<Application>().getString(R.string.correct12)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question13),
                getApplication<Application>().getString(R.string.a13),
                getApplication<Application>().getString(R.string.b13),
                getApplication<Application>().getString(R.string.c13),
                getApplication<Application>().getString(R.string.d13),
                getApplication<Application>().getString(R.string.correct13)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question14),
                getApplication<Application>().getString(R.string.a14),
                getApplication<Application>().getString(R.string.b14),
                getApplication<Application>().getString(R.string.c14),
                getApplication<Application>().getString(R.string.d14),
                getApplication<Application>().getString(R.string.correct14)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question15),
                getApplication<Application>().getString(R.string.a15),
                getApplication<Application>().getString(R.string.b15),
                getApplication<Application>().getString(R.string.c15),
                getApplication<Application>().getString(R.string.d15),
                getApplication<Application>().getString(R.string.correct15)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question16),
                getApplication<Application>().getString(R.string.a16),
                getApplication<Application>().getString(R.string.b16),
                getApplication<Application>().getString(R.string.c16),
                getApplication<Application>().getString(R.string.d16),
                getApplication<Application>().getString(R.string.correct16)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question17),
                getApplication<Application>().getString(R.string.a17),
                getApplication<Application>().getString(R.string.b17),
                getApplication<Application>().getString(R.string.c17),
                getApplication<Application>().getString(R.string.d17),
                getApplication<Application>().getString(R.string.correct17)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question18),
                getApplication<Application>().getString(R.string.a18),
                getApplication<Application>().getString(R.string.b18),
                getApplication<Application>().getString(R.string.c18),
                getApplication<Application>().getString(R.string.d18),
                getApplication<Application>().getString(R.string.correct18)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question19),
                getApplication<Application>().getString(R.string.a19),
                getApplication<Application>().getString(R.string.b19),
                getApplication<Application>().getString(R.string.c19),
                getApplication<Application>().getString(R.string.d19),
                getApplication<Application>().getString(R.string.correct19)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question20),
                getApplication<Application>().getString(R.string.a20),
                getApplication<Application>().getString(R.string.b20),
                getApplication<Application>().getString(R.string.c20),
                getApplication<Application>().getString(R.string.d20),
                getApplication<Application>().getString(R.string.correct20)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question21),
                getApplication<Application>().getString(R.string.a21),
                getApplication<Application>().getString(R.string.b21),
                getApplication<Application>().getString(R.string.c21),
                getApplication<Application>().getString(R.string.d21),
                getApplication<Application>().getString(R.string.correct21)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question22),
                getApplication<Application>().getString(R.string.a22),
                getApplication<Application>().getString(R.string.b22),
                getApplication<Application>().getString(R.string.c22),
                getApplication<Application>().getString(R.string.d22),
                getApplication<Application>().getString(R.string.correct22)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question23),
                getApplication<Application>().getString(R.string.a23),
                getApplication<Application>().getString(R.string.b23),
                getApplication<Application>().getString(R.string.c23),
                getApplication<Application>().getString(R.string.d23),
                getApplication<Application>().getString(R.string.correct23)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question24),
                getApplication<Application>().getString(R.string.a24),
                getApplication<Application>().getString(R.string.b24),
                getApplication<Application>().getString(R.string.c24),
                getApplication<Application>().getString(R.string.d24),
                getApplication<Application>().getString(R.string.correct24)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question25),
                getApplication<Application>().getString(R.string.a25),
                getApplication<Application>().getString(R.string.b25),
                getApplication<Application>().getString(R.string.c25),
                getApplication<Application>().getString(R.string.d25),
                getApplication<Application>().getString(R.string.correct25)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question26),
                getApplication<Application>().getString(R.string.a26),
                getApplication<Application>().getString(R.string.b26),
                getApplication<Application>().getString(R.string.c26),
                getApplication<Application>().getString(R.string.d26),
                getApplication<Application>().getString(R.string.correct26)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question27),
                getApplication<Application>().getString(R.string.a27),
                getApplication<Application>().getString(R.string.b27),
                getApplication<Application>().getString(R.string.c27),
                getApplication<Application>().getString(R.string.d27),
                getApplication<Application>().getString(R.string.correct27)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question28),
                getApplication<Application>().getString(R.string.a28),
                getApplication<Application>().getString(R.string.b28),
                getApplication<Application>().getString(R.string.c28),
                getApplication<Application>().getString(R.string.d28),
                getApplication<Application>().getString(R.string.correct28)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question29),
                getApplication<Application>().getString(R.string.a29),
                getApplication<Application>().getString(R.string.b29),
                getApplication<Application>().getString(R.string.c29),
                getApplication<Application>().getString(R.string.d29),
                getApplication<Application>().getString(R.string.correct29)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question30),
                getApplication<Application>().getString(R.string.a30),
                getApplication<Application>().getString(R.string.b30),
                getApplication<Application>().getString(R.string.c30),
                getApplication<Application>().getString(R.string.d30),
                getApplication<Application>().getString(R.string.correct30)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question31),
                getApplication<Application>().getString(R.string.a31),
                getApplication<Application>().getString(R.string.b31),
                getApplication<Application>().getString(R.string.c31),
                getApplication<Application>().getString(R.string.d31),
                getApplication<Application>().getString(R.string.correct31)
            )
        );       mListOfQuestion.add(
            Quiz(
                getApplication<Application>().getString(R.string.question32),
                getApplication<Application>().getString(R.string.a32),
                getApplication<Application>().getString(R.string.b32),
                getApplication<Application>().getString(R.string.c32),
                getApplication<Application>().getString(R.string.d32),
                getApplication<Application>().getString(R.string.correct32)
            )
        );
    }

    fun getQuizList(): LiveData<MutableList<Quiz>> {

        mQuizList.postValue(mListOfQuestion)

        return mQuizList
    }


}