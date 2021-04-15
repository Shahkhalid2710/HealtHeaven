package com.applocum.connecttomyhealth.ui.mygp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_my_g_p.*
import kotlinx.android.synthetic.main.custom_mygp_xml.*

class MyGPActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }
        tvAddMyGp.setOnClickListener {
            startActivity(Intent(this,AddGPServiceActivity::class.java))
        }
        btnAddGpService.setOnClickListener {
            startActivity(Intent(this,AddGPServiceActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_my_g_p
}