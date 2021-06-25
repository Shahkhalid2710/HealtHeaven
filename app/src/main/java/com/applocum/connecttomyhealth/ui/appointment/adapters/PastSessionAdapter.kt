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
import kotlinx.android.synthetic.main.raw_past_session_xml.view.*
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PastSessionAdapter(context: Context, list: ArrayList<BookAppointmentResponse>,private val onItemClick: ItemClickListner) : RecyclerView.Adapter<PastSessionAdapter.PastSessionHolder>() {
    var mContext = context
    var mList = list

    inner class PastSessionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastSessionHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_past_session_xml, parent, false)
        return PastSessionHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("SimpleDateFormat", "CheckResult")
    override fun onBindViewHolder(holder: PastSessionHolder, position: Int) {
        val bookAppointmentResponse = mList[position]
        holder.itemView.tvDoctorFName.text = bookAppointmentResponse.gp_details.first_name
        holder.itemView.tvDoctorLName.text = bookAppointmentResponse.gp_details.last_name
        holder.itemView.tvSessionType.text = bookAppointmentResponse.appointment_type
        holder.itemView.tvSlot.text = (bookAppointmentResponse.duration.toString() + " " + "mins")
        holder.itemView.tvSessionDateTime.text = convertDateTime(bookAppointmentResponse.actual_start_time)

        Glide.with(mContext).load(bookAppointmentResponse.gp_details.image)
            .into(holder.itemView.ivDoctor)

        when (bookAppointmentResponse.appointment_type) {
            "face_to_face" -> {
                holder.itemView.tvSessionType.text = ("Face to Face")
            }
            "phone_call" -> {
                holder.itemView.tvSessionType.text = ("Phone Call")
            }
            "video" -> {
                holder.itemView.tvSessionType.text = ("Video Call")
            }
        }

        when (bookAppointmentResponse.status) {
            "cancelled" -> {
                holder.itemView.tvCancel.visibility=View.VISIBLE
                holder.itemView.tvMissed.visibility=View.GONE
            }
            "missed" -> {
                holder.itemView.tvCancel.visibility=View.GONE
                holder.itemView.tvMissed.visibility=View.VISIBLE            }
        }

        RxView.clicks(holder.itemView.btnViewDetails).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                onItemClick.itemClick(bookAppointmentResponse, position)
            }

        RxView.clicks(holder.itemView.btnBookAgain).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                onItemClick.onButtonClick(bookAppointmentResponse, position)
            }
    }

    interface ItemClickListner {
        fun itemClick(bookAppointmentResponse: BookAppointmentResponse, position: Int)
        fun onButtonClick(bookAppointmentResponse: BookAppointmentResponse, position: Int)
    }
}