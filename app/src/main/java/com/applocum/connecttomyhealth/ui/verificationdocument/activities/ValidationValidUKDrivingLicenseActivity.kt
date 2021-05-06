package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_validation_valid_u_k_driving_license.*
import kotlinx.android.synthetic.main.activity_validation_valid_u_k_driving_license.btnRetakePhoto
import kotlinx.android.synthetic.main.activity_validation_valid_u_k_driving_license.btnSubmit
import kotlinx.android.synthetic.main.activity_validation_valid_u_k_driving_license.ivBack

class ValidationValidUKDrivingLicenseActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        val selectedImagePath=intent.getStringExtra("image")

        btnSubmit.setOnClickListener {
            startActivity(Intent(this,VerifiedActivity::class.java))
            finish()
        }
        btnRetakePhoto.setOnClickListener {
            startActivity(Intent(this,ValidPassportActivity::class.java))
            finish()
        }
        Glide.with(this).load(selectedImagePath).into(ivDrivingLicense)

    }

    override fun getLayoutResourceId(): Int= R.layout.activity_validation_valid_u_k_driving_license
}