package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_verify_identity.*

class VerifyIdentityActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rlValidPassport.setOnClickListener {
            startActivity(Intent(this,ValidPassportActivity::class.java))
        }
        rlDrivingLicense.setOnClickListener {
            startActivity(Intent(this,UkDrivingLicenseActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int= R.layout.activity_verify_identity
}