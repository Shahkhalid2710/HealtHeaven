package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_verified.*

class VerifiedActivity : BaseActivity() {
    override fun getLayoutResourceId(): Int = R.layout.activity_verified

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnContinue.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
      //  super.onBackPressed()
    }
}