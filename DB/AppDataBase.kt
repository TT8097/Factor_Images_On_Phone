package com.example.myapplication.DB

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [PhotoObject_DB::class,PinObjectDB::class], version = 2)
abstract class AppDataBase: RoomDatabase() {
    abstract fun PhotoObject_Dao():PhotoObjectDB_Dao
    abstract fun PinObjectDB_Dao():PinObjectDB_Dao
}