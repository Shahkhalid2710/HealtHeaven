package com.applocum.connecttomyhealth.ui.sessiondetails

import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertDate
import com.applocum.connecttomyhealth.convertTime
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_session_details.*
import kotlinx.android.synthetic.main.activity_session_details.ivBack
import kotlinx.android.synthetic.main.activity_session_details.ivDoctor
import kotlinx.android.synthetic.main.activity_session_details.tvTime

class SessionDetailsActivity : BaseActivity() {
    lateinit var bookAppointmentResponse:BookAppointmentResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        bookAppointmentResponse= intent.getSerializableExtra("bookAppointmentResponse") as BookAppointmentResponse

        val startTime= convertTime(bookAppointmentResponse.actual_start_time)
        val endTime= convertTime(bookAppointmentResponse.actual_end_time)

        tvDoctorFName.text=bookAppointmentResponse.gp_details.first_name
        tvDoctorLName.text=bookAppointmentResponse.gp_details.last_name
        tvDate.text= convertDate(bookAppointmentResponse.actual_start_time)
        tvAmount.text=("€"+bookAppointmentResponse.appointment_price.toString()+"0")
        tvTime.text=("$startTime - $endTime")
        tvDoctorGmcno.text=("GMC NO."+" "+bookAppointmentResponse.gp_details.gmc)
        Glide.with(this).load(bookAppointmentResponse.gp_details.image).into(ivDoctor)

        when(bookAppointmentResponse.appointment_type)
        {
            "face_to_face"->{
                tvSessionType.text=("Face to Face")
            }
            "phone_call"->{
                tvSessionType.text=("Phone Call")
            }
            "video"->{
                tvSessionType.text=("Video Call")
            }

        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_session_details
}