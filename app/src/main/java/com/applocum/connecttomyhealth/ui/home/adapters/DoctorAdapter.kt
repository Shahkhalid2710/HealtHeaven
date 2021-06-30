package com.applocum.connecttomyhealth.ui.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.doctor_raw_xml.view.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class DoctorAdapter(context: Context,list:ArrayList<Specialist?>,private val doctorClick:DoctorClickListner):RecyclerView.Adapter<DoctorAdapter.DoctorHolder>(){
    var mContext=context
    var mList=list
    lateinit var view:View

    inner class DoctorHolder(itemview:View):RecyclerView.ViewHolder(itemview){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.doctor_raw_xml,parent,false)
        return DoctorHolder(v)
    }

    override fun getItemCount(): Int {
        return 6
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: DoctorHolder, position: Int) {
        val specialist = mList[position]
        holder.itemView.tvDoctorFName.text=specialist?.first_name
        holder.itemView.tvDoctorLName.text=specialist?.last_name
        holder.itemView.tvDoctorDesignation.text=specialist?.designation
        holder.itemView.tvDoctorEmail.text=specialist?.email
        Glide.with(mContext).load(specialist?.image).into(holder.itemView.ivDoctor)

        RxView.clicks(holder.itemView.cvDoctor).throttleFirst(2000,TimeUnit.MILLISECONDS)
            .subscribe {
                specialist?.let { it1 -> doctorClick.onDoctorClick(it1, position) }
            }

    }

    interface DoctorClickListner{
        fun onDoctorClick(specialist: Specialist,position: Int)
    }
}