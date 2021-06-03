package com.applocum.connecttomyhealth.ui.allergyhistory

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_add_medical_history.*
import kotlinx.android.synthetic.main.activity_allergy_history.*
import kotlinx.android.synthetic.main.activity_allergy_history.ivBack
import java.util.concurrent.TimeUnit

class AllergyHistoryActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        RxView.clicks(tvAddAllergy).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,AddAllergyActivity::class.java))
            }

        val viewPagerFragmentAdapter=ViewPagerFragmentAdapter(this,supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(ActiveAllergyFragment(),"Active")
        viewPagerFragmentAdapter.addfragment(PastAllergyFragment(),"Past")
        viewPager.adapter=viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)

    }

    override fun getLayoutResourceId(): Int=R.layout.activity_allergy_history
}