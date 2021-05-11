package com.applocum.connecttomyhealth.ui.investigation

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.investigation.models.Investigation
import kotlinx.android.synthetic.main.raw_investigation_xml.view.*


class InvestigationAdapter(context: Context,list: ArrayList<Investigation>):RecyclerView.Adapter<InvestigationAdapter.InvestigationHolder>() {

    var mContext=context
    var mList=list



    inner class InvestigationHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvestigationHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.raw_investigation_xml,parent,false)
        return InvestigationHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: InvestigationHolder, position: Int) {
        val investigation=mList[position]

        holder.itemView.tvInvestigationName.text=investigation.iName
        holder.itemView.tvInvestigationDate.text=investigation.iDate
        holder.itemView.tvInvestigationDescription.text=investigation.iDes

        holder.itemView.llInvestigation.setOnClickListener {
            if (holder.itemView.llDes.visibility == View.GONE) {
                holder.itemView.llDes.visibility = View.VISIBLE
                holder.itemView.ivList.animate().rotation(180f).start()

            } else {
                holder.itemView.llDes.visibility = View.GONE
                holder.itemView.ivList.animate().rotation(360f).start()
            }

            val animation =
                ObjectAnimator.ofInt(holder.itemView.llDes, "maxLines", holder.itemView.tvInvestigationDescription.maxLines)
            animation.setDuration(200).start()
        }
    }
}