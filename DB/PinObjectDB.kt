package com.example.myapplication.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PinObjectDB(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    @ColumnInfo(name="uid")
    var uid:String,
    @ColumnInfo(name="x")
    var x:Int,
    @ColumnInfo(name="y")
    var y:Int,
    @ColumnInfo(name="short_text")
    var short_text:String,
    @ColumnInfo(name="long_text")
    var long_text:String
)