package com.applocum.connecttomyhealth.ui.medicalhistory.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MedicalHistoryResponse(
    @SerializedName("medical_history") val medical_history: MedicalHistory
) : Serializable