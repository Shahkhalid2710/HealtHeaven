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

fun dateTimeUTCFormat(date: String): String {
    val defaultSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+00:00", Locale.getDefault())
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00",Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(defaultSdf.parse(date))
}

fun convertDateTime(time: String): String {
    val defaultSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    defaultSdf.timeZone = TimeZone.getTimeZone("UTC")
    val sdf = SimpleDateFormat("EEEE,dd'th' MMMM yyyy,hh:mm a", Locale.getDefault())
    return sdf.format(defaultSdf.parse(time))
}

fun convertDate(date: String): String {
    val defaultSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    defaultSdf.timeZone = TimeZone.getTimeZone("UTC")
    val sdf = SimpleDateFormat("dd'th' MMMM yyyy", Locale.getDefault())
    return sdf.format(defaultSdf.parse(date))
}

