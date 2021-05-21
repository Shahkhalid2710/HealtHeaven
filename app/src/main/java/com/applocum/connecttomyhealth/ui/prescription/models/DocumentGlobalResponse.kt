package com.applocum.connecttomyhealth.ui.prescription.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DocumentGlobalResponse (

	@SerializedName("status") val status : Int,
	@SerializedName("success") val success : Boolean,
	@SerializedName("message") val message : String,
	@SerializedName("data") val data :ArrayList<Document>,
	@SerializedName("subscription_plans") val subscription_plans : Boolean
):Serializable