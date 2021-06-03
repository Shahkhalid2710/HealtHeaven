package com.applocum.connecttomyhealth.ui.mydownloads

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.fitnote.FitNoteActivity
import com.applocum.connecttomyhealth.ui.othernotes.OtherNoteActivity
import com.applocum.connecttomyhealth.ui.prescription.models.PrescriptionActivity
import com.applocum.connecttomyhealth.ui.referral.ReferralActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_my_downloads.*
import java.util.concurrent.TimeUnit

class MyDownloadsActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe{
                finish()
            }

        RxView.clicks(llPrescription).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe{
                startActivity(Intent(this,PrescriptionActivity::class.java))
            }
        RxView.clicks(llFitNote).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe{
                startActivity(Intent(this,FitNoteActivity::class.java))
            }
        RxView.clicks(llReferral).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe{
                startActivity(Intent(this,ReferralActivity::class.java))
            }
        RxView.clicks(llotherNotes).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe{
                startActivity(Intent(this,OtherNoteActivity::class.java))
            }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_my_downloads
}