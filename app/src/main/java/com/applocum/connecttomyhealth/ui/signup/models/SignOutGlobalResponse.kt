package com.applocum.connecttomyhealth.ui.signup.models
import com.google.gson.annotations.SerializedName


data class SignOutGlobalResponse (

	@SerializedName("status") val status : Int,
	@SerializedName("success") val success : Boolean,
	@SerializedName("message") val message : String,
	@SerializedName("data") val data : List<String>,
	@SerializedName("subscription_plans") val subscription_plans : Boolean
)