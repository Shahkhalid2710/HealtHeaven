package com.applocum.connecttomyhealth.ui.referral.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertDocumentTime
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.raw_download.view.*
import java.util.concurrent.TimeUnit

class ReferralAdapter(context: Context, list:ArrayList<Document>, private val onReferralClick:ReferralClickListner): RecyclerView.Adapter<ReferralAdapter.ReferralHolder>() {
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

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ReferralHolder, position: Int) {
        val document=mList[position]
        holder.itemView.tvDocType.text = mContext.resources.getString(R.string.referral)
        holder.itemView.tvDate.text= convertDocumentTime(document.created_at)
        holder.itemView.tvDoctorName.text=("By"+" "+document.by)

        RxView.clicks(holder.itemView).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                onReferralClick.onReferralClick(document, position)
            }
    }
    interface ReferralClickListner{
        fun onReferralClick(document: Document, position: Int)
    }
}