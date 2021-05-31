package com.applocum.connecttomyhealth.ui.profiledetails.models
import com.google.gson.annotations.SerializedName


data class PhoneDetail (
	@SerializedName("country_code") val country_code : Int,
	@SerializedName("number") val number : String
)