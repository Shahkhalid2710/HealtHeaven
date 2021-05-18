package com.applocum.connecttomyhealth.ui.medicalhistory.models
import com.google.gson.annotations.SerializedName

data class MedicalHistoryTrueFalse (
	@SerializedName("true") val trueMedicalHistory : ArrayList<TrueMedicalHistory> = ArrayList(),
	@SerializedName("false") val falseMedicalHistory : ArrayList<FalseMedicalHistory> = ArrayList()
)