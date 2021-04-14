package com.applocum.connecttomyhealth.ui.booksession

import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.ViewPagerFragmentAdapter
import kotlinx.android.synthetic.main.activity_book_session.ivBack
import kotlinx.android.synthetic.main.activity_book_session.tablayout
import kotlinx.android.synthetic.main.activity_book_session.viewPager

class BookSessionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        val viewPagerFragmentAdapter= ViewPagerFragmentAdapter(this,supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(AboutSpecialistFragment(),"About Specialists")
        viewPagerFragmentAdapter.addfragment(AvailabilityFragment(),"Availability")
        viewPager.adapter=viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }
    override fun getLayoutResourceId(): Int =R.layout.activity_book_session
}