package com.applocum.connecttomyhealth.ui.allergyhistory.models
import com.google.gson.annotations.SerializedName

data class TrueAllergy (
	@SerializedName("id") val id : Int,
	@SerializedName("is_active") val is_active : Boolean,
	@SerializedName("status") val status : String,
	@SerializedName("is_deleted") val is_deleted : Boolean,
	@SerializedName("deleted_by") val deleted_by : String,
	@SerializedName("latest_reported_by") val latest_reported_by : String,
	@SerializedName("latest_reported_on") val latest_reported_on : String,
	@SerializedName("allergy") val allergy : Allergy
)