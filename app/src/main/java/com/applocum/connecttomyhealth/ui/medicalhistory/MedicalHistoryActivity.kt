package com.applocum.connecttomyhealth.ui.medicalhistory

import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_medical_history.*
import kotlinx.android.synthetic.main.custom_medical_history.*

class MedicalHistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        btnAddMedicalHistory.setOnClickListener {
             startActivity(Intent(this,AddMedicalHistoryActivity::class.java))
        }
    }
    override fun getLayoutResourceId(): Int =R.layout.activity_medical_history
}