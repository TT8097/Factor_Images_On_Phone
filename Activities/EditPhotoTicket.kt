package com.example.myapplication.Activities

import android.content.Intent
import android.content.res.Resources
import android.graphics.Paint.Align
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Database
import androidx.room.Room
import coil.compose.AsyncImage
import com.example.myapplication.DB.AppDataBase
import com.example.myapplication.R
import com.example.myapplication.ui.Objects.PhotoObject
import com.example.myapplication.ui.Objects.PhotoObjectBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.io.Serializable

class EditPhotoTicket : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        layout()}
    }
    @Composable
    fun layout(){
        val photoobject: PhotoObject= this.intent.extras?.get("editphoto") as PhotoObject
        val focus =  LocalFocusManager.current
        var long_text by remember { mutableStateOf(photoobject.long_text!!)}
        var short_text by remember { mutableStateOf(photoobject.short_text!!)}
        val doShowPopUp = remember { mutableStateOf(false) }

        Column(Modifier.fillMaxSize().background(color = colorResource(R.color.dark_grey)).clickable { focus.clearFocus() }){
            Box(Modifier.fillMaxHeight(0.4f).fillMaxSize()){ AsyncImage( model = photoobject.uri, "xd", modifier = Modifier.fillMaxSize());ElevatedButton(onClick = {doShowPopUp.value=true},Modifier.align(Alignment.TopEnd), colors = ButtonColors(containerColor = colorResource(R.color.white_grey), contentColor = Color.Unspecified, disabledContainerColor =Color.Unspecified, disabledContentColor = Color.Unspecified )) { Text(
                stringResource(R.string.SAFE)
            ) } }//image

            Column(modifier = Modifier.fillMaxHeight(1f).fillMaxWidth(1f)) {
                Row(Modifier.fillMaxHeight(0.1f)) { TextField(modifier = Modifier.fillMaxSize().background(color =colorResource(R.color.black)),
                    shape =RoundedCornerShape(15) , value = short_text, onValueChange = {short_text=it},colors = TextFieldDefaults.colors(unfocusedContainerColor = colorResource(R.color.dark_grey),focusedContainerColor = colorResource(R.color.dark_grey)), maxLines = 1,singleLine = true) } // short text
                Row(Modifier.fillMaxSize()) { TextField(modifier = Modifier.fillMaxSize().background(color=colorResource(R.color.black)),
                    shape = RoundedCornerShape(5),value = long_text, onValueChange = {long_text=it}, colors = TextFieldDefaults.colors(unfocusedContainerColor = colorResource(R.color.dark_grey), focusedContainerColor = colorResource(R.color.dark_grey)))
                 } // long text
            }
        }
        if(alertdialog(doShowPopUp)){
            val db = Room.databaseBuilder(
                applicationContext,AppDataBase::class.java,"xdbx"
            ).build()
            photoobject.short_text=short_text
            photoobject.long_text=long_text
            LaunchedEffect(Unit) { withContext(Dispatchers.IO){db.PhotoObject_Dao().update_image_object(PhotoObjectBuilder.convert_Normal_to_DB(photoobject))} }
            val intent = Intent()
            intent.putExtra("odp",photoobject as? Serializable)
            setResult(RESULT_OK,intent)
            finish()
        }
    }
    @Preview(showSystemUi = true)
    @Composable
    fun xd(){
        //val doShowPopUp = remember { mutableStateOf(true) }
        //alertdialog(doShowPopUp)
        layout()
    }
   @Composable
   fun alertdialog(doShow: MutableState<Boolean>): Boolean {
       var doSafe by remember { mutableStateOf(false) }
       if(doShow.value) {
           AlertDialog(
               onDismissRequest = { doShow.value = false },
               confirmButton = { Text(stringResource(R.string.YES), modifier = Modifier.clickable { doShow.value=false;doSafe=true
               }.padding(horizontal = 60.dp)) },
               dismissButton = { Text(stringResource(R.string.NO), modifier = Modifier.clickable { doShow.value=false }) },
               text = { Text(stringResource(R.string.DOSAFE), modifier = Modifier.padding(40.dp)) }

           )

       }
       return doSafe
   }


}