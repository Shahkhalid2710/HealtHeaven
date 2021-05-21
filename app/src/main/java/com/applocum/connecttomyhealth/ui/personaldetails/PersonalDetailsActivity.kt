package com.applocum.connecttomyhealth.ui.personaldetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.exemptions.ExemptionsActivity
import com.applocum.connecttomyhealth.ui.mygp.GpServiceActivity
import com.applocum.connecttomyhealth.ui.mygp.MyGPActivity
import com.applocum.connecttomyhealth.ui.profiledetails.ProfileDetailsActivity
import kotlinx.android.synthetic.main.activity_personal_details.*

class PersonalDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener {
            finish()
        }
        rlProfileddetails.setOnClickListener {
            startActivity(Intent(this,ProfileDetailsActivity::class.java))
        }
        rlMyGp.setOnClickListener {
            startActivity(Intent(this,GpServiceActivity::class.java))
        }
        rlExemptions.setOnClickListener {
            startActivity(Intent(this,ExemptionsActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_personal_details
}