package com.applocum.connecttomyhealth.ui.securitycheck.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Security (
	@SerializedName("token") val token : String
):Serializable