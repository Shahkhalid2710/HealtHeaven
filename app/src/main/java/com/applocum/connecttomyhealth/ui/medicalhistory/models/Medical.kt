package com.applocum.connecttomyhealth.ui.medicalhistory.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Medical(
    @SerializedName("id") val id: Int,
    @SerializedName("code") val code: Long,
    @SerializedName("description") val description: String,
    @SerializedName("full_code") val full_code: String
) : Serializable