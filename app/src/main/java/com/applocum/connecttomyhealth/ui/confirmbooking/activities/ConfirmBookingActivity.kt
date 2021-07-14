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
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.payment.activities.PaymentShowActivity
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_confirm_booking.*
import kotlinx.android.synthetic.main.activity_confirm_booking.ivBack
import kotlinx.android.synthetic.main.activity_confirm_booking.ivDoctor
import kotlinx.android.synthetic.main.activity_confirm_booking.tvCancel
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ConfirmBookingActivity : BaseActivity() {
    @Inject
    lateinit var userHolder: UserHolder

    lateinit var common: Common

    override fun getLayoutResourceId(): Int = R.layout.activity_confirm_booking

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("SimpleDateFormat", "CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)

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

        val bookAppointment = userHolder.getBookAppointmentData()

        if (bookAppointment.recurringSessionCount == "0")
        {
            tvSessions.text="1"
        }else
        {
            tvSessions.text=bookAppointment.recurringSessionCount
        }

        common = intent.getSerializableExtra("commonData") as Common
        val appointmentBasicPrice = common.appointment_basic_price

        tvTotalSession.text=tvSessions.text
        val totalSession:Int=tvTotalSession.text.toString().toInt()
        val sessionCost = totalSession * appointmentBasicPrice

        tvSessionCost.text=("€$appointmentBasicPrice.00")
        tvTotalCost.text = ("€$sessionCost.00")
        tvTime.text = convertAvailableTimeSlots(bookAppointment.appointmentTime)
        tvDuration.text = (bookAppointment.appointmentSlot + " " + "mins")
        tvDoctorName.text = bookAppointment.therapistName
        tvDoctorBio.text = bookAppointment.threapistBio
        Glide.with(this).load(bookAppointment.therapistImage).into(ivDoctor)

        if (bookAppointment.confirmDate.equals(""))
        {
            var spf = SimpleDateFormat("yy-MM-dd")
            val newDate = spf.parse(bookAppointment.appointmentDate)
            spf = SimpleDateFormat("d'th' MMMM yyyy")
            val newDateString = spf.format(newDate)
            tvAppointmentDate.text = newDateString
        }else
        {
            tvAppointmentDate.text=bookAppointment.confirmDate
        }

        when(bookAppointment.confirmDate)
        {
            "Daily from"->
            {
                var spf = SimpleDateFormat("yy-MM-dd")
                val newDate = spf.parse(bookAppointment.appointmentDate)
                spf = SimpleDateFormat("d'th' MMMM yyyy")
                val newDateString = spf.format(newDate)
                tvAppointmentDate.text=(bookAppointment.confirmDate +" "+ newDateString)
            }
        }

        when (bookAppointment.appointmentType) {
            "offline_appointment" -> {
                tvSessionType.text = ("Face to Face")
            }
            "phone_call_appointment" -> {
                tvSessionType.text = ("Phone Call")
            }
            "online_appointment" -> {
                tvSessionType.text = ("Video Call")
            }
        }

        RxView.clicks(btnConfirm).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val intent=Intent(this, PaymentShowActivity::class.java)
                intent.putExtra("date",tvAppointmentDate.text)
                intent.putExtra("cost",tvTotalCost.text.toString())
                startActivity(intent)
                overridePendingTransition(0,0)
            }
    }
}