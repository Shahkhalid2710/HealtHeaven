package com.applocum.connecttomyhealth.ui.signup.models

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GlobalResponse(
    @SerializedName("status") var status: Int = 0,
    @SerializedName("success") var success: String = "",
    @SerializedName("message") var message: String = "",
    @SerializedName("data") var data: JsonElement?,
    @SerializedName("common") val common : Common
):Serializable