package com.applocum.connecttomyhealth.ui.medicalhistory.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MedicalGlobalResponse(

    @SerializedName("status") val status: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ArrayList<Medical>,
    @SerializedName("subscription_plans") val subscription_plans: Boolean
) : Serializable