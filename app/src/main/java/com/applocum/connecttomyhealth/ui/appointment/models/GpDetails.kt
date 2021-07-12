package com.applocum.connecttomyhealth.ui.appointment.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GpDetails(

    @SerializedName("id") val id: Int? = 0,
    @SerializedName("first_name") val first_name: String? = "",
    @SerializedName("last_name") val last_name: String? = "",
    @SerializedName("image") val image: String? = "",
    @SerializedName("gmc") val gmc: String? = "",
    @SerializedName("nmc") val nmc: String? = ""
) : Serializable