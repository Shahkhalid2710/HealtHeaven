package com.applocum.connecttomyhealth.shareddata.endpoints

import android.content.SharedPreferences
import com.applocum.connecttomyhealth.extensions.prefString

class UserHolder(pref:SharedPreferences) {
    var userid by pref.prefString()

    var userFirstName by pref.prefString()

    var userLastName by pref.prefString()

    var userEmail by pref.prefString()

    var userGender by pref.prefString()

    var userDOB by pref.prefString()

    var userToken by pref.prefString()

    fun saveUser(id:String,firsname:String,lastname:String,email:String,gender:String,dob:String,token:String)
    {
        userid=id
        userFirstName=firsname
        userLastName=lastname
        userEmail=email
        userGender=gender
        userDOB=dob
        userToken=token
    }

    fun clearUserData(
        id:String,firsname:String,lastname:String,email:String,gender:String,dob:String,token:String) {
        userid=id
        userFirstName=firsname
        userLastName=lastname
        userEmail=email
        userGender=gender
        userDOB=dob
        userToken=token
    }
}