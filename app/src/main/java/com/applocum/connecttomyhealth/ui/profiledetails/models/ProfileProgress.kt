package com.applocum.connecttomyhealth.ui.profiledetails.models
import com.google.gson.annotations.SerializedName

data class ProfileProgress (

	@SerializedName("my_gp") val my_gp : Boolean,
	@SerializedName("my_bio") val my_bio : Boolean,
	@SerializedName("photo_id") val photo_id : Boolean,
	@SerializedName("exemption") val exemption : Boolean
)