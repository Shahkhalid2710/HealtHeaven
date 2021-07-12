package com.applocum.connecttomyhealth.ui.booksession.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.booksession.models.DateModel
import kotlinx.android.synthetic.main.raw_date.view.*

class CustomDateAdapter(context: Context,list: ArrayList<DateModel>):RecyclerView.Adapter<CustomDateAdapter.CustomDateHolder>() {
    var mContext=context
    var mList=list

    inner class CustomDateHolder(itemView: View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomDateHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.raw_date,parent,false)
        return CustomDateHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: CustomDateHolder, position: Int) {
        val dateModel=mList[position]
        holder.itemView.tvDate.text = dateModel.date
        if (dateModel.isChecked)
        {
            holder.itemView.tvDate.setBackgroundResource(R.drawable.custom_dot)
            holder.itemView.tvDate.setTextColor(Color.parseColor("#FFFFFF"))
        }else{
            holder.itemView.tvDate.setBackgroundResource(R.drawable.custom_corner_image)
            holder.itemView.tvDate.setTextColor(Color.parseColor("#828282"))
        }

        holder.itemView.setOnClickListener {
            dateModel.isChecked = !dateModel.isChecked

            if (dateModel.isChecked)
            {
                holder.itemView.tvDate.setBackgroundResource(R.drawable.custom_dot)
                holder.itemView.tvDate.setTextColor(Color.parseColor("#FFFFFF"))
            }else
            {
                holder.itemView.tvDate.setBackgroundResource(R.drawable.custom_corner_image)
                holder.itemView.tvDate.setTextColor(Color.parseColor("#828282"))
            }
        }
    }

    fun selectedItem():ArrayList<DateModel>
    {
        val selected:ArrayList<DateModel> = ArrayList()
        for (i in mList.indices)
        {
            if (mList[i].isChecked) {
                selected.add(mList[i])
                Log.d("Dates","->"+mList[i].date)
            }
        }
        return selected
    }
}