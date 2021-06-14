package com.applocum.connecttomyhealth.ui.mygp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SurgeryResponse(
    @SerializedName("surgery") val surgery: Surgery? = Surgery()
) : Serializable