package com.applocum.connecttomyhealth.ui.profiledetails.models
import com.google.gson.annotations.SerializedName

data class EmailContact (

	@SerializedName("id") val id : Int,
	@SerializedName("email") val email : String,
	@SerializedName("emailable_id") val emailable_id : Int,
	@SerializedName("emailable_type") val emailable_type : String
)