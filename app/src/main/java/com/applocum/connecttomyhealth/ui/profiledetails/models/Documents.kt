package com.applocum.connecttomyhealth.ui.profiledetails.models
import com.google.gson.annotations.SerializedName


data class Documents (

	@SerializedName("id") val id : Int,
	@SerializedName("file_url") val file_url : String,
	@SerializedName("file_file_name") val file_file_name : String
)