package com.applocum.connecttomyhealth.ui.personaldetails

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.exemptions.ExemptionsActivity
import com.applocum.connecttomyhealth.ui.mygp.GpServiceActivity
import com.applocum.connecttomyhealth.ui.mygp.MyGPActivity
import com.applocum.connecttomyhealth.ui.profiledetails.ProfileDetailsActivity
import com.applocum.connecttomyhealth.ui.settings.SettingActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_personal_details.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.concurrent.TimeUnit

class PersonalDetailsActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        RxView.clicks(rlProfileddetails).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,ProfileDetailsActivity::class.java))
            }

        RxView.clicks(rlMyGp).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,GpServiceActivity::class.java))
            }

        RxView.clicks(rlExemptions).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,ExemptionsActivity::class.java))
            }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_personal_details
}