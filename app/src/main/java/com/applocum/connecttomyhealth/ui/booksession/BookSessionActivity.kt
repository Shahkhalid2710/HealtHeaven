package com.applocum.connecttomyhealth.ui.booksession

import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.ViewPagerFragmentAdapter
import kotlinx.android.synthetic.main.activity_book_session.*

class BookSessionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        val viewPagerFragmentAdapter= ViewPagerFragmentAdapter(this,supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(AboutSpecialistFragment(),"About Specialists")
        viewPagerFragmentAdapter.addfragment(AvailabilityFragment(),"Availability")
        viewPager.adapter=viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)

        btnBookSession.setOnClickListener {
            startActivity(Intent(this,SessionBookActivity::class.java))
        }

    }
    override fun getLayoutResourceId(): Int =R.layout.activity_book_session
}