package com.applocum.connecttomyhealth.ui.exemptions.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.exemptions.models.Exemption
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.raw_exemption_xml.view.*
import java.util.concurrent.TimeUnit

class ExemptionAdapter(context: Context, list: ArrayList<Exemption>) : RecyclerView.Adapter<ExemptionAdapter.ExemptionHolder>() {
    var mContext = context
    var mList = list
    private var selectedItem = -1

    inner class ExemptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExemptionHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_exemption_xml, parent, false)
        return ExemptionHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ExemptionHolder, position: Int) {
        val exemption = mList[position]
        holder.itemView.tvLetter.text = exemption.eLetter
        holder.itemView.tvDes.text = exemption.eDes

        RxView.clicks(holder.itemView).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectedItem = position
                notifyDataSetChanged()
            }

        if (selectedItem == holder.adapterPosition) {
            holder.itemView.llexemption.setBackgroundResource(R.drawable.custom_btn)
            holder.itemView.tvLetter.setTextColor(Color.parseColor("#FFFFFF"))
            holder.itemView.tvDes.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.llexemption.setBackgroundResource(R.drawable.custom_edittext)
            holder.itemView.tvLetter.setTextColor(Color.parseColor("#3a3a3a"))
            holder.itemView.tvDes.setTextColor(Color.parseColor("#3a3a3a"))
        }
    }
}