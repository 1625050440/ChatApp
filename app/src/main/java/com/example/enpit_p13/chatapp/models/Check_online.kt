package com.example.enpit_p13.chatapp.models

import android.os.Parcelable
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.parcel.Parcelize

@Parcelize
class  Check_online(val uid_check_online: String,val username :String):Parcelable
{
    constructor(): this("","")
 var my_uid = FirebaseAuth.getInstance().uid
}
