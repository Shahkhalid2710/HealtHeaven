package com.applocum.connecttomyhealth.ui.appointment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.convertDateTime
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.item_progress.view.*
import kotlinx.android.synthetic.main.raw_past_session_xml.view.*
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PastSessionAdapter(
    context: Context,
    list: ArrayList<BookAppointmentResponse?>,
    private val onItemClick: ItemClickListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mContext = context
    var mList = list
    private val LOADING = 0
    private val ITEM = 1

    inner class PastSessionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.raw_past_session_xml, parent, false)
                viewHolder = PastSessionHolder(viewItem)
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
        val bookAppointmentResponse = mList[position]
        when (getItemViewType(position)) {
            ITEM -> {
                val pastSessionHolder = holder as PastSessionHolder

                if (bookAppointmentResponse?.gp_details ==null)
                {
                    pastSessionHolder.itemView.tvSessionType.text = bookAppointmentResponse?.appointment_type
                    pastSessionHolder.itemView.tvSlot.text = (bookAppointmentResponse?.duration.toString() + " " + "mins")
                    pastSessionHolder.itemView.tvSessionDateTime.text = bookAppointmentResponse?.actual_start_time?.let { convertDateTime(it) }

                    Glide.with(mContext).load(R.drawable.ic_blank_profile_pic).into(pastSessionHolder.itemView.ivDoctor)
                    holder.itemView.llSessionCancelled.visibility=View.VISIBLE
                    holder.itemView.llDocName.visibility=View.GONE

                }else {
                    pastSessionHolder.itemView.tvDoctorFName.text =
                        bookAppointmentResponse.gp_details.first_name
                    pastSessionHolder.itemView.tvDoctorLName.text =
                        bookAppointmentResponse.gp_details.last_name
                    pastSessionHolder.itemView.tvSessionType.text =
                        bookAppointmentResponse.appointment_type
                    pastSessionHolder.itemView.tvSlot.text =
                        (bookAppointmentResponse.duration.toString() + " " + "mins")
                    pastSessionHolder.itemView.tvSessionDateTime.text =
                        bookAppointmentResponse.actual_start_time.let { convertDateTime(it) }

                    Glide.with(mContext).load(bookAppointmentResponse.gp_details.image)
                        .into(pastSessionHolder.itemView.ivDoctor)

                    holder.itemView.llSessionCancelled.visibility=View.GONE
                    holder.itemView.llDocName.visibility=View.VISIBLE

                }
                when (bookAppointmentResponse?.appointment_type) {
                    "face_to_face" -> {
                        pastSessionHolder.itemView.tvSessionType.text = ("Face to Face")
                    }
                    "phone_call" -> {
                        pastSessionHolder.itemView.tvSessionType.text = ("Phone Call")
                    }
                    "video" -> {
                        pastSessionHolder.itemView.tvSessionType.text = ("Video Call")
                    }
                }

                when (bookAppointmentResponse?.status) {
                    "cancelled" -> {
                        pastSessionHolder.itemView.tvCancelled.visibility = View.VISIBLE
                        pastSessionHolder.itemView.tvMissed.visibility = View.GONE
                    }
                    "not_attended" -> {
                        pastSessionHolder.itemView.tvMissed.visibility = View.VISIBLE
                        pastSessionHolder.itemView.tvCancelled.visibility = View.GONE
                    }
                }

                RxView.clicks(pastSessionHolder.itemView.btnViewDetails)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        bookAppointmentResponse?.let { it1 -> onItemClick.itemClick(it1, position) }
                    }

                RxView.clicks(pastSessionHolder.itemView.btnBookAgain)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        bookAppointmentResponse?.let { it1 ->
                            onItemClick.onButtonClick(
                                it1,
                                position
                            )
                        }
                    }
            }
            LOADING->{
                val loadingViewHolder=holder as LoadingViewHolder
                loadingViewHolder.itemView.itemProgress.visibility=View.VISIBLE
            }
        }
    }

    interface ItemClickListner {
        fun itemClick(bookAppointmentResponse: BookAppointmentResponse, position: Int)
        fun onButtonClick(bookAppointmentResponse: BookAppointmentResponse, position: Int)
    }
}