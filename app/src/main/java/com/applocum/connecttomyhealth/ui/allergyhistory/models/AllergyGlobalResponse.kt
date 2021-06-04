package com.applocum.connecttomyhealth.ui.allergyhistory.models

import com.google.gson.annotations.SerializedName

data class AllergyGlobalResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ArrayList<String>,
    @SerializedName("subscription_plans") val subscription_plans: Boolean
)