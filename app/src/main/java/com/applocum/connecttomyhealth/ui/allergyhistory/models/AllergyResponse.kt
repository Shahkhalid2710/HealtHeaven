package com.applocum.connecttomyhealth.ui.allergyhistory.models

import com.google.gson.annotations.SerializedName

data class AllergyResponse(
    @SerializedName("true") val trueAllergy: ArrayList<TrueAllergy>,
    @SerializedName("false") val falseAllergy: ArrayList<FalseAllergy>
)