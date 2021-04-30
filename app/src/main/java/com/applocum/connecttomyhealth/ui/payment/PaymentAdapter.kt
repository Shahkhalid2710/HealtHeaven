package com.applocum.connecttomyhealth.ui.payment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.addcard.models.Card

class PaymentAdapter(context: Context,list:ArrayList<Card>):RecyclerView.Adapter<PaymentAdapter.PaymentHolder>() {
    var mContext=context
    var mList=list

    inner class PaymentHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.raw_other_payment,parent,false)
        return PaymentHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: PaymentHolder, position: Int) {
        val card=mList[position]
    }
}