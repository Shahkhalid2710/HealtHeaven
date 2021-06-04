package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_verify_identity.*
import java.util.concurrent.TimeUnit

class VerifyIdentityActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int= R.layout.activity_verify_identity

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(rlValidPassport).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,ValidPassportActivity::class.java))
            }

        RxView.clicks(rlDrivingLicense).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,UkDrivingLicenseActivity::class.java))
            }
    }
}