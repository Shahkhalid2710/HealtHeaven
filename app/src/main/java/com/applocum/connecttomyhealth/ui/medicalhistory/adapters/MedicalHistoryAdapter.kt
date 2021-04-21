package com.applocum.connecttomyhealth.ui.medicalhistory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.medicalhistory.models.MedicalHistory
import kotlinx.android.synthetic.main.raw_medical_history.view.*

class MedicalHistoryAdapter(context: Context, list: ArrayList<MedicalHistory>) :
    RecyclerView.Adapter<MedicalHistoryAdapter.MedicalHistoryHolder>() {

    var mContext = context
    var mList = list

    inner class MedicalHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalHistoryHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_medical_history, parent, false)
        return MedicalHistoryHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MedicalHistoryHolder, position: Int) {
        val medicalHistory = mList[position]
        holder.itemView.tvName.text = medicalHistory.mName
        holder.itemView.tvDate.text = medicalHistory.mDate

        holder.itemView.setOnClickListener {
            if (holder.itemView.rlVerified.visibility == View.GONE) {
                holder.itemView.rlVerified.visibility = View.VISIBLE
                holder.itemView.rlNotVerified.visibility = View.GONE
            } else {
                holder.itemView.rlVerified.visibility = View.GONE
                holder.itemView.rlNotVerified.visibility = View.VISIBLE
            }
        }

    }
}