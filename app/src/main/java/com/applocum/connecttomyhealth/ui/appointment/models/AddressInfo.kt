package com.applocum.connecttomyhealth.ui.appointment.models
import com.google.gson.annotations.SerializedName

data class AddressInfo (

	@SerializedName("id") val id : Int,
	@SerializedName("phone") val phone : String,
	@SerializedName("line1") val line1 : String,
	@SerializedName("line2") val line2 : String,
	@SerializedName("pincode") val pincode : String,
	@SerializedName("user_id") val user_id : Int,
	@SerializedName("created_at") val created_at : String,
	@SerializedName("updated_at") val updated_at : String,
	@SerializedName("town") val town : String,
	@SerializedName("line3") val line3 : String,
	@SerializedName("label") val label : String,
	@SerializedName("name") val name : String,
	@SerializedName("status") val status : String,
	@SerializedName("is_default") val is_default : Boolean,
	@SerializedName("latitude") val latitude : Double,
	@SerializedName("longitude") val longitude : Double,
	@SerializedName("country") val country : String
)