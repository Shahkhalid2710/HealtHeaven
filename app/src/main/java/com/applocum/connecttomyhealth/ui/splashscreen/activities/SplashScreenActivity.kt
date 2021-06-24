package com.applocum.connecttomyhealth.ui.splashscreen.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.walkthrough.activities.WalkThroughActivity
import javax.inject.Inject

class SplashScreenActivity : BaseActivity() {

    override fun getLayoutResourceId():Int = R.layout.activity_splash_screen
    override fun handleInternetConnectivity(isConnect: Boolean?) {

    }

    @Inject
    lateinit var userHolder: UserHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)

        Handler(Looper.getMainLooper()).postDelayed({
            if (userHolder.userToken.isNullOrBlank())
            {
                val intent= Intent(this, WalkThroughActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(0,0)
            }
          else
            {
                val intent= Intent(this, BottomNavigationViewActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(0,0)
            }
        },1000)
    }
}