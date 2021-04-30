package com.applocum.connecttomyhealth.ui.booksession.models
import com.google.gson.annotations.SerializedName

data class Time (
	@SerializedName("start_time") val start_time : String,
	@SerializedName("end_time") val end_time : String
)