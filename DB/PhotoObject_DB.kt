package com.example.myapplication.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class PhotoObject_DB(
    @PrimaryKey()
    var UID:String,
    @ColumnInfo(name = "uri")
    var uri:String,
    @ColumnInfo(name = "long_text")
    var long_text:String,
    @ColumnInfo(name = "short_text")
    var short_text:String
)



