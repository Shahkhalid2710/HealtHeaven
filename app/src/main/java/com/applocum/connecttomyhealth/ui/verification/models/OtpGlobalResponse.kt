package com.applocum.connecttomyhealth.ui.verification.models
import com.google.gson.annotations.SerializedName

data class OtpGlobalResponse (

	@SerializedName("status") val status : Int,
	@SerializedName("success") val success : Boolean,
	@SerializedName("message") val message : String,
	@SerializedName("data") val data : List<String>,
	@SerializedName("subscription_plans") val subscription_plans : Boolean
)