package com.applocum.connecttomyhealth.ui.booksession

import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addsymptoms.AddSymptomsActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.ViewPagerFragmentAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_book_session.*
import javax.inject.Inject

class BookSessionActivity : BaseActivity() {
     @Inject
     lateinit var presenter: BookSessionPresenter

    @Inject
    lateinit var userHolder: UserHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

      (application as MyApplication).component.inject(this)

        val viewPagerFragmentAdapter= ViewPagerFragmentAdapter(this,supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(AboutSpecialistFragment(),"About Specialists")
        viewPagerFragmentAdapter.addfragment(AvailabilityFragment(),"Availability")
        viewPager.adapter=viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)

        btnBookSession.setOnClickListener {
            startActivity(Intent(this,AddSymptomsActivity::class.java))
        }

       val firstname=intent.extras?.getString("firstname")
       val lastname=intent.extras?.getString("lastname")
       val image=intent.extras?.getString("image")
       val designation=intent.extras?.getString("designation")
       val bio=intent.extras?.getString("bio")
       val id=intent.extras?.getString("doctorid")
        tvDoctorFirstName.text=firstname
        tvDoctorLastName.text=lastname
        tvDoctorProf.text=designation
        Glide.with(this).load(image).into(ivDoctor)


    }
    override fun getLayoutResourceId(): Int = R.layout.activity_book_session

}