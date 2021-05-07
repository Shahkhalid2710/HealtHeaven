package com.applocum.connecttomyhealth.ui.appointment.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BookAppointmentResponse (

	@SerializedName("id") val id : Int,
	@SerializedName("start_time") val start_time : String,
	@SerializedName("end_time") val end_time : String,
	@SerializedName("status") val status : String,
	@SerializedName("appointment_price") val appointment_price : Int,
	@SerializedName("duration") val duration : Int,
	@SerializedName("activate_waiting_room") val activate_waiting_room : Boolean,
	@SerializedName("is_unpaid") val is_unpaid : Boolean,
	@SerializedName("doc_preference") val doc_preference : String,
	@SerializedName("appointment_booked_by") val appointment_booked_by : String,
	@SerializedName("patient_detail") val patient_detail :PatientDetail,
	@SerializedName("invoice") val invoice : String,
	@SerializedName("gp_details") val gp_details : GpDetails=GpDetails(),
	@SerializedName("comments") val comments : String,
	@SerializedName("reason_image") val reason_image : String,
	@SerializedName("reason_text") val reason_text : String,
	@SerializedName("is_cancellable") val is_cancellable : Boolean,
	@SerializedName("price_without_discount") val price_without_discount : Int,
	@SerializedName("offline_appointment") val offline_appointment : Boolean,
	@SerializedName("has_patient_join") val has_patient_join : Boolean,
	@SerializedName("conducted_by") val conducted_by : String,
	@SerializedName("corporate_id") val corporate_id : Int,
	@SerializedName("corporate_name") val corporate_name : String,
	@SerializedName("appointment_type") val appointment_type : String,
	@SerializedName("membership_code") val membership_code : String,
	@SerializedName("membership_plan_name") val membership_plan_name : String,
	@SerializedName("patient_dob") val patient_dob : String,
	@SerializedName("gp_designation") val gp_designation : String,
	@SerializedName("address_info") val address_info :AddressInfo,
):Serializable