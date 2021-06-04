package com.applocum.connecttomyhealth.ui.medicalhistory.models

import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MedicalHistory(

    @SerializedName("id") val id: Int,
    @SerializedName("is_active") val is_active: Boolean,
    @SerializedName("start_date") val start_date: String,
    @SerializedName("end_date") val end_date: String,
    @SerializedName("is_deleted") val is_deleted: Boolean,
    @SerializedName("deleted_by") val deleted_by: String,
    @SerializedName("latest_reported_by") val latest_reported_by: String,
    @SerializedName("latest_reported_on") val latest_reported_on: String,
    @SerializedName("status") val status: String,
    @SerializedName("history_problem") val history_problem: HistoryProblem,
    @SerializedName("patient") val patient: Patient
) : Serializable