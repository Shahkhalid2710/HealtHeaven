package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_uk_driving_license.*

class UkDrivingLicenseActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
    }

    override fun getLayoutResourceId(): Int=R.layout.activity_uk_driving_license
}