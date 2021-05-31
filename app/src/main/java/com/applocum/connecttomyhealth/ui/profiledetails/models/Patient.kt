package com.applocum.connecttomyhealth.ui.profiledetails.models
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.applocum.connecttomyhealth.ui.signup.models.Organization
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.google.gson.annotations.SerializedName


data class Patient (

	@SerializedName("id") val id : Int,
	@SerializedName("first_name") val first_name : String,
	@SerializedName("nhs_number") val nhs_number : String,
	@SerializedName("last_name") val last_name : String,
	@SerializedName("email") val email : String,
	@SerializedName("phone") val phone :String,
	@SerializedName("image") val image : String,
	@SerializedName("gender") val gender : String,
	@SerializedName("date_of_birth") val date_of_birth : String,
	@SerializedName("age") val age : Int,
	@SerializedName("preferred_language") val preferred_language : String,
	@SerializedName("height_category") val height_category : String,
	@SerializedName("height_value1") val height_value1 : String,
	@SerializedName("height_value2") val height_value2 : String,
	@SerializedName("weight_category") val weight_category : String,
	@SerializedName("weight_value1") val weight_value1 : String,
	@SerializedName("weight_value2") val weight_value2 : String,
	@SerializedName("gp_service") val gp_service : GpService,
	@SerializedName("addresses") val addresses : Addresses,
	@SerializedName("documents") val documents : List<String>,
	@SerializedName("smoke") val smoke : String,
	@SerializedName("alcohol") val alcohol : String,
	@SerializedName("blood_pressure") val blood_pressure : String,
	@SerializedName("bmi") val bmi : String,
	@SerializedName("morning_time") val morning_time : String,
	@SerializedName("noon_time") val noon_time : String,
	@SerializedName("evening_time") val evening_time : String,
	@SerializedName("night_time") val night_time : String,
	@SerializedName("organization") val organization : Organization,
	@SerializedName("restore_id") val restore_id : String,
	@SerializedName("profile_progress") val profile_progress : ProfileProgress,
	@SerializedName("total_details_count") val total_details_count : Int,
	@SerializedName("total_filled_details") val total_filled_details : Int,
	@SerializedName("is_health_summary_exist") val is_health_summary_exist : Boolean,
	@SerializedName("email_verified") val email_verified : Boolean,
	@SerializedName("phone_verified") val phone_verified : Boolean,
	@SerializedName("user") val user : User,
	@SerializedName("corporate_id") val corporate_id : Int,
	@SerializedName("corporate_name") val corporate_name : String,
	@SerializedName("phone_detail") val phone_detail : PhoneDetail
)