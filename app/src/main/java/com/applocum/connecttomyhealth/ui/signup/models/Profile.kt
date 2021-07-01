package com.applocum.connecttomyhealth.ui.signup.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Profile (

	@SerializedName("date_of_birth") val dateOfBirth : String,
	@SerializedName("age") val age : String,
	@SerializedName("height_category") val heightCategory : String,
	@SerializedName("height_value1") val heightValue1 : String,
	@SerializedName("height_value2") val heightValue2 : String,
	@SerializedName("weight_category") val weightCategory : String,
	@SerializedName("weight_value1") val weightValue1 : String,
	@SerializedName("weight_value2") val weightValue2 : String,
	@SerializedName("smoke") val smoke : String,
	@SerializedName("alcohol") val alcohol : String,
	@SerializedName("bmi") val bmi : String,
	@SerializedName("restore_id") val restoreId : String
):Serializable