package com.example.myapplication.ui.Objects

import com.example.myapplication.DB.PhotoObject_DB
import java.util.UUID

class PhotoObjectBuilder{
    val phot_object:PhotoObject= PhotoObject()
    fun add_uri(uri:String):PhotoObjectBuilder{
        phot_object.uri=uri
        return this
    }
    fun add_short_text(theme:String):PhotoObjectBuilder{
        phot_object.short_text=theme
        return this
    }
    fun add_long_text(long_text:String):PhotoObjectBuilder{
        phot_object.long_text=long_text
        return this
    }
    fun add_UID(UID:UUID):PhotoObjectBuilder{
        phot_object.UID=UID.toString()
        return this
    }
    fun create():PhotoObject{
        return phot_object
    }
    companion object{
    fun convert_Normal_to_DB(photoObject: PhotoObject):PhotoObject_DB{
        return PhotoObject_DB(photoObject.UID.toString(),photoObject.uri.toString(),photoObject.long_text.toString(),photoObject.short_text.toString())
        }
    }






}