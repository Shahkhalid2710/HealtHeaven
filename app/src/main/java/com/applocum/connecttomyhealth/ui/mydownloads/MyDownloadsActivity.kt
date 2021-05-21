package com.applocum.connecttomyhealth.ui.mydownloads

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.fitnote.FitNoteActivity
import com.applocum.connecttomyhealth.ui.othernotes.OtherNoteActivity
import com.applocum.connecttomyhealth.ui.prescription.models.PrescriptionActivity
import com.applocum.connecttomyhealth.ui.referral.ReferralActivity
import kotlinx.android.synthetic.main.activity_my_downloads.*

class MyDownloadsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        llPrescription.setOnClickListener { startActivity(Intent(this,PrescriptionActivity::class.java)) }
        llFitNote.setOnClickListener { startActivity(Intent(this,FitNoteActivity::class.java)) }
        llReferral.setOnClickListener { startActivity(Intent(this,ReferralActivity::class.java)) }
        llotherNotes.setOnClickListener { startActivity(Intent(this,OtherNoteActivity::class.java)) }

    }

    override fun getLayoutResourceId(): Int =R.layout.activity_my_downloads
}