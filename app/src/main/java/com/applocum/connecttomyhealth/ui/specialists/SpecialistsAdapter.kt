package com.applocum.connecttomyhealth.ui.specialists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.specialists.models.Specialists
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_doctor_xml.view.*

class SpecialistsAdapter(context: Context,list:ArrayList<Specialists>):RecyclerView.Adapter<SpecialistsAdapter.SpecialistHolder>() {
    var mContext=context
    var mList=list

    inner class SpecialistHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialistHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.raw_doctor_xml,parent,false)
        return SpecialistHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: SpecialistHolder, position: Int) {
        val specialists=mList[position]
        holder.itemView.tvDoctorName.text=specialists.sName
        holder.itemView.tvProf.text=specialists.sProf
        holder.itemView.tvDes.text=specialists.sDes

        Glide.with(mContext).load(specialists.sImage).into(holder.itemView.ivDoctor)
    }
}