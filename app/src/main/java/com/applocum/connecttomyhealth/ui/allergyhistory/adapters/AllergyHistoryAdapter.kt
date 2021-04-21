package com.applocum.connecttomyhealth.ui.allergyhistory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.allergyhistory.models.AllergyHistory
import kotlinx.android.synthetic.main.raw_allergy_history.view.*

class AllergyHistoryAdapter(context: Context, list: ArrayList<AllergyHistory>) :
    RecyclerView.Adapter<AllergyHistoryAdapter.AllergyHistoryHolder>() {
    var mContext = context
    var mList = list

    inner class AllergyHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergyHistoryHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_allergy_history, parent, false)
        return AllergyHistoryHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: AllergyHistoryHolder, position: Int) {
        val allergyHistory = mList[position]
        holder.itemView.tvName.text = allergyHistory.aName
        holder.itemView.tvName.setOnClickListener {
            holder.itemView.flNotVerified.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            if (holder.itemView.rlDeletedby.visibility == View.GONE) {
                holder.itemView.rlDeletedby.visibility = View.VISIBLE
                holder.itemView.rlNotVerified.visibility = View.GONE
            } else {
                holder.itemView.rlDeletedby.visibility = View.GONE
                holder.itemView.rlNotVerified.visibility = View.VISIBLE
            }
        }

    }
}