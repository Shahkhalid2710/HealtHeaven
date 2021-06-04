package com.applocum.connecttomyhealth.ui.appointment.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BookAppointmentGlobalResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ArrayList<BookAppointmentResponse>,
    @SerializedName("subscription_plans") val subscription_plans: Boolean
) : Serializable