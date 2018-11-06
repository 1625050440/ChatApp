package com.example.enpit_p13.chatapp

import com.google.firebase.auth.FirebaseAuth

class Message {



    constructor() //empty for firebase



    constructor(messageText: String, usernameText:String){

        text = messageText
        username = usernameText

    }

    var text: String? = null

    var timestamp: Long = System.currentTimeMillis()

    var Uid: String? = FirebaseAuth.getInstance().uid

    var username: String? = null
}

