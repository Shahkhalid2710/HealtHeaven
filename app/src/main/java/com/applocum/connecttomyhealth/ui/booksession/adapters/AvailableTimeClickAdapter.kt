package com.applocum.connecttomyhealth.ui.booksession.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertAvailableTimeSlots
import com.applocum.connecttomyhealth.ui.booksession.models.SessionType
import com.applocum.connecttomyhealth.ui.booksession.models.Time
import kotlinx.android.synthetic.main.raw_session_booking.view.*

class AvailableTimeClickAdapter(context: Context, list:ArrayList<Time>, private var itemClickListner: ItemClickListner) :
    RecyclerView.Adapter<AvailableTimeClickAdapter.SessionTypeHolder>(){
    var mContext=context
    var mList=list
    private var selectedItem:Int=-1


    inner class SessionTypeHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionTypeHolder {
        val v= LayoutInflater.from(mContext).inflate(R.layout.raw_session_booking,parent,false)
        return SessionTypeHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: SessionTypeHolder, position: Int) {
        val time=mList[position]

        holder.itemView.tvName.text= convertAvailableTimeSlots(time.start_time)
        holder.itemView.setOnClickListener {
            itemClickListner.onItemClick(time, position)
            selectedItem =position
            notifyDataSetChanged()
        }
        if (selectedItem == holder.adapterPosition) {
            holder.itemView.rl.setBackgroundResource(R.drawable.custom_btn)
            holder.itemView.tvName.setTextColor(Color.parseColor("#FFFFFF"))
        }
        else {
            holder.itemView.rl.setBackgroundResource(R.drawable.default_button)
            holder.itemView.tvName.setTextColor(Color.parseColor("#008976"))
        }
    }
    interface ItemClickListner
    {
        fun onItemClick(time: Time, position: Int)
    }
}