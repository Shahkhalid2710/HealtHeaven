package com.applocum.connecttomyhealth.ui.investigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_investigation.*

class InvestigationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        tvAddInvestigation.setOnClickListener {
            startActivity(Intent(this,AddInvestigationActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_investigation
}