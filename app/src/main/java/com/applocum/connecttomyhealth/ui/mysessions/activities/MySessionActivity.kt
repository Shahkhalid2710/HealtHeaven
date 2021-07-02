package com.applocum.connecttomyhealth.ui.mysessions.activities

import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity

class MySessionActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_my_session
    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}