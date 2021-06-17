package com.applocum.connecttomyhealth.ui.exemptions.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_exemptions.*
import kotlinx.android.synthetic.main.activity_exemptions.ivBack
import kotlinx.android.synthetic.main.custom_exemption_xml.view.*
import java.util.concurrent.TimeUnit

class ExemptionsActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_exemptions

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(exemptions.btnAddCertificate).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, ChooseExemptionActivity::class.java))
                overridePendingTransition(0,0)
            }
     }
}