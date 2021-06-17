package com.applocum.connecttomyhealth.ui.medicalhistory.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.adapters.ViewPagerFragmentAdapter
import com.applocum.connecttomyhealth.ui.medicalhistory.fragments.ActiveMedicalHistoryFragment
import com.applocum.connecttomyhealth.ui.medicalhistory.fragments.PastMedicalHistoryFragment
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_medical_history.*
import kotlinx.android.synthetic.main.activity_medical_history.ivBack
import kotlinx.android.synthetic.main.custom_medical_history.*
import java.util.concurrent.TimeUnit

class MedicalHistoryActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_medical_history

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(btnAddMedicalHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddMedicalHistoryActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(tvAddMedicalHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddMedicalHistoryActivity::class.java))
                overridePendingTransition(0,0)
            }

        val viewPagerFragmentAdapter = ViewPagerFragmentAdapter(this, supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(ActiveMedicalHistoryFragment(), "Active")
        viewPagerFragmentAdapter.addfragment(PastMedicalHistoryFragment(), "Past ")
        viewPager.adapter = viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }
}