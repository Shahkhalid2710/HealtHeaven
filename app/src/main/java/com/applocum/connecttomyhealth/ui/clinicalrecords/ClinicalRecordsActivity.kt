package com.applocum.connecttomyhealth.ui.clinicalrecords

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.AllergyHistoryActivity
import com.applocum.connecttomyhealth.ui.familyhistory.FamilyHistoryActivity
import com.applocum.connecttomyhealth.ui.investigation.InvestigationActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.MedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.medication.MedicationActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_clinical_records.*
import kotlinx.android.synthetic.main.activity_clinical_records.ivBack
import java.util.concurrent.TimeUnit

class ClinicalRecordsActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        RxView.clicks(rlFamilyHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,FamilyHistoryActivity::class.java))
            }

        RxView.clicks(rlAllergyHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,AllergyHistoryActivity::class.java))
            }

        RxView.clicks(rlInvestigation).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,InvestigationActivity::class.java))
            }
        RxView.clicks(rlMedicalBased).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,MedicationActivity::class.java))
            }
        RxView.clicks(rlMedicalHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,MedicalHistoryActivity::class.java))
            }

    }

    override fun getLayoutResourceId(): Int=R.layout.activity_clinical_records
}