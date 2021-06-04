package com.applocum.connecttomyhealth.ui.medicalhistory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.medicalhistory.models.Medical
import kotlinx.android.synthetic.main.raw_medical_list.view.*

class MedicalDiseaseAdapter(context: Context, list: ArrayList<Medical>, private val onItemClick: ItemClickListnter
) : RecyclerView.Adapter<MedicalDiseaseAdapter.MedicalHolder>() {
    var mContext = context
    var mList = list

    inner class MedicalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_medical_list, parent, false)
        return MedicalHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MedicalHolder, position: Int) {
        val medical = mList[position]
        holder.itemView.tvDiseaseName.text = medical.description

        holder.itemView.setOnClickListener {
            onItemClick.onItemClick(medical, position)
            notifyDataSetChanged()
            mList.clear()
        }
    }

    interface ItemClickListnter {
        fun onItemClick(medical: Medical, position: Int)
    }
}