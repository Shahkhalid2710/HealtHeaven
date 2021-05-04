package com.applocum.connecttomyhealth.ui.specialists.models
import com.google.gson.annotations.SerializedName

data class DoctorResponse (
	@SerializedName("status") val status : Int,
	@SerializedName("success") val success : Boolean,
	@SerializedName("message") val message : String,
	@SerializedName("data") val data : ArrayList<Specialist>,
	@SerializedName("subscription_plans") val subscription_plans : Boolean
)