package com.applocum.connecttomyhealth.ui.booksession.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addsymptoms.activities.AddSymptomActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.adapters.ViewPagerFragmentAdapter
import com.applocum.connecttomyhealth.ui.booksession.presenters.BookSessionPresenter
import com.applocum.connecttomyhealth.ui.booksession.fragments.AboutSpecialistFragment
import com.applocum.connecttomyhealth.ui.booksession.fragments.AvailabilityFragment
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_book_session.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BookSessionActivity : BaseActivity() {
    @Inject
    lateinit var presenter: BookSessionPresenter

    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int = R.layout.activity_book_session

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        val specialist = intent.getSerializableExtra("specialist") as Specialist

        val viewPagerFragmentAdapter = ViewPagerFragmentAdapter(this, supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(AboutSpecialistFragment().newInstance(specialist), "About Specialists")
        viewPagerFragmentAdapter.addfragment(AvailabilityFragment().newInstance(specialist), "Availability")
        viewPager.adapter = viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)

        RxView.clicks(btnBookSession).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = Intent(this, AddSymptomActivity::class.java)
                intent.putExtra("specialist", specialist)
                val appointment = userHolder.getBookAppointmentData()
                appointment.therapistId = specialist.id
                appointment.therapistImage = specialist.image
                appointment.threapistBio = specialist.bio
                appointment.therapistName = "${specialist.first_name} ${specialist.last_name}"
                specialist.usual_address.apply {
                    appointment.therapistAddress = "$line1, $line2,$line3, $town, $pincode"
                }
                userHolder.saveBookAppointmentData(appointment)
                startActivity(intent)
            }

        tvDoctorFirstName.text = specialist.first_name
        tvDoctorLastName.text = specialist.last_name
        tvDoctorProf.text = specialist.designation
        Glide.with(this).load(specialist.image).into(ivDoctor)
    }
}