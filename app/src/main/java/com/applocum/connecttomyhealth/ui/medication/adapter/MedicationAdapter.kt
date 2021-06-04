package com.applocum.connecttomyhealth.ui.medication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.medication.models.Medication
import kotlinx.android.synthetic.main.raw_medicaton_xml.view.*

class MedicationAdapter(context: Context, list: ArrayList<Medication>) : RecyclerView.Adapter<MedicationAdapter.MedicationHolder>() {
    var mContext = context
    var mList = list

    inner class MedicationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_medicaton_xml, parent, false)
        return MedicationHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MedicationHolder, position: Int) {
        val medication = mList[position]
        holder.itemView.tvDes.text = medication.mDes
        holder.itemView.tvDate.text = medication.mDate
        holder.itemView.tvQuantity.text = medication.mQuantity
    }
}