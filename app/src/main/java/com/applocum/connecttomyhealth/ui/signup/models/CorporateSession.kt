package com.applocum.connecttomyhealth.ui.signup.models

import com.google.gson.annotations.SerializedName

data class CorporateSession(
    @SerializedName("questionnaire_type") val questionnaireType : String
)