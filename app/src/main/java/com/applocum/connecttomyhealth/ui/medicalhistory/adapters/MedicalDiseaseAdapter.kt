package com.applocum.connecttomyhealth.ui.medicalhistory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.medicalhistory.models.Medical
import kotlinx.android.synthetic.main.item_progress.view.*
import kotlinx.android.synthetic.main.raw_medical_list.view.*

class MedicalDiseaseAdapter(
    context: Context, list: ArrayList<Medical?>, private val onItemClick: ItemClickListnter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mContext = context
    var mList = list
    private val LOADING = 0
    private val ITEM = 1

    inner class MedicalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.raw_medical_list, parent, false)
                viewHolder = MedicalHolder(viewItem)
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
        val medical = mList[position]
        when (getItemViewType(position)) {
            ITEM -> {
                val medicalHolder = holder as MedicalHolder
                medicalHolder.itemView.tvDiseaseName.text = medical?.description

                medicalHolder.itemView.setOnClickListener {
                    medical?.let { it1 -> onItemClick.onItemClick(it1, position) }
                    notifyDataSetChanged()
                    mList.clear()
                }
            }
            LOADING->{
                val loadingViewHolder= holder as MedicalDiseaseAdapter.LoadingViewHolder
                loadingViewHolder.itemView.itemProgress.visibility=View.VISIBLE
            }
        }
    }

    interface ItemClickListnter {
        fun onItemClick(medical: Medical, position: Int)
    }
}