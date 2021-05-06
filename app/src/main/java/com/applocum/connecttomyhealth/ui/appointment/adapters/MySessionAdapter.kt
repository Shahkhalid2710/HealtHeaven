package com.applocum.connecttomyhealth.ui.appointment.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.appointment.models.Session
import com.applocum.connecttomyhealth.ui.sessiondetails.SessionDetailsActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.custom_cancel_book_session_dialog.view.*
import kotlinx.android.synthetic.main.raw_session_xml.view.*
import kotlinx.android.synthetic.main.raw_session_xml.view.btnCancel

class MySessionAdapter(context: Context, list: ArrayList<Session>) :
    RecyclerView.Adapter<MySessionAdapter.SessionHolder>() {
    var mContext = context
    var mList = list

    inner class SessionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionHolder {
        val v = LayoutInflater.from(mContext)
            .inflate(com.applocum.connecttomyhealth.R.layout.raw_session_xml, parent, false)
        return SessionHolder(v)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: SessionHolder, position: Int) {
        val session = mList[position]
        holder.itemView.tvDoctorFName.text = session.sName
        holder.itemView.tvSessionType.text = session.sSessionType
        holder.itemView.tvSessionDate.text = session.sDate

        holder.itemView.btnCheckin.setOnClickListener {
            val intent = Intent(mContext, SessionDetailsActivity::class.java)
            mContext.startActivity(intent)
        }

        holder.itemView.btnCancel.setOnClickListener {
            val showDialogView = LayoutInflater.from(mContext)
                .inflate(R.layout.custom_cancel_book_session_dialog, null, false)
            val dialog = AlertDialog.Builder(mContext).create()
            dialog.setView(showDialogView)
            dialog.setCanceledOnTouchOutside(false)

            showDialogView.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            showDialogView.btnNo.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        Glide.with(mContext).load(session.sImage).into(holder.itemView.ivDoctor)
    }
}