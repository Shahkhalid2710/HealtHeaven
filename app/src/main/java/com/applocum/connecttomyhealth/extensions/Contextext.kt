package com.applocum.connecttomyhealth.extensions

import android.content.Context
import android.content.SharedPreferences

fun Context.getPrefInstance(prefName: String): SharedPreferences =
    this.getSharedPreferences(prefName, android.content.Context.MODE_PRIVATE)