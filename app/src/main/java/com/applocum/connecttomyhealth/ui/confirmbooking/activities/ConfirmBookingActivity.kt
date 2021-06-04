package com.applocum.connecttomyhealth.ui.confirmbooking.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertAvailableTimeSlots
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.booksession.models.Common
import com.applocum.connecttomyhealth.ui.payment.activities.PaymentShowActivity
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_confirm_booking.*
import kotlinx.android.synthetic.main.activity_confirm_booking.ivBack
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ConfirmBookingActivity : BaseActivity() {
    @Inject
    lateinit var userHolder: UserHolder

    lateinit var common: Common

    override fun getLayoutResourceId(): Int = R.layout.activity_confirm_booking

    @SuppressLint("SimpleDateFormat", "CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(btnConfirm).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, PaymentShowActivity::class.java))
            }

        common = intent.getSerializableExtra("commonData") as Common
        val appointmentBasicPrice = common.appointment_basic_price
        tvBookingCost.text = ("€$appointmentBasicPrice.00")
        tvTotalCost.text = ("€$appointmentBasicPrice.00")
        val bookAppointment = userHolder.getBookAppointmentData()
        tvTime.text = convertAvailableTimeSlots(bookAppointment.appointmentTime)
        tvDuration.text = (bookAppointment.appointmentSlot + " " + "min")
        tvDoctorName.text = bookAppointment.therapistName
        tvDoctorBio.text = bookAppointment.threapistBio
        Glide.with(this).load(bookAppointment.therapistImage).into(ivDoctor)

        var spf = SimpleDateFormat("yy-MM-dd")
        val newDate = spf.parse(bookAppointment.appointmentDate)
        spf = SimpleDateFormat("dd-MM-yyyy")
        val newDateString = spf.format(newDate)
        tvAppointmentDate.text = newDateString

        when (bookAppointment.appointmentType) {
            "face_to_face" -> {
                tvSessionType.text = ("Face to Face")
            }
            "phone_call" -> {
                tvSessionType.text = ("Phone Call")
            }
            "video" -> {
                tvSessionType.text = ("Video Call")
            }
        }
    }
}