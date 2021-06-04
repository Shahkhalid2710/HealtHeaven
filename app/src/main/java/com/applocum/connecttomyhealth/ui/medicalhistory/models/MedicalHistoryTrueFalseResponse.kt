package com.applocum.connecttomyhealth.ui.medicalhistory.models

import com.google.gson.annotations.SerializedName

data class MedicalHistoryTrueFalseResponse(
    @SerializedName("medical_history") val medical_history: MedicalHistoryTrueFalse
)