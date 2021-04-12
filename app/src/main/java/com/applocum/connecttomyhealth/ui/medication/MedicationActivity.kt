package com.applocum.connecttomyhealth.ui.medication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.ActiveAllergyFragment
import com.applocum.connecttomyhealth.ui.allergyhistory.PastAllergyFragment
import com.applocum.connecttomyhealth.ui.allergyhistory.ViewPagerFragmentAdapter
import kotlinx.android.synthetic.main.activity_allergy_history.*
import kotlinx.android.synthetic.main.activity_medication.*
import kotlinx.android.synthetic.main.activity_medication.ivBack
import kotlinx.android.synthetic.main.activity_medication.tablayout
import kotlinx.android.synthetic.main.activity_medication.viewPager

class MedicationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        val viewPagerFragmentAdapter= ViewPagerFragmentAdapter(this,supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(ActiveAcuteMedicationIssueFragment(),"Active Acute")
        viewPagerFragmentAdapter.addfragment(PastAcuteMedicationIssueFragment(),"Past Acute")
        viewPager.adapter=viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_medication
}