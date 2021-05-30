package com.applocum.connecttomyhealth.ui.home.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.doctor_raw_xml.view.*
import kotlinx.android.synthetic.main.fragment_about_specialist.view.*
import java.util.*
import kotlin.collections.ArrayList


class DoctorAdapter(context: Context,list:ArrayList<Specialist>,private val doctorClick:DoctorClickListner):RecyclerView.Adapter<DoctorAdapter.DoctorHolder>(){
    var mContext=context
    var mList=list
    lateinit var view:View
    private var lastPosition = -1

    inner class DoctorHolder(itemview:View):RecyclerView.ViewHolder(itemview){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.doctor_raw_xml,parent,false)
        return DoctorHolder(v)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: DoctorHolder, position: Int) {
        val specialist = mList[position]
        holder.itemView.tvDoctorFName.text=specialist.first_name
        holder.itemView.tvDoctorLName.text=specialist.last_name
        holder.itemView.tvDoctorDesignation.text=specialist.designation
        holder.itemView.tvDoctorEmail.text=specialist.email
        Glide.with(mContext).load(specialist.image).into(holder.itemView.ivDoctor)

        holder.itemView.cvDoctor.setOnClickListener {
            doctorClick.onDoctorClick(specialist, position)
        }
        setAnimation(holder.itemView,position)
    }
   /* private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(mContext,android.R.anim.slide_out_right)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }*/

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration = Random().nextInt(501).toLong()
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }
    }
    interface DoctorClickListner{
        fun onDoctorClick(specialist: Specialist,position: Int)
    }

}