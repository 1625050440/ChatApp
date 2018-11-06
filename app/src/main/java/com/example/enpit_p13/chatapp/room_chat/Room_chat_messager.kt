package com.example.enpit_p13.chatapp.room_chat

import android.os.Parcelable
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.parcel.Parcelize

@Parcelize
class Room_chat_messager(val messageText: String,val  kadaimeiText:String) :Parcelable{

    constructor() : this("","")

    var timestamp: Long = System.currentTimeMillis()

    var Uid: String? = FirebaseAuth.getInstance().uid

}

