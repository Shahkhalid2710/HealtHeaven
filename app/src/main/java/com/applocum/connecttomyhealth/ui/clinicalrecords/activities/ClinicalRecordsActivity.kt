package com.applocum.connecttomyhealth.ui.clinicalrecords.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.activities.AllergyHistoryActivity
import com.applocum.connecttomyhealth.ui.familyhistory.activities.FamilyHistoryActivity
import com.applocum.connecttomyhealth.ui.investigation.activities.InvestigationActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.activities.MedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.medication.activities.MedicationActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_clinical_records.*
import kotlinx.android.synthetic.main.activity_clinical_records.ivBack
import java.util.concurrent.TimeUnit

class ClinicalRecordsActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_clinical_records

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(rlFamilyHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,FamilyHistoryActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(rlAllergyHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AllergyHistoryActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(rlInvestigation).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, InvestigationActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(rlMedicalBased).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, MedicationActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(rlMedicalHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, MedicalHistoryActivity::class.java))
                overridePendingTransition(0,0)
            }
    }
}