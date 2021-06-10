package com.applocum.connecttomyhealth.ui.notification.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.notification.models.Notification
import kotlinx.android.synthetic.main.raw_notification.view.*

class NotificationAdapter(context: Context,list:ArrayList<Notification>):RecyclerView.Adapter<NotificationAdapter.NotificationHolder>() {
    var mContext=context
    var mList=list

    inner class NotificationHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.raw_notification,parent,false)
        return NotificationHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val notification=mList[position]
        holder.itemView.tvTopic.text=notification.nTitle
        holder.itemView.tvDescription.text=notification.nDescription
        holder.itemView.tvDate.text=notification.nDate
    }
}