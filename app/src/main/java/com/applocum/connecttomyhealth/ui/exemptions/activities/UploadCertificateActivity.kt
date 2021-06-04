package com.applocum.connecttomyhealth.ui.exemptions.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_choose_exemption.*
import kotlinx.android.synthetic.main.activity_upload_certificate.*
import kotlinx.android.synthetic.main.activity_upload_certificate.ivBack
import java.util.concurrent.TimeUnit

class UploadCertificateActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }
    }

    override fun getLayoutResourceId(): Int= R.layout.activity_upload_certificate
}