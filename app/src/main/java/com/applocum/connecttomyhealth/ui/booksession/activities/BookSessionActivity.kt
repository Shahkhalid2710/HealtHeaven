package com.applocum.connecttomyhealth.ui.booksession.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addsymptoms.activities.AddSymptomActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.adapters.ViewPagerFragmentAdapter
import com.applocum.connecttomyhealth.ui.booksession.fragments.AboutSpecialistFragment
import com.applocum.connecttomyhealth.ui.booksession.fragments.AvailabilityFragment
import com.applocum.connecttomyhealth.ui.booksession.presenters.BookSessionPresenter
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
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

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvCancel).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = (Intent(this, BottomNavigationViewActivity::class.java))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finishAffinity()
                overridePendingTransition(0,0)
            }

        val specialist = intent.getSerializableExtra("specialist") as Specialist
        val specialistId=intent.getIntExtra("specialistId",0)

        val viewPagerFragmentAdapter = ViewPagerFragmentAdapter(this, supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(AboutSpecialistFragment().newInstance(specialist), "About Specialist")
        viewPagerFragmentAdapter.addfragment(AvailabilityFragment().newInstance(specialist), "Availability")
        viewPager.adapter = viewPagerFragmentAdapter
        tablayout.setupWithViewPager(viewPager)

        viewPager.setPagingEnabled(false)

        RxView.clicks(btnBookSession).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = Intent(this, AddSymptomActivity::class.java)
                //intent.putExtra("specialist", specialist)
                intent.putExtra("specialistId", specialistId)
                val appointment = userHolder.getBookAppointmentData()
                appointment.therapistId = specialist.id
                appointment.therapistId = specialistId
                appointment.therapistImage = specialist.image
                appointment.threapistBio = specialist.bio
                appointment.therapistName = "${specialist.first_name} ${specialist.last_name}"
                specialist.usual_address?.apply {
                    appointment.therapistAddress = "$line1, $line2,$line3, $town, $pincode"
                }
                userHolder.saveBookAppointmentData(appointment)
                startActivity(intent)
                overridePendingTransition(0,0)
            }

        tvDoctorFirstName.text = specialist.first_name
        tvDoctorLastName.text = specialist.last_name
        tvDoctorProf.text = specialist.designation
        Glide.with(this).load(specialist.image).into(ivDoctor)
    }

}