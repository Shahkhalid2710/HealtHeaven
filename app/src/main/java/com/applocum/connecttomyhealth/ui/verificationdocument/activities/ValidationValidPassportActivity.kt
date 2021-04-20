package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_validation_valid_passport.*
import java.io.ByteArrayOutputStream

class ValidationValidPassportActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }
        btnSubmit.setOnClickListener {
            startActivity(Intent(this,VerifyIdentityActivity::class.java))
        }
        btnRetakePhoto.setOnClickListener {
            startActivity(Intent(this,ValidPassportActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_validation_valid_passport
}