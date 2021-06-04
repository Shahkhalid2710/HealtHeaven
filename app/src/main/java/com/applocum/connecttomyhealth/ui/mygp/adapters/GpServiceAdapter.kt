package com.applocum.connecttomyhealth.ui.mygp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.raw_gp_service.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GpServiceAdapter(context: Context, list: ArrayList<GpService>, private val itemclick: ItemClickListner) :
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

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: GpServiceHolder, position: Int) {
        val gpService = mList[position]
        holder.itemView.tvName.text = capitalize(gpService.practice_name)
        holder.itemView.tvArea.text = capitalize(gpService.address)
        holder.itemView.tvCity.text = capitalize(gpService.city)

        RxView.clicks(holder.itemView).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                itemclick.onItemClick(gpService, position)
            }
    }

    private fun capitalize(capString: String): String {
        val capBuffer = StringBuffer()
        val capMatcher: Matcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase(Locale.ROOT) + capMatcher.group(2).toLowerCase(Locale.ROOT))
        }
        return capMatcher.appendTail(capBuffer).toString()
    }

    interface ItemClickListner{
        fun onItemClick(gpService: GpService,position: Int)
    }
}