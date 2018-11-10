package com.example.enpit_p13.chatapp.room_chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Room_chat_messager(val messageText: String,val  kadaimeiText:String,val uid :String,val check:Boolean) :Parcelable{

    constructor() : this("","","",false)

    var timestamp: Long = System.currentTimeMillis()


}

