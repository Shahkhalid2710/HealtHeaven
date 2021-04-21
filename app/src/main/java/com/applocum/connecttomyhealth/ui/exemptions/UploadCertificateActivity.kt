package com.applocum.connecttomyhealth.ui.exemptions

import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_upload_certificate.*

class UploadCertificateActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
    }

    override fun getLayoutResourceId(): Int= R.layout.activity_upload_certificate
}