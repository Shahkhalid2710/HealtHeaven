package com.applocum.connecttomyhealth.ui.appointment.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertDateTime
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.raw_session_xml.view.*
import kotlinx.android.synthetic.main.raw_session_xml.view.btnCancel
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UpcomingSessionAdapter(context: Context, list: ArrayList<BookAppointmentResponse>, private val itemCLick: ItemClickListner) :
    RecyclerView.Adapter<UpcomingSessionAdapter.SessionHolder>() {
    var mContext = context
    var mList = list

    inner class SessionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_session_xml, parent, false)
        return SessionHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("SimpleDateFormat", "CheckResult")
    override fun onBindViewHolder(holder: SessionHolder, position: Int) {
        val bookAppointmentResponse = mList[position]
        holder.itemView.tvDoctorFName.text = bookAppointmentResponse.gp_details.first_name
        holder.itemView.tvDoctorLName.text = bookAppointmentResponse.gp_details.last_name
        holder.itemView.tvSessionType.text = bookAppointmentResponse.appointment_type
        holder.itemView.tvSlot.text = (bookAppointmentResponse.duration.toString() + " " + "mins")
        holder.itemView.tvSessionDateTime.text =
            bookAppointmentResponse.start_time?.let { convertDateTime(it) }

        Glide.with(mContext).load(bookAppointmentResponse.gp_details.image)
            .into(holder.itemView.ivDoctor)

        when (bookAppointmentResponse.appointment_type) {
            "face_to_face" -> {
                holder.itemView.tvSessionType.text = ("Face to Face")
                holder.itemView.btnCheckin.text = ("Check-in")
            }
            "phone_call" -> {
                holder.itemView.tvSessionType.text = ("Phone Call")
                holder.itemView.btnCheckin.text = ("Join")
            }
            "video" -> {
                holder.itemView.tvSessionType.text = ("Video Call")
                holder.itemView.btnCheckin.text = ("Join")
            }
        }

        if (holder.itemView.btnCheckin.text == "Join") {
            holder.itemView.btnCheckin.isEnabled = false
        }

        RxView.clicks(holder.itemView.btnCheckin).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                itemCLick.onButtonClick(bookAppointmentResponse, position)
            }

        RxView.clicks(holder.itemView.btnCancel).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                itemCLick.itemClick(bookAppointmentResponse, position)
            }
    }

    interface ItemClickListner {
        fun itemClick(bookAppointmentResponse: BookAppointmentResponse, position: Int)
        fun onButtonClick(bookAppointmentResponse: BookAppointmentResponse, position: Int)
    }
}