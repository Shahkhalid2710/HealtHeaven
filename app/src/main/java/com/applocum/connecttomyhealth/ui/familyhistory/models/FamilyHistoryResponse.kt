package com.applocum.connecttomyhealth.ui.familyhistory.models
import com.google.gson.annotations.SerializedName


data class FamilyHistoryResponse (

	@SerializedName("status") val status : Int,
	@SerializedName("success") val success : Boolean,
	@SerializedName("message") val message : String,
	@SerializedName("data") val data : ArrayList<FamilyHistory>,
	@SerializedName("subscription_plans") val subscription_plans : Boolean
)