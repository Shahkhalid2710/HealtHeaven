package com.applocum.connecttomyhealth.ui.specialists.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Specialist (
	@SerializedName("id") val id : Int=0,
	@SerializedName("first_name") val first_name : String="",
	@SerializedName("last_name") val last_name : String="",
	@SerializedName("email") val email : String="",
	@SerializedName("gender") val gender : String="",
	@SerializedName("image") val image : String="",
	@SerializedName("bio") val bio : String="",
	@SerializedName("doctor_specialities") val doctor_specialities : List<String> = ArrayList(),
	@SerializedName("languages") val languages : List<String> = ArrayList(),
	@SerializedName("designation") val designation : String="",
	@SerializedName("corporate_organization_name") val corporate_organization_name : String="",
	@SerializedName("usual_address") val usual_address : UsualAddress=UsualAddress(),
	@SerializedName("distance") val distance : String="",
	@SerializedName("doc_type") val doc_type : String=""
):Serializable