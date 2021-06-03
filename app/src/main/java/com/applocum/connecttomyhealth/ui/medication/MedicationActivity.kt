package com.applocum.connecttomyhealth.ui.medication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.ViewPagerFragmentAdapter
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_medication.ivBack
import kotlinx.android.synthetic.main.activity_medication.tablayout
import kotlinx.android.synthetic.main.activity_medication.viewPager
import java.util.concurrent.TimeUnit

class MedicationActivity : BaseActivity() {
    override fun getLayoutResourceId(): Int =R.layout.activity_medication
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        val viewPagerFragmentAdapter= ViewPagerFragmentAdapter(this,supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(ActiveAcuteMedicationIssueFragment(),"Active Acute")
        viewPagerFragmentAdapter.addfragment(PastAcuteMedicationIssueFragment(),"Past Acute")
        viewPager.adapter=viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }

}