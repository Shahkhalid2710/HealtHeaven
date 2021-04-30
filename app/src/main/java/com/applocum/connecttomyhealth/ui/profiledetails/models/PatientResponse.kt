package com.applocum.connecttomyhealth.ui.profiledetails.models
import com.google.gson.annotations.SerializedName

data class PatientResponse (
	@SerializedName("patient") val patient : Patient
)