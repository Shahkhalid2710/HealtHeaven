package com.applocum.connecttomyhealth.ui.familyhistory.models

import com.google.gson.annotations.SerializedName


data class FamilyHistory(
    @SerializedName("id") val id: Int,
    @SerializedName("disorder") val disorder: String
)