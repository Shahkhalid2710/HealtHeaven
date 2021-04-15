package com.applocum.connecttomyhealth.ui.exemptions

import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_exemptions.*
import kotlinx.android.synthetic.main.activity_exemptions.ivBack
import kotlinx.android.synthetic.main.custom_exemption_xml.view.*

class ExemptionsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        exemptions.btnAddCertificate.setOnClickListener {
            startActivity(Intent(this,ChooseExemptionActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_exemptions
}