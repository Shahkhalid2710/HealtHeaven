package com.applocum.connecttomyhealth.ui.signup.models

import com.applocum.connecttomyhealth.ui.profiledetails.models.Common
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GlobalResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("success") val success : Boolean,
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : JsonElement?,
    @SerializedName("common") val common : Common,
    @SerializedName("subscription_plans") val subscription_plans : Boolean
):Serializable