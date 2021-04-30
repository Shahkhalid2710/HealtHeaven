package com.applocum.connecttomyhealth.ui.booksession.models
import com.google.gson.annotations.SerializedName

data class TimeResponse (
	@SerializedName("status") val status : Int,
	@SerializedName("success") val success : Boolean,
	@SerializedName("message") val message : String,
	@SerializedName("data") val data : ArrayList<Time>,
	@SerializedName("common") val common : Common,
	@SerializedName("subscription_plans") val subscription_plans : Boolean
)