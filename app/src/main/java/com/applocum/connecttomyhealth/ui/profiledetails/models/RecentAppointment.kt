package com.applocum.connecttomyhealth.ui.profiledetails.models
import com.google.gson.annotations.SerializedName


data class RecentAppointment (

	@SerializedName("id") val id : Int,
	@SerializedName("status") val status : String,
	@SerializedName("created_at") val created_at : String
)