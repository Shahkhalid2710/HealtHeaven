package com.applocum.connecttomyhealth.ui.signup.models
import com.google.gson.annotations.SerializedName

data class Address (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("line1") val line1 : String,
	@SerializedName("line2") val line2 : String,
	@SerializedName("line3") val line3 : String,
	@SerializedName("town") val town : String,
	@SerializedName("pincode") val pincode : String
)