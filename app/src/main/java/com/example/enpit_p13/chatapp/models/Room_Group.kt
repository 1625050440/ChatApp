package com.example.enpit_p13.chatapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class  Room_Group(val count:Int): Parcelable
{
    constructor(): this(0)
}