package com.example.myapplication.Activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.widget.Toast

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api


import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue.*

import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room


import coil.compose.AsyncImage
import com.example.myapplication.DB.AppDataBase
import com.example.myapplication.DB.PhotoObjectDB_Dao
import com.example.myapplication.R


import com.example.myapplication.ui.Objects.PhotoObject
import com.example.myapplication.ui.Objects.PhotoObjectBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.Serializable
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
class pickphoto : ComponentActivity() {
    val test = mutableStateOf(0)
    val test_lista = mutableStateListOf<PhotoObject>()
    val launch=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode== RESULT_OK) test_lista[test.value]=result.data?.extras?.get("odp") as PhotoObject
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
        Main_Column()
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun peview(){
           Main_Column()
        }

    @Composable
    fun ListView_Image_Object(element:PhotoObject,index:Int){

        var doedit by remember { mutableStateOf(false) }


        if (doedit){
            val x = Intent(this@pickphoto, EditPhotoTicket::class.java)
            x.putExtra("editphoto", element as? Serializable)
            doedit=false
            test.value=index
            launch.launch(x)

        }


        Row( Modifier.fillMaxWidth().pointerInput(Unit){ detectTapGestures(onDoubleTap =
            {
                val x: Intent = Intent(this@pickphoto, MainActivity::class.java)
                x.putExtra("123", element as? Serializable)
                startActivity(x)
            }
            , onTap = {val x = Intent(this@pickphoto, EditPhotoTicket::class.java)
                x.putExtra("editphoto", element as? Serializable)
                doedit=false
                test.value=index
                launch.launch(x)}
        )}.padding(10.dp).background( color= colorResource( R.color.dark_grey), shape = RoundedCornerShape(20.dp)).height(100.dp))
        {


            Column( Modifier.weight(0.6f).padding(2.dp))

            {
                AsyncImage( model = element.uri, "xd", modifier = Modifier.fillMaxSize().clip(shape = RoundedCornerShape(20.dp)))
            }

            Column(Modifier.weight(0.4f).fillMaxSize())
            {

                Row(Modifier.fillMaxHeight(0.3f).fillMaxWidth())
                {
                    Text(element.short_text.toString(),Modifier.fillMaxSize())
                }

                HorizontalDivider(modifier = Modifier.fillMaxHeight(0.05f), thickness = 1.dp, color = Color.Black)

                Row(Modifier.fillMaxSize())
                {
                    Text(element.long_text.toString(), modifier = Modifier.fillMaxSize())
                }
            }
        }

    }
    @Composable
     fun Main_Column(){

        val db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "xdbx"
        ).allowMainThreadQueries().build()

        val localDao = db.PhotoObject_Dao()

        val list_of_photos = remember { test_lista }
        val context = LocalContext.current;
        val notificationtext= stringResource(R.string.HAVENTPICKEDIMAGE)
        val xdd =  rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),onResult = { uri->

            if(uri!=null ){
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                val newPhotoObject=PhotoObjectBuilder().add_UID(UUID.randomUUID()).add_long_text("").add_short_text("").add_uri(uri.toString()).create()
                list_of_photos.add(newPhotoObject)
                localDao.add_photo(PhotoObjectBuilder.convert_Normal_to_DB(newPhotoObject))
                }else{
                    Toast.makeText(
                        baseContext,
                        notificationtext,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        })
        LaunchedEffect(Unit) { withContext(Dispatchers.IO){  val photos=localDao.get_all_photoobject();list_of_photos.addAll(photos)}}





        Box(Modifier.fillMaxSize()){

        LazyColumn(Modifier.background(color = colorResource(R.color.black)).fillMaxSize()) {

            items(list_of_photos, key = {item->item.hashCode()}) { element ->
                val do_exist= remember { mutableStateOf(true) }
                val xd = rememberSwipeToDismissBoxState()
                AnimatedVisibility(do_exist.value, enter = slideInHorizontally() ,exit = slideOutVertically ()+ shrinkVertically ()+ fadeOut()) {
                SwipeToDismissBox(state = xd ,backgroundContent ={do_exist.value=Slide_To_Dismis_Reaction(xd,list_of_photos,element)}){
                 ListView_Image_Object(element,list_of_photos.indexOf(element))
                }
                }
                if(!do_exist.value) Remove_Object(list_of_photos,element,localDao)
            }
        }
        ElevatedButton(modifier = Modifier.align(AbsoluteAlignment.BottomRight).padding(20.dp),colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray), onClick = {
            xdd.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) { Text(list_of_photos.size.toString()) }
        }
     }

    @Composable
    fun Remove_Object(listofphotos: SnapshotStateList<PhotoObject>,element : PhotoObject,localDao: PhotoObjectDB_Dao){

        LaunchedEffect(true) {
            delay(1000);listofphotos.remove(element);withContext(Dispatchers.IO){ localDao.delete_photoobject(element.UID.toString());
            val Pinsdb=Room.databaseBuilder(applicationContext,AppDataBase::class.java , "xdbx").allowMainThreadQueries().build()
            val Pinsdao=Pinsdb.PinObjectDB_Dao()
            Pinsdao.deletAllPinsUID(element.UID.toString())
        }
        }


    }

    @Composable
    fun Slide_To_Dismis_Reaction(boxstate:SwipeToDismissBoxState, listofphotos: SnapshotStateList<PhotoObject>, element:PhotoObject):Boolean{
            when (boxstate.dismissDirection) {
                StartToEnd , EndToStart -> {
                    //AnimatedVisibility(boxstate.currentValue==StartToEnd, enter = fadeIn(), exit = slideOutHorizontally()) {
                    Row(
                        Modifier.fillMaxWidth().padding(10.dp).background(
                            color = colorResource(R.color.purple_200),
                            shape = RoundedCornerShape(20.dp)
                        ).height(100.dp)
                    ) {}

                    if (boxstate.currentValue==StartToEnd||boxstate.currentValue==EndToStart) {

                        return false
                    }
                }
                Settled -> {
                    Box(Modifier.fillMaxSize()) {}
                }
            }

        return true
            }
    }




