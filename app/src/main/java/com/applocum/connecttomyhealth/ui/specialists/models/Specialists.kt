package com.applocum.connecttomyhealth.ui.specialists.models

import java.io.Serializable

class Specialists(image:Int,name:String,prof:String,des:String) :Serializable{
    var sImage=image
    var sName=name
    var sProf=prof
    var sDes=des
}