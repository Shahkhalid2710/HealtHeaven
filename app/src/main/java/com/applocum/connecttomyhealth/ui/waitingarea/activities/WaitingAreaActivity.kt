package com.applocum.connecttomyhealth.ui.waitingarea.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_waiting_area.*
import java.util.concurrent.TimeUnit

class WaitingAreaActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_waiting_area
    override fun handleInternetConnectivity(isConnect: Boolean?) {

    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe { finish() }
    }
}