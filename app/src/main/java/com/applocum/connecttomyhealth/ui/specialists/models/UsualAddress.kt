package com.applocum.connecttomyhealth.ui.specialists.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UsualAddress(
    @SerializedName("line1") val line1 : String="",
    @SerializedName("line2") val line2 : String="",
    @SerializedName("line3") val line3 : String="",
    @SerializedName("town") val town : String="",
    @SerializedName("pincode") val pincode : String="",
    @SerializedName("name") val name : String="",
    @SerializedName("latitude") val latitude : Double=0.0,
    @SerializedName("longitude") val longitude : Double=0.0
    ):Serializable