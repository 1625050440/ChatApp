package com.example.enpit_p13.chatapp.quetion

import java.sql.Date
import java.text.SimpleDateFormat

class Questionnaire(
        val q1:String,
        val q2:String,
        val q3:String
) {
    constructor():this("none","","")
    val date = SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(Date(System.currentTimeMillis()))
}