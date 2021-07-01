package com.applocum.connecttomyhealth.ui.appointment.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BookAppointmentResponse(

    @SerializedName("id") val id: Int = 0,
    @SerializedName("start_time") val start_time: String? = "",
    @SerializedName("end_time") val end_time: String? = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("appointment_price") val appointment_price: Double = 0.0,
    @SerializedName("duration") val duration: Int = 0,
    @SerializedName("activate_waiting_room") val activate_waiting_room: Boolean= false,
    @SerializedName("is_unpaid") val is_unpaid: Boolean = false,
    @SerializedName("doc_preference") val doc_preference: String = "",
    @SerializedName("appointment_booked_by") val appointment_booked_by: String = "",
    @SerializedName("patient_detail") val patient_detail: PatientDetail = PatientDetail(),
    @SerializedName("invoice") val invoice: String = "",
    @SerializedName("gp_details") val gp_details: GpDetails = GpDetails(),
    @SerializedName("comments") val comments: String = "",
    @SerializedName("reason_image") val reason_image: String = "",
    @SerializedName("reason_text") val reason_text: String = "",
    @SerializedName("is_cancellable") val is_cancellable: Boolean = false,
    @SerializedName("price_without_discount") val price_without_discount: Int = 0,
    @SerializedName("offline_appointment") val offline_appointment: Boolean = false,
    @SerializedName("has_patient_join") val has_patient_join: Boolean = false,
    @SerializedName("conducted_by") val conducted_by: String = "",
    @SerializedName("corporate_id") val corporate_id: Int = 0,
    @SerializedName("corporate_name") val corporate_name: String = "",
    @SerializedName("appointment_type") val appointment_type: String = "",
    @SerializedName("membership_code") val membership_code: String = "",
    @SerializedName("actual_start_time") val actual_start_time: String = "",
    @SerializedName("actual_end_time") val actual_end_time: String = "",
    @SerializedName("call_summary_pdf") val call_summary_pdf: String = "",
    @SerializedName("membership_plan_name") val membership_plan_name: String = "",
    @SerializedName("patient_dob") val patient_dob: String = "",
    @SerializedName("gp_designation") val gp_designation: String = "",
    @SerializedName("address_info") val address_info: AddressInfo = AddressInfo(),
) : Serializable