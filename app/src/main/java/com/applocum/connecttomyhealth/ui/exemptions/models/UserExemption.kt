package com.applocum.connecttomyhealth.ui.exemptions.models

import com.google.gson.annotations.SerializedName

data class UserExemption(

    @SerializedName("id") val id: Int,
    @SerializedName("exemption") val exemption: String,
    @SerializedName("text") val text: String,
    @SerializedName("file_url") val file_url: String,
    @SerializedName("thumb_file_url") val thumb_file_url: String
)