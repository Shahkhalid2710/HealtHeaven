package com.applocum.connecttomyhealth.ui.payment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.payment.models.MembershipResponse
import kotlinx.android.synthetic.main.raw_code_membership.view.*

class MembershipAdapter(context: Context, list:ArrayList<MembershipResponse>,val isshow:Boolean,private val codeClick:CodeClickListener):RecyclerView.Adapter<MembershipAdapter.PaymentHolder>() {
    var mContext=context
    var mList=list
    var isShow=isshow
    private var selectCode=-1

    inner class PaymentHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.raw_code_membership,parent,false)
        return PaymentHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: PaymentHolder, position: Int) {
        val membershipResponse=mList[position]

        holder.itemView.tvCode.text=membershipResponse.membership_code
        holder.itemView.tvSessionAvailable.text=(membershipResponse.max_usage_count.toString() +" "+"free sessions available")

        if (isShow) holder.itemView.cbMemberships.visibility=View.VISIBLE
        else holder.itemView.cbMemberships.visibility=View.GONE

        holder.itemView.llPaymentCode.setOnClickListener {
            selectCode=position
            codeClick.codeClick(membershipResponse, position)
            notifyDataSetChanged()
        }

        if (selectCode == position)
        {
            holder.itemView.cbMemberships.isChecked=true
        }
        else
        {
            holder.itemView.cbMemberships.isChecked=false
        }
    }
    interface CodeClickListener{
        fun codeClick(membershipResponse: MembershipResponse, position: Int)
    }
}