package com.applocum.connecttomyhealth.ui.othernotes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertDocumentTime
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.raw_download.view.*
import java.util.concurrent.TimeUnit

class OtherNoteAdapter(
    context: Context,
    list: ArrayList<Document?>,
    private val onNoteClick: NoteClickListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mContext = context
    var mList = list
    private val LOADING = 0
    private val ITEM = 1

    inner class OtherNoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.raw_download, parent, false)
                viewHolder = OtherNoteHolder(viewItem)
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
                val otherNoteHolder = holder as OtherNoteHolder

                otherNoteHolder.itemView.tvDocType.text = mContext.resources.getString(R.string.others_notes)
                otherNoteHolder.itemView.tvDate.text = document?.created_at?.let {
                    convertDocumentTime(
                        it
                    )
                }
                otherNoteHolder.itemView.tvDoctorName.text = ("By" + " " + document?.by)

                RxView.clicks(otherNoteHolder.itemView).throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        document?.let { it1 -> onNoteClick.onNoteClick(it1, position) }
                    }
            }
        }
    }

    interface NoteClickListner {
        fun onNoteClick(document: Document, position: Int)
    }
}