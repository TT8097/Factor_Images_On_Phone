package com.example.myapplication.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.ui.Objects.Pin

@Dao
interface PinObjectDB_Dao {
    @Query("select * from PinObjectDB where uid == :uid")
    fun getAllPins(uid:String):List<Pin>
    @Query("Insert into PinObjectDB(uid,x,y,short_text,long_text)values(:uid,:x,:y,:shortText,:longText)")
    fun addPin(uid:String,x:Int,y:Int,shortText: String,longText: String )
    @Query("Update PinObjectDB set short_text=:shortText , long_text=:longText where id=:id")
    fun updateText(id : Int,shortText:String,longText:String)
    @Query("Delete from PinObjectDb where id=:id")
    fun deletePin(id:Int)
    @Query("Delete from PinObjectDB where uid=:uid" )
    fun deletAllPinsUID(uid:String)
}