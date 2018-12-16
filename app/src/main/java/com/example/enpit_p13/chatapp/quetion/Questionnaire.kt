package com.example.enpit_p13.chatapp.quetion

import java.sql.Date
import java.text.SimpleDateFormat

class Questionnaire(
        val situmon1:String,
        val situmon2:String,
        val situmon3:String,
        val situmon4:String
) {
    constructor():this("none","","","")
    val date = SimpleDateFormat("yyyy/MM/dd aahh:mm:ss").format(Date(System.currentTimeMillis()))
}