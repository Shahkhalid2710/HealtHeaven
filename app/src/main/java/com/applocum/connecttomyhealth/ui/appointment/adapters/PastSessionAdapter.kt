package com.applocum.connecttomyhealth.ui.appointment.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertTime
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_past_session_xml.view.ivDoctor
import kotlinx.android.synthetic.main.raw_past_session_xml.view.tvDoctorFName
import kotlinx.android.synthetic.main.raw_past_session_xml.view.tvDoctorLName
import kotlinx.android.synthetic.main.raw_past_session_xml.view.tvSessionDate
import kotlinx.android.synthetic.main.raw_past_session_xml.view.tvSessionTime
import kotlinx.android.synthetic.main.raw_past_session_xml.view.tvSessionType
import kotlinx.android.synthetic.main.raw_past_session_xml.view.tvSlot
import java.text.SimpleDateFormat

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PastSessionAdapter(context: Context, list:ArrayList<BookAppointmentResponse>):RecyclerView.Adapter<PastSessionAdapter.PastSessionHolder>() {
    var mContext=context
    var mList=list

    inner class PastSessionHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastSessionHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.raw_past_session_xml,parent,false)
        return PastSessionHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: PastSessionHolder, position: Int) {
        val bookAppointmentResponse=mList[position]
        holder.itemView.tvDoctorFName.text = bookAppointmentResponse.gp_details.first_name
        holder.itemView.tvDoctorLName.text = bookAppointmentResponse.gp_details.last_name
        holder.itemView.tvSessionType.text = bookAppointmentResponse.appointment_type
        holder.itemView.tvSlot.text = (bookAppointmentResponse.duration.toString() + " " + "min")
        holder.itemView.tvSessionTime.text = (bookAppointmentResponse.start_time)

        Glide.with(mContext).load(bookAppointmentResponse.gp_details.image)
            .into(holder.itemView.ivDoctor)

        val date = bookAppointmentResponse.address_info.created_at
        var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("EEEE, dd MMMM yyyy")
        val newDateString = spf.format(newDate)
        holder.itemView.tvSessionDate.text =newDateString

        when(bookAppointmentResponse.appointment_type)
        {
            "face_to_face"->{
                holder.itemView.tvSessionType.text=("Face to Face")
            }
            "phone_call"->{
                holder.itemView.tvSessionType.text=("Phone Call")
            }
            "video"->{
                holder.itemView.tvSessionType.text=("Video Call")
            }
        }


    }
}