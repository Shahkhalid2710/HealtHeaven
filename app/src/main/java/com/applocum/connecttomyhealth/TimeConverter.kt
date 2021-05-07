package com.applocum.connecttomyhealth

import java.text.SimpleDateFormat
import java.util.*

fun convertAvailableTimeSlots(time: String): String {
    val defaultSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+00:00", Locale.getDefault())
    defaultSdf.timeZone = TimeZone.getTimeZone("UTC")
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(defaultSdf.parse(time))
}

fun convertTime(time: String): String {
    val defaultSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    defaultSdf.timeZone = TimeZone.getTimeZone("UTC")
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(defaultSdf.parse(time))
}