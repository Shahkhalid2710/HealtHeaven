package com.applocum.connecttomyhealth.ui.signup.models

import com.google.gson.annotations.SerializedName

data class Organization(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)