package com.applocum.connecttomyhealth.ui.appointment.models

import java.io.Serializable

class Session(image:Int,name:String,sessiontype:String,date:String):Serializable {
    var sImage=image
    var sName=name
    var sSessionType=sessiontype
    var sDate=date
}