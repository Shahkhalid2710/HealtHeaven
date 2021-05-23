package com.applocum.connecttomyhealth.ui.payment.models
import com.google.gson.annotations.SerializedName

data class FeaturePriceList (

	@SerializedName("label") val label : String,
	@SerializedName("price") val price : Int,
	@SerializedName("feature") val feature : String
)