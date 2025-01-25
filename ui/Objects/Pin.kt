package com.example.myapplication.ui.Objects

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.myapplication.DB.PinObjectDB
import java.io.Serializable
import kotlin.math.roundToInt

class Pin() : Serializable {
    var id : Int = -1
    var uid : String = ""
    var x : Int = 1
    var y : Int = 2
    var short_text: String=""
    var long_text:String=""

    fun myPointstoString(heightLocal:Float,widthLocla:Float):String{
        val xdp = x.toFloat()/widthLocla
        val ydp = y.toFloat()/heightLocal
        return "|${xdp};${ydp};$short_text"
    }

}