package com.applocum.connecttomyhealth.ui.referral.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertDocumentTime
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.item_progress.view.*
import kotlinx.android.synthetic.main.raw_download.view.*
import java.util.concurrent.TimeUnit

class ReferralAdapter(
    context: Context,
    list: ArrayList<Document?>,
    private val onReferralClick: ReferralClickListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mContext = context
    var mList = list
    private val LOADING = 0
    private val ITEM = 1

    inner class ReferralHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.raw_download, parent, false)
                viewHolder = ReferralHolder(viewItem)
            }
            LOADING -> {
                val viewLoading: View = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingViewHolder(viewLoading)
            }
        }
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mList[position] == null) LOADING else ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val document = mList[position]
        when (getItemViewType(position)) {
            ITEM -> {
                val referralHolder = holder as ReferralHolder
                referralHolder.itemView.tvDocType.text =
                    mContext.resources.getString(R.string.referral)
                referralHolder.itemView.tvDate.text = document?.created_at?.let {
                    convertDocumentTime(
                        it
                    )
                }
                referralHolder.itemView.tvDoctorName.text = ("By" + " " + document?.by)

                RxView.clicks(referralHolder.itemView).throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        document?.let { it1 -> onReferralClick.onReferralClick(it1, position) }
                    }
            }
            LOADING->{
                val loadingViewHolder= holder as ReferralAdapter.LoadingViewHolder
                loadingViewHolder.itemView.itemProgress.visibility=View.VISIBLE
            }
        }
    }

    interface ReferralClickListner {
        fun onReferralClick(document: Document, position: Int)
    }
}