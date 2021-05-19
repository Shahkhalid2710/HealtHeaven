package com.applocum.connecttomyhealth.ui.investigation.models
import com.google.gson.annotations.SerializedName

data class Investigation (

	@SerializedName("id") val id : Int,
	@SerializedName("snomed_code") val snomed_code : String,
	@SerializedName("description") val description : String,
	@SerializedName("taken_on") val taken_on : String,
	@SerializedName("created_by") val created_by : String
)