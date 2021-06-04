package com.applocum.connecttomyhealth.ui.addcard.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Card(
    @SerializedName("id") val id: Int,
    @SerializedName("card_number") val card_number: String,
    @SerializedName("is_verified") val is_verified: Boolean,
    @SerializedName("card_type") val card_type: String,
    @SerializedName("expiry_date") val expiry_date: Int,
    @SerializedName("is_primary") val is_primary: Boolean,
    @SerializedName("card_holder_name") val card_holder_name: String
) : Serializable