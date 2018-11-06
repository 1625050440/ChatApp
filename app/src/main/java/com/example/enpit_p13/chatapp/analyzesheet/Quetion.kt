package com.example.enpit_p13.chatapp.analyzesheet

import com.example.enpit_p13.chatapp.R.id.Q2_spinner
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_own_analysis.*
import java.sql.Timestamp
import kotlin.system.measureTimeMillis

class Quetion(
            val q1:String,
            val q2_1:String,val q2_2:Int,
            val q3:String,
            val q4:Int,
            val q5:Int,
            val q6_1:Int,val q6_2:String,
            val q7:String ) {
    constructor():this("","",1,"",3,3,3,"","")
    var timestamp = System.currentTimeMillis()
    val userId=FirebaseAuth.getInstance().uid
}
