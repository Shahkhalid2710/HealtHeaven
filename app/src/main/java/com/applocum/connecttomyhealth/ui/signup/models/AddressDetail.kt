package com.applocum.connecttomyhealth.ui.signup.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddressDetail (
	@SerializedName("address") val address : Address
):Serializable