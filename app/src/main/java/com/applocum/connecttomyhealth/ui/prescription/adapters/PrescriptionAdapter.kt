package com.applocum.connecttomyhealth.ui.prescription.adapters

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

class PrescriptionAdapter(context: Context, list:ArrayList<Document>, private val onPrescriptionClick: PrescriptionClickListner):RecyclerView.Adapter<PrescriptionAdapter.PrescriptionHolder>() {
    var mContext=context
    var mList=list

    inner class PrescriptionHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.raw_download,parent,false)
        return PrescriptionHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: PrescriptionHolder, position: Int) {
        val document=mList[position]
        holder.itemView.tvDocType.text = mContext.resources.getString(R.string.prescription)
        holder.itemView.tvDate.text= convertDocumentTime(document.created_at)
        holder.itemView.tvDoctorName.text=("By"+" "+document.by)

        RxView.clicks(holder.itemView).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                onPrescriptionClick.onPrescriptionClick(document, position)
            }
    }

    interface PrescriptionClickListner{
        fun onPrescriptionClick(document: Document, position: Int)
    }
}