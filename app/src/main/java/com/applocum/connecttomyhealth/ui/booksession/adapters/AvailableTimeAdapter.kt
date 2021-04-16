package com.applocum.connecttomyhealth.ui.booksession.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.booksession.models.SessionType
import kotlinx.android.synthetic.main.raw_session_booking.view.*

class AvailableTimeAdapter(context: Context, list: ArrayList<SessionType>) :
    RecyclerView.Adapter<AvailableTimeAdapter.SessionTypeHolder>() {
    var mContext = context
    var mList = list

    inner class SessionTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionTypeHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.raw_session_booking, parent, false)
        return SessionTypeHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: SessionTypeHolder, position: Int) {
        val sessionType = mList[position]

        holder.itemView.tvName.text = sessionType.sName
    }
}