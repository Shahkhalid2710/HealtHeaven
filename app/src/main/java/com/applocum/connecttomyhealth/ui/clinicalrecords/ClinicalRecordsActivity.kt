package com.applocum.connecttomyhealth.ui.clinicalrecords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.AllergyHistoryActivity
import com.applocum.connecttomyhealth.ui.familyhistory.FamilyHistoryActivity
import com.applocum.connecttomyhealth.ui.investigation.InvestigationActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.MedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.medication.MedicationActivity
import kotlinx.android.synthetic.main.activity_clinical_records.*

class ClinicalRecordsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        rlFamilyHistory.setOnClickListener {
            startActivity(Intent(this,FamilyHistoryActivity::class.java))
        }
        rlAllergyHistory.setOnClickListener {
            startActivity(Intent(this,AllergyHistoryActivity::class.java))
        }
        rlInvestigation.setOnClickListener {
            startActivity(Intent(this,InvestigationActivity::class.java))
        }
        rlMedicalBased.setOnClickListener {
            startActivity(Intent(this,MedicationActivity::class.java))
        }
        rlMedicalHistory.setOnClickListener {
            startActivity(Intent(this,MedicalHistoryActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int=R.layout.activity_clinical_records
}