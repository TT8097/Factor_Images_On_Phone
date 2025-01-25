package com.example.myapplication.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.ui.Objects.PhotoObject

@Dao
interface PhotoObjectDB_Dao {
@Insert
fun add_photo(xd:PhotoObject_DB)
@Query("Delete from PhotoObject_DB where UID = :UID")
fun delete_photoobject(UID:String)
@Query("Select * from PhotoObject_DB")
fun get_all_photoobject():List<PhotoObject>
@Update
fun update_image_object(photoObject:PhotoObject_DB)
}
