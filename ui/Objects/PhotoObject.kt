package com.example.myapplication.ui.Objects
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class PhotoObject() :  Serializable {
    var UID:String? =""
    var uri: String? = ""
        get() = field
    var short_text:String ?= ""
        get() = field
    var long_text:String?=""
        get() = field







}