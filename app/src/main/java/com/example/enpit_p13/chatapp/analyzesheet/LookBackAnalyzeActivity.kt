package com.example.enpit_p13.chatapp.analyzesheet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.text.InputType
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

        ReadAnalyze()
    }

    fun ReadAnalyze(){
        val intent = getIntent()
        var timedata = intent.getLongExtra(SelectOwnAnalyzeActivity.TIME_KEY,0)
        Log.d("gettime",SimpleDateFormat("yyyy年MM月dd日 hh時mm分").format(Date(timedata)))
        val ref = FirebaseDatabase.getInstance().getReference().child("AnalyzeSheet/${FirebaseAuth.getInstance().uid}/${timedata.toString()}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("readRef",p0.key)
                val read_quetion = p0.getValue<Quetion>(Quetion::class.java)
                ReadQuetion(read_quetion!!)
            }

        })

        Q1_edit_num_lookback.setInputType(InputType.TYPE_NULL)
        Q2_edit_num_loockback.setEnabled(false)
        Q2_spinner_loockback.setEnabled(false)
        Q3_edit_num_loockback.setEnabled(false)
        Q5_yesButtom_loockback.setEnabled(false)
        Q5_noButtom_loockback.setEnabled(false)
        Q6_edit_detail_loockback.setEnabled(false)
        Q6_noButtom_loockback.setEnabled(false)
        Q6_yesButtom_loockback.setEnabled(false)
        Q7_edit_text_loockback.setEnabled(false)
        Q8Edit_detail_loockback.setEnabled(false)
        Q9_edit_detail_lookback.setEnabled(false)

    }

    fun ReadQuetion(quetion: Quetion){
        Q1_edit_num_lookback.setText(quetion.q1)
        Q2_edit_num_loockback.setText(quetion.q2_1)
        Q2_spinner_loockback.setSelection(quetion.q2_2)
        Q3_edit_num_loockback.setText(quetion.q3)

        when {
            quetion.q5 == 0 -> {
                Q5_yesButtom_loockback.isChecked = false
                Q5_noButtom_loockback.isChecked = true
            }
            quetion.q5 == 1 -> {
                Q5_yesButtom_loockback.isChecked = true
                Q5_noButtom_loockback.isChecked = false
            }
            else -> {
                Q5_yesButtom_loockback.isChecked = false
                Q5_noButtom_loockback.isChecked = false
            }
        }

        when {
            quetion.q6_1 == 0 -> {
                Q6_yesButtom_loockback.isChecked = false
                Q6_noButtom_loockback.isChecked = true
            }
            quetion.q6_1 == 1 -> {
                Q6_yesButtom_loockback.isChecked = true
                Q6_noButtom_loockback.isChecked = false
            }
            else -> {
                Q6_yesButtom_loockback.isChecked = false
                Q6_noButtom_loockback.isChecked = false
            }
        }

        Q6_edit_detail_loockback.setText(quetion.q6_2)
        Q7_edit_text_loockback.setText(quetion.q7)
        Q8Edit_num_loockback.setText(quetion.q8_1)
        Q8_spinner_loockback.setSelection(quetion.q8_2)
        Q8Edit_detail_loockback.setText(quetion.q8_3)
        Q9_edit_detail_lookback.setText(quetion.q9)
    }

}
