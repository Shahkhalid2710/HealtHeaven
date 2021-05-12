package com.applocum.connecttomyhealth.ui.changepassword.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PasswordGlobalResponse (
	@SerializedName("status") val status : Int,
	@SerializedName("success") val success : Boolean,
	@SerializedName("message") val message : String,
	@SerializedName("data") val data : List<String>,
	@SerializedName("subscription_plans") val subscriptionPlans : Boolean
):Serializable