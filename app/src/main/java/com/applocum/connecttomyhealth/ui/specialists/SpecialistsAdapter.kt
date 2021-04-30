package com.applocum.connecttomyhealth.ui.specialists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.booksession.BookSessionActivity
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_doctor_xml.view.*

class SpecialistsAdapter(context: Context,list: ArrayList<Specialist>,private val onitemclick:ItemClickListner) : RecyclerView.Adapter<SpecialistsAdapter.SpecialistHolder>() {
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

    override fun onBindViewHolder(holder: SpecialistHolder, position: Int) {
        val specialist = mList[position]
        holder.itemView.tvDoctorFirstName.text =specialist.first_name
        holder.itemView.tvDoctorLastName.text = specialist.last_name
        holder.itemView.tvProf.text = specialist.designation
        holder.itemView.tvDes.text = specialist.bio

        holder.itemView.btnViewProfile.setOnClickListener {
            onitemclick.onItemClick(specialist,position)
        }
        holder.itemView.btnBookSession.setOnClickListener {
           onitemclick.onItemClick(specialist, position)
        }

        Glide.with(mContext).load(specialist.image).into(holder.itemView.ivDoctor)
    }
    interface ItemClickListner
    {
        fun onItemClick(specialist: Specialist,position: Int)
    }
}