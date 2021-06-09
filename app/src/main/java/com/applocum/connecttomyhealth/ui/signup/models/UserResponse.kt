package com.applocum.connecttomyhealth.ui.signup.models
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.applocum.connecttomyhealth.ui.profiledetails.models.Documents
import com.applocum.connecttomyhealth.ui.profiledetails.models.ProfileProgress
import com.google.gson.annotations.SerializedName


data class UserResponse (
	@SerializedName("image") val image : String,
	@SerializedName("gp_service") val gp_service : GpService,
	@SerializedName("documents") val documents : ArrayList<Documents>,
	@SerializedName("profile_progress") val profile_progress : ProfileProgress,
	@SerializedName("user") val user : User
)