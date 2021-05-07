package com.applocum.connecttomyhealth.ui.appointment.models
import com.google.gson.annotations.SerializedName

data class PatientDetail (

	@SerializedName("id") val id : Int,
	@SerializedName("email") val email : String,
	@SerializedName("first_name") val first_name : String,
	@SerializedName("last_name") val last_name : String,
	@SerializedName("status") val status : String,
	@SerializedName("image") val image : String,
	@SerializedName("gender") val gender : String
)