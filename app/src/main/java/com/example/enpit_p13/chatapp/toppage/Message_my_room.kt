package com.example.enpit_p13.chatapp.toppage

import com.google.firebase.auth.FirebaseAuth

class Message_my_room {
    constructor(messageText: String, usernameText:String,check:Boolean){

        text = messageText
        username = usernameText
        add_cm = check

    }

    var text: String? = null

    var timestamp: Long = System.currentTimeMillis()

    var Uid: String? = FirebaseAuth.getInstance().uid

    var username: String? = null
    var add_cm:Boolean = false
}