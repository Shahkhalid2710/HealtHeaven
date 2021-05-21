package com.applocum.connecttomyhealth.ui.payment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import kotlinx.android.synthetic.main.raw_other_payment.view.*

class PaymentCardAdapter(context: Context, list:ArrayList<Card>,val isshow:Boolean,isDeleteShow:Boolean,private val cardClick:CardClickListener):RecyclerView.Adapter<PaymentCardAdapter.PaymentHolder>() {
    var mContext=context
    var mList=list
    var isShow=isshow
    var isDeleteShow=isDeleteShow
    var selectCard=-1

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
        val expires="Expires"
        holder.itemView.tvCard.text=(card.card_type+" "+card.card_number)
        holder.itemView.tvHolderName.text=card.card_holder_name
        holder.itemView.tvDate.text = (expires+" "+card.expiry_date.toString())

        if (isShow) holder.itemView.cbotherPayment.visibility=View.VISIBLE
        else holder.itemView.cbotherPayment.visibility=View.GONE

        if (isDeleteShow) holder.itemView.ivDelete.visibility=View.VISIBLE
        else holder.itemView.ivDelete.visibility=View.GONE

        holder.itemView.llPaymentCard.setOnClickListener {
            selectCard=position
            cardClick.cardClick(card, position)
            notifyDataSetChanged()
        }

        holder.itemView.ivDelete.setOnClickListener {
            cardClick.deleteCardClick(card, position)
            notifyDataSetChanged()
        }

        if (selectCard == position)
        {
            holder.itemView.cbotherPayment.isChecked=true
        }
        else
        {
            holder.itemView.cbotherPayment.isChecked=false
        }
    }
    interface CardClickListener{
        fun cardClick(card: Card,position: Int)
        fun deleteCardClick(card: Card,position: Int)
    }
}