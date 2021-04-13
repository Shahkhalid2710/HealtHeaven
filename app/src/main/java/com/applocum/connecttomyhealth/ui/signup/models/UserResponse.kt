package com.applocum.connecttomyhealth.ui.signup.models

import com.google.gson.annotations.SerializedName

data class UserResponse (
	@SerializedName("user") val user : User
)