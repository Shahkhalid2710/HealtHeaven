package com.applocum.connecttomyhealth.ui.mygp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Surgery(

    @SerializedName("id") val id: Int? = 0,
    @SerializedName("practice_code") val practice_code: String ?= "",
    @SerializedName("practice_name") val practice_name: String ?= "",
    @SerializedName("address") val address: String ?= "",
    @SerializedName("city") val city: String? = "",
    @SerializedName("county") val county: String? = "",
    @SerializedName("post_code") val post_code: String? = "",
    @SerializedName("phone") val phone: String? = "",
    @SerializedName("lat") val lat: Double ?= 0.0,
    @SerializedName("long") val long: Double? = 0.0,
    @SerializedName("status") val status: String? = ""
) : Serializable