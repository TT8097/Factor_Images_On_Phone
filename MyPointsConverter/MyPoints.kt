package com.example.myapplication.MyPointsConverter

import android.content.res.Configuration
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.Objects.Pin
import com.example.myapplication.ui.Objects.PinObjectBuilder
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

interface MyPoints {
    companion object{
        fun listToMypointsConverter( pins:SnapshotStateList<Pin>,density : Float,heightLocal:Float,widthLocla:Float):String{
            var stringBuilder = "${widthLocla}|${heightLocal}|${density}"
            pins.forEach{pin ->
                stringBuilder+=pin.myPointstoString(heightLocal,widthLocla)
            }
            return stringBuilder
        }
        fun myPointsToPin(mypoints : String, pins : SnapshotStateList<Pin>, density : Float,listToUpdate : SnapshotStateList<Pin>,heightLocal:Float,widthLocla:Float,Uid:String){
            var pinsString:List<String> = mypoints.split("|")
            val densForeign=pinsString[2].toFloat()
            val heightForeign = pinsString[1].toFloat()
            val widthForeign = pinsString[0].toFloat()
            val heightrate = heightForeign/heightLocal
            val widthrate = widthForeign/widthLocla
            val heightDiffrenses: Float= if(heightForeign>heightLocal){
                ((heightLocal/heightForeign)-1)
            }
            else{
                ((heightLocal/heightForeign)-1)
            }
            pinsString=pinsString.drop(3)

            pinsString.forEach { pinString ->
                val pinData= pinString.split(";")
                val pin : PinObjectBuilder = PinObjectBuilder();
                pin.setOffset(
                    Offset(
                        (pinData[0].toFloat()*(widthLocla))*widthrate,
                        (((pinData[1].toFloat()+heightDiffrenses)*(heightLocal))*heightrate)
                        )
                )
                pin.setShortText(pinData[2])
                pin.setUid(Uid)
                pins.add(pin.build())
                listToUpdate.add(pin.build())
            }
        }
    }

}