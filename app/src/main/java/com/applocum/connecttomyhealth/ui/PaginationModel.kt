package com.applocum.connecttomyhealth.ui

import com.google.gson.annotations.SerializedName

data class PaginationModel (
    @SerializedName("total") val total : Int,
    @SerializedName("total_pages") val totalPages : Int,
    @SerializedName("first_page") val firstPage : Boolean,
    @SerializedName("last_page") val lastPage : Boolean,
    @SerializedName("previous_page") val previousPage : String,
    @SerializedName("next_page") val nextPage :String?,
    @SerializedName("out_of_bounds") val outOfBounds :Boolean,
    @SerializedName("offset") val offSet :Int,
)