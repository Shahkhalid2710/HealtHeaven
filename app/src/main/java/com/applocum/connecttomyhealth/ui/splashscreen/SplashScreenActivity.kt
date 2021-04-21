package com.applocum.connecttomyhealth.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.BottomNavigationViewActivity

class SplashScreenActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent= Intent(this, BottomNavigationViewActivity::class.java)
            startActivity(intent)
            finish()
        },1000)
    }

    override fun getLayoutResourceId(): Int= R.layout.activity_splash_screen
}