package com.applocum.connecttomyhealth.shareddata.endpoints

import android.content.SharedPreferences
import com.applocum.connecttomyhealth.extensions.prefString
import com.google.gson.Gson

class UserHolder(pref: SharedPreferences) {
    var userid by pref.prefString()
        private set

    var userFirstName by pref.prefString()

    var userLastName by pref.prefString()

    var userEmail by pref.prefString()
        private set

    var userGender by pref.prefString()
        private set

    var userDOB by pref.prefString()
        private set

    var userToken by pref.prefString()
        private set

    var userPhoto by pref.prefString()
        private set

    var bookAppointmentData by pref.prefString()
        private set

    var clinicalToken by pref.prefString()
        private set


    fun saveUser(
        id: String,
        firstname: String,
        lastname: String,
        email: String,
        gender: String,
        dob: String,
        token: String
    ) {
        userid = id
        userFirstName = firstname
        userLastName = lastname
        userEmail = email
        userGender = gender
        userDOB = dob
        userToken = token
    }

    fun clearUserData(
        id: String,
        firstname: String,
        lastname: String,
        email: String,
        gender: String,
        dob: String,
        token: String
    ) {
        userid = id
        userFirstName = firstname
        userLastName = lastname
        userEmail = email
        userGender = gender
        userDOB = dob
        userToken = token
    }

    fun saveClinicalToken(token:String)
    {
        clinicalToken=token
    }

    fun clearClinicalToken(token:String)
    {
        clinicalToken=token
    }

    fun saveUserPhoto(picUrl:String)
    {
        userPhoto=picUrl
    }

    fun clearUserPhoto(picUrl:String)
    {
        userPhoto=picUrl
    }

    fun saveBookAppointmentData(appointment: BookAppointment?) {
        appointment?.let {
            this.bookAppointmentData = Gson().toJson(appointment)
        }
    }

    fun getBookAppointmentData(): BookAppointment {
        return Gson().fromJson(bookAppointmentData,BookAppointment::class.java)
    }
}