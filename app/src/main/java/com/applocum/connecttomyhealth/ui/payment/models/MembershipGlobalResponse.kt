package com.applocum.connecttomyhealth.ui.payment.models
import com.google.gson.annotations.SerializedName


data class MembershipGlobalResponse (

	@SerializedName("status") val status : Int,
	@SerializedName("success") val success : Boolean,
	@SerializedName("message") val message : String,
	@SerializedName("data") val data : ArrayList<MembershipResponse>,
	@SerializedName("subscription_plans") val subscription_plans : Boolean
)