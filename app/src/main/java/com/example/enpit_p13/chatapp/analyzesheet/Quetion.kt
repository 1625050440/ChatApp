package com.example.enpit_p13.chatapp.analyzesheet

import com.example.enpit_p13.chatapp.R.id.Q2_spinner
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_own_analysis.*
import java.sql.Timestamp
import kotlin.system.measureTimeMillis

class Quetion(
        //q5,q6_1の値は、ラジオボタンの状態であり、0の時いいえボタン、1の時はいボタン、
        //-1(初期値)の時、どちらのボタンも押されていない状態を示す。
        //q8,q2の値は、スピナーの位置を示す。
            val q1:String,
            val q2_1:String,val q2_2:Int,
            val q3:String,
            val q5:Int,
            val q6_1:Int,val q6_2:String,
            val q7:String,
            val q8_1:String, val q8_2:Int, val q8_3:String,
            val q9:String){
    constructor():this("","",1,"",-1,-1,"","","",1,"","")
    var timestamp = System.currentTimeMillis()
    val userId=FirebaseAuth.getInstance().uid
}
