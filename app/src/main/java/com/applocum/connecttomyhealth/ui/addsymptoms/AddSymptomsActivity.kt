package com.applocum.connecttomyhealth.ui.addsymptoms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.confirmbooking.ConfirmBookingActivity
import kotlinx.android.synthetic.main.activity_add_symptoms.*

class AddSymptomsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        btnContinue.setOnClickListener {
            startActivity(Intent(this,ConfirmBookingActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int=R.layout.activity_add_symptoms
}