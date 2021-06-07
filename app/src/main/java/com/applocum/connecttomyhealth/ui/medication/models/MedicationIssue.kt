package com.applocum.connecttomyhealth.ui.medication.models
import com.google.gson.annotations.SerializedName

data class MedicationIssue (
	@SerializedName("current") val current : ArrayList<String>,
	@SerializedName("past") val past :ArrayList<String>
)