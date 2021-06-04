package com.applocum.connecttomyhealth.ui.familyhistory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.familyhistory.models.FamilyHistory
import kotlinx.android.synthetic.main.raw_family_history.view.*

class FamilyHistoryAdapter(context: Context,list: ArrayList<FamilyHistory>):RecyclerView.Adapter<FamilyHistoryAdapter.FamilyHolder>() {
    var mContext=context
    var mList=list

    inner class FamilyHolder(itemView: View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.raw_family_history,parent,false)
        return FamilyHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: FamilyHolder, position: Int) {
        val familyHistory=mList[position]
        holder.itemView.tvFamilyHistoryName.text= familyHistory.disorder
    }
}