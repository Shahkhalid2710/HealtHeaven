package com.applocum.connecttomyhealth.ui.help.activities

import android.annotation.SuppressLint
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_help.*
import java.util.concurrent.TimeUnit

class HelpActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int= R.layout.activity_help

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }
    }
}