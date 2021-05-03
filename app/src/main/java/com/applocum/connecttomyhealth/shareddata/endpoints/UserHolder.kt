package com.applocum.connecttomyhealth.shareddata.endpoints

import android.content.SharedPreferences
import com.applocum.connecttomyhealth.extensions.prefString

class UserHolder(pref: SharedPreferences) {
    var userid by pref.prefString()
        private set

    var userFirstName by pref.prefString()
        private set

    var userLastName by pref.prefString()
        private set

    var userEmail by pref.prefString()
        private set

    var userGender by pref.prefString()
        private set

    var userDOB by pref.prefString()
        private set

    var userToken by pref.prefString()
        private set


    var doctorId by pref.prefString()
        private set

    var doctorFirstname by pref.prefString()
       private set

    var doctorLastname by pref.prefString()
        private set

    var doctorDesignation by pref.prefString()
        private set

    var doctorBio by pref.prefString()
        private set

    var doctorImage by pref.prefString()
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
}