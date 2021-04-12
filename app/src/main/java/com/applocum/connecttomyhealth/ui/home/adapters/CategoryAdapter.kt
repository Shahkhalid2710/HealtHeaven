package com.applocum.connecttomyhealth.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.home.model.Category
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.category_raw_xml.view.*

class CategoryAdapter(context: Context, list:ArrayList<Category>):RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    var mContext=context
    var mList=list

    inner class CategoryHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.category_raw_xml,parent,false)
        return CategoryHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val category=mList[position]
        holder.itemView.tvCategoryName.text=category.cName
        Glide.with(mContext).load(category.cImage).into(holder.itemView.ivCategory)
    }
}