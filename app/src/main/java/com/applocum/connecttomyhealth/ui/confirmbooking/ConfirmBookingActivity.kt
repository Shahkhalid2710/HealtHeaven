package com.applocum.connecttomyhealth.ui.confirmbooking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertAvailableTimeSlots
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.payment.PaymentActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_confirm_booking.*
import java.text.SimpleDateFormat
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ConfirmBookingActivity : BaseActivity() {

    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int =R.layout.activity_confirm_booking

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        (application as MyApplication).component.inject(this)

        btnConfirm.setOnClickListener {
            startActivity(Intent(this,PaymentActivity::class.java))
        }

        val bookAppointment=userHolder.getBookAppointmentData()
        tvTime.text= convertAvailableTimeSlots(bookAppointment.appointmentTime)
        tvSessionType.text=bookAppointment.appointmentType
        tvDuration.text=(bookAppointment.appointmentSlot +" "+"min")
        tvDoctorName.text=bookAppointment.therapistName
        tvDoctorBio.text=bookAppointment.threapistBio
        Glide.with(this).load(bookAppointment.therapistImage).into(ivDoctor)

        var spf = SimpleDateFormat("yy-MM-dd")
        val newDate = spf.parse(bookAppointment.appointmentDate)
        spf = SimpleDateFormat("dd-MM-yyyy")
        val newDateString = spf.format(newDate)
        tvAppointmentDate.text = newDateString

    }
}