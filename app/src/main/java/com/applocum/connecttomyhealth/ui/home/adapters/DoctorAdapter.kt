package com.applocum.connecttomyhealth.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.home.model.Doctor
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.doctor_raw_xml.view.*

class DoctorAdapter(context: Context,list:ArrayList<Doctor>):RecyclerView.Adapter<DoctorAdapter.DoctorHolder>(){
    var mContext=context
    var mList=list

    inner class DoctorHolder(itemview:View):RecyclerView.ViewHolder(itemview){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.doctor_raw_xml,parent,false)
        return DoctorHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: DoctorHolder, position: Int) {
        val doctor=mList[position]
        holder.itemView.tvDoctorName.text=doctor.dName
        holder.itemView.tvProfession.text=doctor.dProfession
        holder.itemView.tvTime.text=doctor.dTime
        Glide.with(mContext).load(doctor.dImage).into(holder.itemView.ivDoctor)
    }
}