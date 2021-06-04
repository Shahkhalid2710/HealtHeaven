package com.applocum.connecttomyhealth.ui.booksession.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.booksession.models.SessionType
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.raw_session_booking.view.*
import java.util.concurrent.TimeUnit

class SelectSlotAdapter(context: Context, list: ArrayList<SessionType>, private var itemClickListner: ItemClickListner) : RecyclerView.Adapter<SelectSlotAdapter.SessionTypeHolder>() {
    var mContext = context
    var mList = list
    private var selectedItem: Int = 0

    inner class SessionTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionTypeHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_slot, parent, false)
        return SessionTypeHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: SessionTypeHolder, position: Int) {
        val sessionType = mList[position]
        holder.itemView.tvName.text = sessionType.sName

        RxView.clicks(holder.itemView).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                itemClickListner.onItemClick(sessionType, position)
                selectedItem = position
                notifyDataSetChanged()
            }

        if (selectedItem == holder.adapterPosition) {
            holder.itemView.rl.setBackgroundResource(R.drawable.custom_btn)
            holder.itemView.tvName.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.rl.setBackgroundResource(R.drawable.default_button)
            holder.itemView.tvName.setTextColor(Color.parseColor("#008976"))
        }
    }

    interface ItemClickListner {
        fun onItemClick(sessionType: SessionType, position: Int)
    }
}