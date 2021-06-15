package com.applocum.connecttomyhealth.ui.profiledetails.models
import com.google.gson.annotations.SerializedName


data class PhoneDetail (
	@SerializedName("country_code") val country_code : Int?=0,
	@SerializedName("number") val number: String?=""
)