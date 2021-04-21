package com.applocum.connecttomyhealth.ui.sessiondetails

import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_session_details.*

class SessionDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

    }

    override fun getLayoutResourceId(): Int =R.layout.activity_session_details
}