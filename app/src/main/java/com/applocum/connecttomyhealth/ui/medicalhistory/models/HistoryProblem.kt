package com.applocum.connecttomyhealth.ui.medicalhistory.models
import com.google.gson.annotations.SerializedName

data class HistoryProblem (

	@SerializedName("id") val id : Int,
	@SerializedName("description") val description : String,
	@SerializedName("category") val category : String
)