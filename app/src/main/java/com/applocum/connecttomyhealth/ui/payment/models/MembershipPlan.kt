package com.applocum.connecttomyhealth.ui.payment.models
import com.google.gson.annotations.SerializedName

data class MembershipPlan (

	@SerializedName("feature_price_list") val feature_price_list : ArrayList<FeaturePriceList>
)