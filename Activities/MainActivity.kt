package com.example.myapplication.Activities
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Dialog
import androidx.room.Room
import coil.compose.AsyncImage
import com.example.myapplication.MyPointsConverter.MyPoints
import com.example.myapplication.DB.AppDataBase
import com.example.myapplication.DB.PinObjectDB_Dao
import com.example.myapplication.R
import com.example.myapplication.ui.Objects.PhotoObject
import com.example.myapplication.ui.Objects.Pin
import com.example.myapplication.ui.Objects.PinObjectBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.concurrent.Executors

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
     lateinit var db: AppDataBase
    lateinit var localdao:PinObjectDB_Dao
    val listToUpdate = mutableStateListOf<Pin>()
    lateinit var photoobject:PhotoObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{

             photoobject = (this.intent.extras?.get("123") as? PhotoObject)!!
            db=Room.databaseBuilder(applicationContext,AppDataBase::class.java , "xdbx").allowMainThreadQueries().build()
            localdao=db.PinObjectDB_Dao()
            massagecard(photoobject)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val copyListToUpdate = listToUpdate
            copyListToUpdate.forEach{x->

                if(x.id==-1){

                    localdao.addPin(
                        x.uid,
                        x.x,
                        x.y,
                        x.short_text,
                        x.long_text
                    )

                }else{localdao.updateText(x.id,x.short_text,x.long_text)}}

        }


    }


    @Composable
    fun massagecard( photoobject:PhotoObject?){
        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        var zoom_mem by remember { mutableStateOf(0f) }
        val pins = remember { mutableStateListOf<Pin>() }
        var element_clicked by remember { mutableStateOf(Pin())  }
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) { withContext(Dispatchers.IO){pins.addAll(localdao.getAllPins(photoobject!!.UID.toString()))  } }
        Box(modifier = Modifier.fillMaxSize()){

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->
                        zoom_mem = zoom
                                scale = (scale * zoom).coerceIn(1f, 5f)
                        val max_x =
                            ((scale - 1) * baseContext.resources.displayMetrics.widthPixels.toFloat()) / 2
                        val max_y =
                            ((scale - 1) * baseContext.resources.displayMetrics.heightPixels.toFloat()) / 2
                        offset = Offset(
                            x = (offset.x + pan.x).coerceIn(-max_x, max_x),
                            y = (offset.y + pan.y).coerceIn(-max_y, max_y)
                        )
                    }

                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        ) {

            AsyncImage(model = photoobject!!.uri, "xd",
                modifier = Modifier
                    .clipToBounds()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { tapoffset ->
                                val pin = PinObjectBuilder()
                                    .setOffset(
                                        Offset(
                                        x= tapoffset.x,
                                        y= tapoffset.y
                                        )
                                    ).setUid(photoobject.UID.toString())
                                    .build()
                                pins.add(pin)
                                listToUpdate.add(pin)
                            }
                        )
                    }
                    .fillMaxHeight()
                    .fillMaxWidth())


            pins.forEach{ pin ->
                key(pin) {
                    val test by remember { mutableStateOf(pin) }
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    pin.x ,
                                    pin.y
                                )
                            }
                            .size(10.dp / scale)
                    ) {
                        ElevatedButton(onClick = {
                            element_clicked = test;

                        }, Modifier.align(Alignment.Center)) { }
                    }
                } }
        }
            Icon(painter = painterResource(R.drawable.baseline_delete_forever_24),"delete", modifier = Modifier
                .size(50.dp)
                .align(Alignment.TopCenter)
                .clickable {
                    if (element_clicked.id != -1) {

                        pins.remove(element_clicked);scope.launch(Dispatchers.IO) {
                            listToUpdate.remove(element_clicked)
                            localdao.deletePin(
                                element_clicked.id
                            )
                        }
                    } else {
                        pins.remove(element_clicked)
                        listToUpdate.remove(element_clicked)
                    }
                } )
            var mypointdialog by remember { mutableStateOf(false) }
            Icon(painter = painterResource(R.drawable.baseline_control_point_duplicate_24),"delete", modifier = Modifier
                .size(50.dp)
                .align(Alignment.TopEnd)
                .clickable {
                    mypointdialog = true
                } )
            if (mypointdialog){popUpWindow({mypointdialog=false},pins)}
            bottom_drawer(element_clicked)
        }



    }
    @Composable
    @Preview(showSystemUi = true)
    fun prev(){



    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun bottom_drawer(element_clicked:Pin){
        val sheet_state= rememberBottomSheetScaffoldState()
        BottomSheetScaffold(sheetContent = { Bottom_Bar_Items(element_clicked)  }, modifier = Modifier.systemBarsPadding() ,scaffoldState = sheet_state, sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight*2) { }
    }
    @Composable
    fun Bottom_Bar_Items(element_clicked:Pin){
        val changed = remember {  mutableStateOf(false)}
        val short_text= remember { mutableStateOf("") }
        val long_text= remember { mutableStateOf("") }
        short_text.value=element_clicked.short_text
        long_text.value=element_clicked.long_text;
        Column() {
            Row(modifier = Modifier
                .fillMaxSize()
                .weight(0.1f)) {  TextField(value = short_text.value, onValueChange = {short_text.value=it;element_clicked.short_text=it ; changed.value=true}, modifier = Modifier.fillMaxSize(), maxLines = 1)}
            Row(modifier = Modifier
                .fillMaxSize()
                .weight(0.9f)) {  TextField(value = long_text.value, onValueChange = {long_text.value=it; element_clicked.long_text=it; changed.value=true}, modifier = Modifier.fillMaxSize())}
        }
        if (changed.value && !listToUpdate.contains(element_clicked)){listToUpdate.add(element_clicked)}

    }

    @Composable
    fun popUpWindow(onDismissRequest: () -> Unit,pins: SnapshotStateList<Pin>){
        var myPoints by remember { mutableStateOf("") }
        val density = LocalDensity.current.density
        val heightLocla= baseContext.resources.displayMetrics.heightPixels.toFloat()
        val widthLocal= baseContext.resources.displayMetrics.widthPixels.toFloat()

        println( windowManager.currentWindowMetrics.bounds.height()-WindowInsets.systemBars.getTop(LocalDensity.current)-WindowInsets.systemBars.getBottom(LocalDensity.current))
        Dialog(onDismissRequest) {
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(20.dp)
                .clickable { onDismissRequest() }, shape = RoundedCornerShape(16.dp)) {

                Column(Modifier.fillMaxSize()) {
                    Row (Modifier.fillMaxWidth()){
                        Column {
                        Text("Mypoints", modifier = Modifier.align(Alignment.CenterHorizontally))
                        TextField(myPoints,{myPoints=it},maxLines = 1, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        }
                    }
                    Row (Modifier.align(Alignment.CenterHorizontally)){
                        Button(onClick = {
                            try {MyPoints.myPointsToPin(myPoints,pins, density,listToUpdate,heightLocla,widthLocal,photoobject.UID.toString())}catch (x: Exception){}}) { Text(
                            stringResource(R.string.ADD)
                        ) }
                        Button(onClick = {myPoints=MyPoints.listToMypointsConverter(pins,density,heightLocla,widthLocal)}) { Text(
                            stringResource(R.string.GENERATE)
                        )}
                    }
                }

            }
        }
    }


}


// 1080.0|2154.0|2.75|0.6398148;0.48746517;|0.33796296;0.3797586;
//1080.0|2201.0|2.625|0.34444445;0.40299863;|0.6490741;0.50567925;