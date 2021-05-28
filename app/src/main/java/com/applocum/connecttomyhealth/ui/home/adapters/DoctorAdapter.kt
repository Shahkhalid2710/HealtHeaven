package com.applocum.connecttomyhealth.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.doctor_raw_xml.view.*

class DoctorAdapter(context: Context,list:ArrayList<Specialist>,private val doctorClick:DoctorClickListner):RecyclerView.Adapter<DoctorAdapter.DoctorHolder>(){
    var mContext=context
    var mList=list

    inner class DoctorHolder(itemview:View):RecyclerView.ViewHolder(itemview){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.doctor_raw_xml,parent,false)
        return DoctorHolder(v)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: DoctorHolder, position: Int) {
        val specialist = mList[position]
        holder.itemView.tvDoctorFName.text=specialist.first_name
        holder.itemView.tvDoctorLName.text=specialist.last_name
        holder.itemView.tvDoctorDesignation.text=specialist.designation
        holder.itemView.tvDoctorEmail.text=specialist.email
        Glide.with(mContext).load(specialist.image).into(holder.itemView.ivDoctor)

        holder.itemView.cvDoctor.setOnClickListener {
            doctorClick.onDoctorClick(specialist, position)
        }

    }
    interface DoctorClickListner{
        fun onDoctorClick(specialist: Specialist,position: Int)
    }
}