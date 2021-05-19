package com.applocum.connecttomyhealth.ui.investigation.models
import com.google.gson.annotations.SerializedName

data class InvestigationResponse (
	@SerializedName("status") val status : Int,
	@SerializedName("success") val success : Boolean,
	@SerializedName("message") val message : String,
	@SerializedName("data") val data :ArrayList<Investigation>,
	@SerializedName("subscription_plans") val subscription_plans : Boolean
)