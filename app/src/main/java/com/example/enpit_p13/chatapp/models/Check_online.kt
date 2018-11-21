package com.example.enpit_p13.chatapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class  Check_online(val uid_check_online: String):Parcelable
{
    constructor(): this("")
}