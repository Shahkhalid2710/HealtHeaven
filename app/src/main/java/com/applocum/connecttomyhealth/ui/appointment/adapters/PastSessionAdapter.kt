package com.applocum.connecttomyhealth.ui.appointment.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.appointment.models.Session
import com.applocum.connecttomyhealth.ui.booksession.BookSessionActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_past_session_xml.view.*

class PastSessionAdapter(context: Context,list:ArrayList<Session>):RecyclerView.Adapter<PastSessionAdapter.PastSessionHolder>() {
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

    override fun onBindViewHolder(holder: PastSessionHolder, position: Int) {
        val session=mList[position]

        holder.itemView.tvDoctorName.text=session.sName
        holder.itemView.tvSessionName.text=session.sSessionType
        holder.itemView.tvSessioonDate.text=session.sDate

        holder.itemView.btnViewDetails.setOnClickListener {
            val intent= Intent(mContext,BookSessionActivity::class.java)
            mContext.startActivity(intent)
        }

        Glide.with(mContext).load(session.sImage).into(holder.itemView.ivDoctor)

    }
}