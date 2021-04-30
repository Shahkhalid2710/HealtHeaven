package com.applocum.connecttomyhealth.ui.booksession.models
import com.google.gson.annotations.SerializedName

data class Common (

	@SerializedName("delivery") val delivery : Int,
	@SerializedName("sick_note_charge") val sick_note_charge : Double,
	@SerializedName("prescription_charge") val prescription_charge : Double,
	@SerializedName("referral_note_charge") val referral_note_charge : Double,
	@SerializedName("appointment_basic_price") val appointment_basic_price : Int,
	@SerializedName("appointment_refund_fees") val appointment_refund_fees : Int,
	@SerializedName("exceeded_fair_usage_price") val exceeded_fair_usage_price : Int,
	@SerializedName("non_joined_waiting_room_fee") val non_joined_waiting_room_fee : Int,
	@SerializedName("is_first_appointment_done") val is_first_appointment_done : Boolean
)