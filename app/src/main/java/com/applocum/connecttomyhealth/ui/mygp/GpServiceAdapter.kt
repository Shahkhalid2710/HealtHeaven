package com.applocum.connecttomyhealth.ui.mygp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import kotlinx.android.synthetic.main.custom_gp_service_dialog.view.*
import kotlinx.android.synthetic.main.raw_gp_service.view.*
import kotlinx.android.synthetic.main.raw_gp_service.view.tvArea


class GpServiceAdapter(context: Context, list: ArrayList<GpService>,private val itemclick:ItemClickListner) :
    RecyclerView.Adapter<GpServiceAdapter.GpServiceHolder>() {
    private var mContext = context
    private var mList = list

    inner class GpServiceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GpServiceHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_gp_service, parent, false)
        return GpServiceHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: GpServiceHolder, position: Int) {
        val gpService = mList[position]
        holder.itemView.tvName.text = gpService.practice_name
        holder.itemView.tvArea.text = gpService.address
        holder.itemView.tvCity.text = gpService.city

        holder.itemView.setOnClickListener {
            itemclick.onItemClick(gpService, position)
        }
    }
    interface ItemClickListner{
        fun onItemClick(gpService: GpService,position: Int)
    }
}