package com.applocum.connecttomyhealth.ui.medicalhistory

import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.ViewPagerFragmentAdapter
import com.applocum.connecttomyhealth.ui.medication.ActiveAcuteMedicationIssueFragment
import com.applocum.connecttomyhealth.ui.medication.PastAcuteMedicationIssueFragment
import kotlinx.android.synthetic.main.activity_medical_history.*
import kotlinx.android.synthetic.main.custom_medical_history.*

class MedicalHistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        btnAddMedicalHistory.setOnClickListener {
             startActivity(Intent(this,AddMedicalHistoryActivity::class.java))
        }
        tvAddMedicalHistory.setOnClickListener {
            startActivity(Intent(this,AddMedicalHistoryActivity::class.java))
        }
        val viewPagerFragmentAdapter= ViewPagerFragmentAdapter(this,supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(ActiveMedicalHistoryFragment(),"Active")
        viewPagerFragmentAdapter.addfragment(PastMedicalHistoryFragment(),"Past ")
        viewPager.adapter=viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }
    override fun getLayoutResourceId(): Int =R.layout.activity_medical_history
}