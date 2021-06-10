package com.applocum.connecttomyhealth.ui.photoid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.profiledetails.models.Documents
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_document.view.*

class PhotoIdAdapter(context: Context,list:ArrayList<Documents>,private val documentClick:DocumentClick):RecyclerView.Adapter<PhotoIdAdapter.PhotoIdHolder>() {
    var mContext=context
    var mList=list

    inner class PhotoIdHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoIdHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.raw_document,parent,false)
        return PhotoIdHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: PhotoIdHolder, position: Int) {
        val documents=mList[position]

        val circularProgressDrawable = CircularProgressDrawable(mContext)
        circularProgressDrawable.strokeWidth = 8f
        circularProgressDrawable.centerRadius = 40f
        circularProgressDrawable.start()
        Glide.with(mContext).load(documents.file_url).placeholder(circularProgressDrawable).into(holder.itemView.ivDocument)

        holder.itemView.ivDelete.setOnClickListener {
            documentClick.deleteDocument(documents, position)
        }
    }

    interface DocumentClick{
        fun deleteDocument(documents: Documents,position: Int)
    }
}