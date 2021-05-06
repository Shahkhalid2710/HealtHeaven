package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.R.attr.path
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_validation_valid_passport.*


class ValidationValidPassportActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

         val selectedImagePath=intent.getStringExtra("image")

        btnSubmit.setOnClickListener {
            startActivity(Intent(this,VerifyIdentityActivity::class.java))
        }
        btnRetakePhoto.setOnClickListener {
            startActivity(Intent(this,ValidPassportActivity::class.java))
        }
        Glide.with(this).load(selectedImagePath).into(ivPassport)
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_validation_valid_passport
}