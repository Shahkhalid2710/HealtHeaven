package com.applocum.connecttomyhealth.ui.profiledetails.models

import com.google.gson.annotations.SerializedName

data class Primary (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("line1") val line1 : String,
	@SerializedName("line2") val line2 : String,
	@SerializedName("line3") val line3 : String,
	@SerializedName("town") val town : String,
	@SerializedName("pincode") val pincode : String,
	@SerializedName("phone") val phone :String,
	@SerializedName("country_code") val country_code : Int,
	@SerializedName("phone_number") val phone_number : String,
	@SerializedName("label") val label : String,
	@SerializedName("is_primary") val is_primary : Boolean,
	@SerializedName("is_default") val is_default : Boolean
)