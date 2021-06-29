package com.applocum.connecttomyhealth.ui.investigation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertInvestigationDate
import com.applocum.connecttomyhealth.ui.investigation.models.Investigation
import kotlinx.android.synthetic.main.raw_investigation_xml.view.*

class InvestigationAdapter(context: Context, list: ArrayList<Investigation?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mContext = context
    var mList = list
    private val LOADING = 0
    private val ITEM = 1

    inner class InvestigationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.raw_investigation_xml, parent, false)
                viewHolder = InvestigationHolder(viewItem)
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
        val investigation = mList[position]
        when (getItemViewType(position)) {
            ITEM -> {
                val investigationHolder = holder as InvestigationHolder
                investigationHolder.itemView.tvInvestigationName.text = investigation?.snomed_code
                investigationHolder.itemView.tvInvestigationDate.text =
                    investigation?.taken_on?.let { convertInvestigationDate(it) }
                investigationHolder.itemView.tvInvestigationDescription.text =
                    investigation?.description

                investigationHolder.itemView.llInvestigation.setOnClickListener {
                    if (investigationHolder.itemView.llDes.visibility == View.GONE) {
                        investigationHolder.itemView.llDes.visibility = View.VISIBLE
                        investigationHolder.itemView.ivList.setImageResource(R.drawable.ic_drop_list_top)

                    } else {
                        investigationHolder.itemView.llDes.visibility = View.GONE
                        investigationHolder.itemView.ivList.setImageResource(R.drawable.ic_drop_list)
                    }
                }
                investigationHolder.itemView.ivList.setOnClickListener {
                    if (investigationHolder.itemView.llDes.visibility == View.GONE) {
                        investigationHolder.itemView.llDes.visibility = View.VISIBLE
                        investigationHolder.itemView.ivList.setImageResource(R.drawable.ic_drop_list_top)

                    } else {
                        investigationHolder.itemView.llDes.visibility = View.GONE
                        investigationHolder.itemView.ivList.setImageResource(R.drawable.ic_drop_list)
                    }
                }
            }
        }
    }
}