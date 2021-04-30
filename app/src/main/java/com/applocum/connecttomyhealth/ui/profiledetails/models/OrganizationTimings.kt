package com.applocum.connecttomyhealth.ui.profiledetails.models
import com.google.gson.annotations.SerializedName

data class OrganizationTimings (

	@SerializedName("id") val id : Int,
	@SerializedName("day") val day : String,
	@SerializedName("start_time") val start_time : String,
	@SerializedName("end_time") val end_time : String
)