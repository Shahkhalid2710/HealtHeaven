package com.applocum.connecttomyhealth.ui.booksession.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Common (
	@SerializedName("delivery") val delivery : Int=0,
	@SerializedName("sick_note_charge") val sick_note_charge : Double=0.0,
	@SerializedName("prescription_charge") val prescription_charge : Double=0.0,
	@SerializedName("referral_note_charge") val referral_note_charge : Double=0.0,
	@SerializedName("appointment_basic_price") val appointment_basic_price : Int=0,
	@SerializedName("appointment_refund_fees") val appointment_refund_fees : Int=0,
	@SerializedName("exceeded_fair_usage_price") val exceeded_fair_usage_price : Int=0,
	@SerializedName("non_joined_waiting_room_fee") val non_joined_waiting_room_fee : Int=0,
	@SerializedName("is_first_appointment_done") val is_first_appointment_done : Boolean=false
):Serializable