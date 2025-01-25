package com.example.myapplication.ui.Objects

import androidx.compose.ui.geometry.Offset
import com.example.myapplication.DB.PinObjectDB
import kotlin.math.roundToInt

class PinObjectBuilder {
    private var pin = Pin()
    fun setOffset(offset: Offset): PinObjectBuilder{
        pin.x=offset.x.roundToInt()
        pin.y=offset.y.roundToInt()
        return this
    }
    fun setUid(uid:String): PinObjectBuilder{
        pin.uid=uid
        return this
    }
    fun setShortText(shorttext:String): PinObjectBuilder{
        pin.short_text=shorttext
        return this
    }
    fun setLongtext(longtext:String): PinObjectBuilder{
        pin.long_text=longtext
        return this
    }
    fun build():Pin{return pin}
    companion object{
        fun convertNormalToDB(pin:Pin):PinObjectDB{
            return PinObjectDB(-1,pin.uid,pin.x,pin.y,pin.short_text,pin.long_text)
        }

    }
}