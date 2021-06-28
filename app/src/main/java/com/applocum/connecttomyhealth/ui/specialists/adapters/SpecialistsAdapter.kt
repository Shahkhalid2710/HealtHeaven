package com.applocum.connecttomyhealth.ui.specialists.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.raw_doctor_xml.view.*
import java.util.concurrent.TimeUnit

class SpecialistsAdapter(
    context: Context,
    list: ArrayList<Specialist?>,
    private val onitemclick: ItemClickListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext = context
    var mList = list
    private val LOADING = 0
    private val ITEM = 1

    inner class SpecialistHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.raw_doctor_xml, parent, false)
                viewHolder = SpecialistHolder(viewItem)
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
        val specialist = mList[position]
        when (getItemViewType(position)) {
            ITEM -> {
                val specialistHolder: SpecialistHolder = holder as SpecialistHolder
                specialistHolder.itemView.tvDoctorFirstName.text = specialist?.first_name
                specialistHolder.itemView.tvDoctorLastName.text = specialist?.last_name
                specialistHolder.itemView.tvProf.text = specialist?.designation
                specialistHolder.itemView.tvDes.text = specialist?.bio

                RxView.clicks(specialistHolder.itemView.btnViewProfile)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        onitemclick.onItemClick(specialist!!, position)
                    }

                RxView.clicks(specialistHolder.itemView.btnBookSession)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        onitemclick.onbookSession(specialist!!, position)
                    }

                if (specialist?.image.isNullOrEmpty()) {
                    Glide.with(mContext).load(R.drawable.ic_blank_profile_pic)
                        .into(specialistHolder.itemView.ivDoctor)
                } else {
                    Glide.with(mContext).load(specialist?.image)
                        .into(specialistHolder.itemView.ivDoctor)
                }
            }
        }
    }

    interface ItemClickListner {
        fun onItemClick(specialist: Specialist, position: Int)
        fun onbookSession(specialist: Specialist, position: Int)
    }
}