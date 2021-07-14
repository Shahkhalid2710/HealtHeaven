package com.applocum.connecttomyhealth.ui.passwordsecurity.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_password_security.*
import java.util.concurrent.TimeUnit

class PasswordSecurityActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_password_security

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(tvNotNow).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe { finish() }
    }
}