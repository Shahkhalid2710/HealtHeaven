package com.applocum.connecttomyhealth.ui.prescription.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Document(

    @SerializedName("download_type") val download_type: String,
    @SerializedName("file") val file: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("by") val by: String
) : Serializable