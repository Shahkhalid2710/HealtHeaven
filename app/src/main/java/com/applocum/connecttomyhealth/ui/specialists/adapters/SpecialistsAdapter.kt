package com.applocum.connecttomyhealth.ui.specialists.adapters

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
import kotlinx.android.synthetic.main.raw_doctor_xml.view.*
import java.util.concurrent.TimeUnit

class SpecialistsAdapter(context: Context,list: ArrayList<Specialist>,private val onitemclick: ItemClickListner) : RecyclerView.Adapter<SpecialistsAdapter.SpecialistHolder>() {
    private var mContext = context
    private var mList = list

    inner class SpecialistHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialistHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_doctor_xml, parent, false)
        return SpecialistHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: SpecialistHolder, position: Int) {
        val specialist = mList[position]
        holder.itemView.tvDoctorFirstName.text =specialist.first_name
        holder.itemView.tvDoctorLastName.text = specialist.last_name
        holder.itemView.tvProf.text = specialist.designation
        holder.itemView.tvDes.text = specialist.bio

        RxView.clicks(holder.itemView.btnViewProfile).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                onitemclick.onItemClick(specialist,position)
            }

        RxView.clicks(holder.itemView.btnBookSession).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                onitemclick.onbookSession(specialist,position)
            }

        Glide.with(mContext).load(specialist.image).into(holder.itemView.ivDoctor)
    }
    interface ItemClickListner
    {
        fun onItemClick(specialist: Specialist,position: Int)
        fun onbookSession(specialist: Specialist,position: Int)
    }
}