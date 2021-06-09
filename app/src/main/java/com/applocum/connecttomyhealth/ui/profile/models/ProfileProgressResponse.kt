package com.applocum.connecttomyhealth.ui.profile.models
import com.applocum.connecttomyhealth.ui.profiledetails.models.ProfileProgress
import com.google.gson.annotations.SerializedName

data class ProfileProgressResponse (

	@SerializedName("profile_progress") val profile_progress : ProfileProgress,
	@SerializedName("total_details_count") val total_details_count : Int,
	@SerializedName("total_filled_details") val total_filled_details : Int
)