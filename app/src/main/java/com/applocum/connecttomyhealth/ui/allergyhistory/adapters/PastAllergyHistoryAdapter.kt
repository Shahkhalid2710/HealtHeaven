package com.applocum.connecttomyhealth.ui.allergyhistory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.allergyhistory.models.FalseAllergy
import kotlinx.android.synthetic.main.raw_allergy_history.view.tvName

class PastAllergyHistoryAdapter(context: Context, list: ArrayList<FalseAllergy>) :
    RecyclerView.Adapter<PastAllergyHistoryAdapter.AllergyHistoryHolder>() {
    var mContext = context
    var mList = list

    inner class AllergyHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergyHistoryHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_past_allergy, parent, false)
        return AllergyHistoryHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: AllergyHistoryHolder, position: Int) {
        val falseAllergy = mList[position]
        holder.itemView.tvName.text = falseAllergy.allergy.name
    }
}