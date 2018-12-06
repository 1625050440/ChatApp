package com.example.enpit_p13.chatapp

import com.google.firebase.auth.FirebaseAuth

class Message_all {

    constructor() //empty for firebase



    constructor(messageText: String, usernameText:String,check:Boolean){

        text = messageText
        username = usernameText
        room_ask = check

    }

    var text: String? = null

    var timestamp: Long = System.currentTimeMillis()

    var Uid: String? = FirebaseAuth.getInstance().uid

    var username: String? = null
    var room_ask:Boolean = false
}