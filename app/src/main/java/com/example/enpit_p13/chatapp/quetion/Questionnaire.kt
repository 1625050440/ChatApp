package com.example.enpit_p13.chatapp.quetion

import java.sql.Date
import java.text.SimpleDateFormat

class Questionnaire(
        val q1:Int,
        val q2:String,
        val q3:String
) {
    //q1は、ラジオボタンの結果で０が"いいえ"、１が"はい"と回答
    constructor():this(0,"","")
    val date = SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(Date(System.currentTimeMillis()))
}