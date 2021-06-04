package com.applocum.connecttomyhealth.ui.profiledetails.models

import com.google.gson.annotations.SerializedName

data class Addresses(

    @SerializedName("primary") val primary: Primary,
    @SerializedName("secondary") val secondary: List<String>
)