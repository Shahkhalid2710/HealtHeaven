package com.applocum.connecttomyhealth.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.walkthrough.WalkThroughActivity
import javax.inject.Inject

class SplashScreenActivity : BaseActivity() {
    override fun getLayoutResourceId(): Int= R.layout.activity_splash_screen

    @Inject
    lateinit var userHolder: UserHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)

        Handler(Looper.getMainLooper()).postDelayed({
            if (userHolder.userToken.isNullOrBlank())
            {
                val intent= Intent(this,WalkThroughActivity::class.java)
                startActivity(intent)
                finish()
            }
          else
            {
                val intent= Intent(this,BottomNavigationViewActivity::class.java)
                startActivity(intent)
                finish()
            }
        },1000)
    }

}