package com.nandyala.quizkotlinnandyala.resultAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nandyala.quizkotlinnandyala.R
import com.nandyala.quizkotlinnandyala.model.QuizResult
import com.google.android.material.textview.MaterialTextView

class ResultAdapter(var mQuizResult: List<QuizResult>) :
    RecyclerView.Adapter<ResultAdapter.ResultsViewHolder>() {


    class ResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mScore: MaterialTextView
        val mDate: MaterialTextView
        init {
            mScore = view.findViewById<MaterialTextView>(R.id.mtotalscore)
            mDate = view.findViewById<MaterialTextView>(R.id.mDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.testresult_container, parent, false)
        return ResultsViewHolder(view)

    }

    override fun getItemCount(): Int {
        return mQuizResult.size
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        holder.mScore.text = mQuizResult.get(position).mScore
        holder.mDate.text = mQuizResult.get(position).mDate

    }
}