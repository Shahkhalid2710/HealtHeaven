package com.applocum.connecttomyhealth.ui.addcard.models

import com.google.gson.annotations.SerializedName


data class CardResponse(

    @SerializedName("status") val status: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ArrayList<Card>,
    @SerializedName("common") val common: Common,
    @SerializedName("subscription_plans") val subscription_plans: Boolean
)