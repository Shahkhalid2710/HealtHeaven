package com.applocum.connecttomyhealth.ui.allergyhistory.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.adapters.ViewPagerFragmentAdapter
import com.applocum.connecttomyhealth.ui.allergyhistory.fragments.ActiveAllergyFragment
import com.applocum.connecttomyhealth.ui.allergyhistory.fragments.PastAllergyFragment
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_allergy_history.*
import kotlinx.android.synthetic.main.activity_allergy_history.ivBack
import java.util.concurrent.TimeUnit

class AllergyHistoryActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_allergy_history

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvAddAllergy).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddAllergyActivity::class.java))
                overridePendingTransition(0,0)
            }

        val viewPagerFragmentAdapter = ViewPagerFragmentAdapter(this, supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(ActiveAllergyFragment(), "Active")
        viewPagerFragmentAdapter.addfragment(PastAllergyFragment(), "Past")
        viewPager.adapter = viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }
}