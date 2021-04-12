package com.applocum.connecttomyhealth.ui.allergyhistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_allergy_history.*

class AllergyHistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        tvAddAllergy.setOnClickListener {
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