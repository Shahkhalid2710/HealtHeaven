package com.applocum.connecttomyhealth.ui.exemptions.models

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class ExemptionGlobalResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: JsonElement?,
    @SerializedName("subscription_plans") val subscription_plans: Boolean
)