package com.applocum.connecttomyhealth.ui.referral.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertDocumentTime
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import kotlinx.android.synthetic.main.raw_download.view.*

class ReferralAdapter(context: Context, list:ArrayList<Document>,private val onReferralClick:ReferralClickListner): RecyclerView.Adapter<ReferralAdapter.ReferralHolder>() {
    var mContext=context
    var mList=list

    inner class ReferralHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferralHolder {
        val v= LayoutInflater.from(mContext).inflate(R.layout.raw_download,parent,false)
        return ReferralHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ReferralHolder, position: Int) {
        val document=mList[position]
        holder.itemView.tvDocType.text = mContext.resources.getString(R.string.referral)
        holder.itemView.tvDate.text= convertDocumentTime(document.created_at)
        holder.itemView.tvDoctorName.text=("By"+" "+document.by)

        holder.itemView.setOnClickListener {
            onReferralClick.onReferralClick(document, position)
        }

    }
    interface ReferralClickListner{
        fun onReferralClick(document: Document,position: Int)
    }
}