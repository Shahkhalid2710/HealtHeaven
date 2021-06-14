@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.applocum.connecttomyhealth

import android.graphics.Typeface
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

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


fun convertSimpleFormat(date: String): String {
    val defaultSdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    defaultSdf.timeZone = TimeZone.getTimeZone("UTC")
    val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return sdf.format(defaultSdf.parse(date))
}


fun convertInvestigationDate(date: String): String {
    val defaultSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    defaultSdf.timeZone = TimeZone.getTimeZone("UTC")
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(defaultSdf.parse(date))
}


fun convertDocumentTime(time: String): String {
    val defaultSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    defaultSdf.timeZone = TimeZone.getTimeZone("UTC")
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    return sdf.format(defaultSdf.parse(time))
}


fun capitalize(capString: String): String? {
    val capBuffer = StringBuffer()
    val capMatcher: Matcher =
        Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
    while (capMatcher.find()) {
        capMatcher.appendReplacement(
            capBuffer,
            capMatcher.group(1).toUpperCase(Locale.ROOT) + capMatcher.group(2).toLowerCase(
                Locale.ROOT
            )
        )
    }
    return capMatcher.appendTail(capBuffer).toString()
}

fun Snackbar.changeFont()
{
    val tv = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    val font = Typeface.createFromAsset(context.assets, "fonts/montserrat_regular.ttf")
    tv.typeface = font
}

fun convertCardDate(date: String): String {
    val defaultSdf = SimpleDateFormat("MMyy", Locale.getDefault())
    defaultSdf.timeZone = TimeZone.getTimeZone("UTC")
    val sdf = SimpleDateFormat("MM/yy", Locale.getDefault())
    return sdf.format(defaultSdf.parse(date))
}
