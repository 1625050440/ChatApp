package com.example.enpit_p13.chatapp.analyzesheet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.enpit_p13.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_look_back_analyze.*
import kotlinx.android.synthetic.main.content_look_back_analyze.*
import java.sql.Date
import java.text.SimpleDateFormat

class LookBackAnalyzeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_look_back_analyze)
        setSupportActionBar(toolbar)

        readAnalyze()
    }

    fun readAnalyze(){
        val intent = intent
        var timedata = intent.getLongExtra(SelectOwnAnalyzeActivity.TIME_KEY,0)
        Log.d("time",SimpleDateFormat("yyyy年MM月dd日 hh時mm分").format(Date(timedata)))
        val ref = FirebaseDatabase.getInstance().reference.child("AnalyzeSheet/${FirebaseAuth.getInstance().uid}/$timedata")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("readRef",p0.key)
                val read_quetion = p0.getValue<Question>(Question::class.java)
                ReadQuetion(read_quetion!!)
            }

        })

        Q1_edit_num_lookback.isEnabled = false
        Q2_edit_num_loockback.isEnabled = false
        Q2_spinner_loockback.isEnabled = false
        Q3_edit_num_loockback.isEnabled = false
        Q5_yesButtom_loockback.isEnabled = false
        Q5_noButtom_loockback.isEnabled = false
        Q6_edit_detail_loockback.isEnabled = false
        Q6_noButtom_loockback.isEnabled = false
        Q6_yesButtom_loockback.isEnabled = false
        Q7_edit_text_loockback.isEnabled = false
        Q8Edit_detail_loockback.isEnabled = false
        Q9_edit_detail_lookback.isEnabled = false

    }

    fun ReadQuetion(question: Question){
        Q1_edit_num_lookback.setText(question.q1)
        Q2_edit_num_loockback.setText(question.q2_1)
        Q2_spinner_loockback.setSelection(question.q2_2)
        Q3_edit_num_loockback.setText(question.q3)

        when {
            question.q5 == 0 -> {
                Q5_yesButtom_loockback.isChecked = false
                Q5_noButtom_loockback.isChecked = true
            }
            question.q5 == 1 -> {
                Q5_yesButtom_loockback.isChecked = true
                Q5_noButtom_loockback.isChecked = false
            }
            else -> {
                Q5_yesButtom_loockback.isChecked = false
                Q5_noButtom_loockback.isChecked = false
            }
        }

        when {
            question.q6_1 == 0 -> {
                Q6_yesButtom_loockback.isChecked = false
                Q6_noButtom_loockback.isChecked = true
            }
            question.q6_1 == 1 -> {
                Q6_yesButtom_loockback.isChecked = true
                Q6_noButtom_loockback.isChecked = false
            }
            else -> {
                Q6_yesButtom_loockback.isChecked = false
                Q6_noButtom_loockback.isChecked = false
            }
        }

        Q6_edit_detail_loockback.setText(question.q6_2)
        Q7_edit_text_loockback.setText(question.q7)
        Q8Edit_num_loockback.setText(question.q8_1)
        Q8_spinner_loockback.setSelection(question.q8_2)
        Q8Edit_detail_loockback.setText(question.q8_3)
        Q9_edit_detail_lookback.setText(question.q9)
    }

}