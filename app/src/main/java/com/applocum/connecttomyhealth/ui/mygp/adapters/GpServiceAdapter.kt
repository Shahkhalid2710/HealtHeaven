package com.applocum.connecttomyhealth.ui.mygp.adapters

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
class GpServiceAdapter(
    context: Context,
    list: ArrayList<GpService?>,
    private val itemclick: ItemClickListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mContext = context
    var mList = list
    private val LOADING = 0
    private val ITEM = 1

    inner class GpServiceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.raw_gp_service, parent, false)
                viewHolder = GpServiceHolder(viewItem)
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
        val gpService = mList[position]
        when (getItemViewType(position)) {
            ITEM -> {
                val gpServiceHolder = holder as GpServiceHolder

                gpServiceHolder.itemView.tvName.text = gpService?.practice_name?.let { capitalize(it) }
                gpServiceHolder.itemView.tvArea.text = gpService?.address?.let { capitalize(it) }
                gpServiceHolder.itemView.tvCity.text = gpService?.city?.let { capitalize(it) }

                RxView.clicks(gpServiceHolder.itemView).throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        gpService?.let { it1 -> itemclick.onItemClick(it1, position) }
                    }
            }
        }
    }

    private fun capitalize(capString: String): String {
        val capBuffer = StringBuffer()
        val capMatcher: Matcher =
            Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
        while (capMatcher.find()) {
            capMatcher.appendReplacement(
                capBuffer,
                capMatcher.group(1).toUpperCase(Locale.ROOT) + capMatcher.group(2)
                    .toLowerCase(Locale.ROOT)
            )
        }
        return capMatcher.appendTail(capBuffer).toString()
    }


    interface ItemClickListner {
        fun onItemClick(gpService: GpService, position: Int)
    }
}