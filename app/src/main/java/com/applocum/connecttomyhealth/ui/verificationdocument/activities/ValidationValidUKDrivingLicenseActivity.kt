package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_validation_valid_u_k_driving_license.btnRetakePhoto
import kotlinx.android.synthetic.main.activity_validation_valid_u_k_driving_license.btnSubmit
import kotlinx.android.synthetic.main.activity_validation_valid_u_k_driving_license.ivBack

class ValidationValidUKDrivingLicenseActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            startActivity(Intent(this,VerifiedActivity::class.java))
            finish()
        }
        btnRetakePhoto.setOnClickListener {
            startActivity(Intent(this,ValidPassportActivity::class.java))
            finish()
        }
    }

    override fun getLayoutResourceId(): Int= R.layout.activity_validation_valid_u_k_driving_license
}