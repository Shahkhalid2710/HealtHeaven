package com.applocum.connecttomyhealth.ui.mydownloads.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.fitnote.activities.FitNoteActivity
import com.applocum.connecttomyhealth.ui.othernotes.activities.OtherNoteActivity
import com.applocum.connecttomyhealth.ui.prescription.activities.PrescriptionActivity
import com.applocum.connecttomyhealth.ui.referral.activities.ReferralActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_my_downloads.*
import java.util.concurrent.TimeUnit

class MyDownloadsActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_my_downloads
    override fun handleInternetConnectivity(isConnect: Boolean?) {

    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(llPrescription).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,PrescriptionActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(llFitNote).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, FitNoteActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(llReferral).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, ReferralActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(llotherNotes).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, OtherNoteActivity::class.java))
                overridePendingTransition(0,0)
            }
     }
}