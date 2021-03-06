package com.applocum.connecttomyhealth.ui.fitnote.adapters

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

class FitNoteAdapter(
    context: Context, list: ArrayList<Document?>, private val onNoteClick: FitNoteClickListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mContext = context
    var mList = list
    private val LOADING = 0
    private val ITEM = 1

    inner class FitNoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.raw_download, parent, false)
                viewHolder = FitNoteHolder(viewItem)
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
                val fitNoteHolder = holder as FitNoteHolder
                fitNoteHolder.itemView.tvDocType.text =
                    mContext.resources.getString(R.string.fit_note)
                fitNoteHolder.itemView.tvDate.text = document?.created_at?.let {
                    convertDocumentTime(
                        it
                    )
                }
                fitNoteHolder.itemView.tvDoctorName.text = ("By" + " " + document?.by)

                RxView.clicks(fitNoteHolder.itemView).throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        document?.let { it1 -> onNoteClick.onNoteClick(it1, position) }
                    }
            }
            LOADING->{
                val loadingViewHolder =holder as FitNoteAdapter.LoadingViewHolder
                loadingViewHolder.itemView.itemProgress.visibility=View.VISIBLE
            }
        }
    }

    interface FitNoteClickListner {
        fun onNoteClick(document: Document, position: Int)
    }
}