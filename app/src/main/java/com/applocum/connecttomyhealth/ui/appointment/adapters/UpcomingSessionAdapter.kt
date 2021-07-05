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
import kotlinx.android.synthetic.main.raw_session_xml.view.*
import kotlinx.android.synthetic.main.raw_session_xml.view.btnCancel
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UpcomingSessionAdapter(
    context: Context,
    list: ArrayList<BookAppointmentResponse?>,
    private val itemCLick: ItemClickListner
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mContext = context
    var mList = list
    private val LOADING = 0
    private val ITEM = 1

    inner class SessionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.raw_session_xml, parent, false)
                viewHolder = SessionHolder(viewItem)
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
                val sessionHolder: SessionHolder = holder as SessionHolder

                sessionHolder.itemView.tvDoctorFName.text =
                    bookAppointmentResponse?.gp_details?.first_name
                sessionHolder.itemView.tvDoctorLName.text =
                    bookAppointmentResponse?.gp_details?.last_name
                sessionHolder.itemView.tvSessionType.text =
                    bookAppointmentResponse?.appointment_type
                sessionHolder.itemView.tvSlot.text =
                    (bookAppointmentResponse?.duration.toString() + " " + "mins")
                sessionHolder.itemView.tvSessionDateTime.text =
                    bookAppointmentResponse?.start_time?.let { convertDateTime(it) }

                Glide.with(mContext).load(bookAppointmentResponse?.gp_details?.image)
                    .into(sessionHolder.itemView.ivDoctor)

                when (bookAppointmentResponse?.appointment_type) {
                    "face_to_face" -> {
                        sessionHolder.itemView.tvSessionType.text = ("Face to Face")
                        sessionHolder.itemView.btnCheckin.text = ("Check-in")
                    }
                    "phone_call" -> {
                        sessionHolder.itemView.tvSessionType.text = ("Phone Call")
                        sessionHolder.itemView.btnCheckin.text = ("Join")
                    }
                    "video" -> {
                        sessionHolder.itemView.tvSessionType.text = ("Video Call")
                        sessionHolder.itemView.btnCheckin.text = ("Join")
                    }
                }

                if (bookAppointmentResponse!!.activate_waiting_room) {
                    when (sessionHolder.itemView.btnCheckin.text) {
                        "Join"->{ sessionHolder.itemView.btnCheckin.isEnabled = true}
                        "Check-in"->{ sessionHolder.itemView.btnCheckin.isEnabled = true}
                    }
                }else
                {
                    when (sessionHolder.itemView.btnCheckin.text) {
                        "Join"->{ sessionHolder.itemView.btnCheckin.isEnabled = false}
                        "Check-in"->{ sessionHolder.itemView.btnCheckin.isEnabled = false}
                    }
                }


                RxView.clicks(sessionHolder.itemView.btnCheckin)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        when (sessionHolder.itemView.btnCheckin.text.toString()) {
                            "Check-in" -> {
                                itemCLick.onCheckInButtonClick(bookAppointmentResponse,sessionHolder.adapterPosition)
                              }
                            "Join" -> {
                                itemCLick.onJoinButtonClick(bookAppointmentResponse, sessionHolder.adapterPosition)
                            }
                    }
                }

                RxView.clicks(sessionHolder.itemView.btnCancel)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        bookAppointmentResponse.let { it1 -> itemCLick.itemClick(it1,sessionHolder.adapterPosition) }
                    }
            }
        }
    }

    fun removeAppointment(id:Int ,position: Int)
    {

        mList.indices
            .filter { id == mList[it]?.id }
            .forEach { mList.remove(mList[it]) }
        if (mList.size == 0) {
        }
        notifyItemRemoved(position)
    }

    interface ItemClickListner {
        fun itemClick(bookAppointmentResponse: BookAppointmentResponse, position: Int)
        fun onCheckInButtonClick(bookAppointmentResponse: BookAppointmentResponse, position: Int)
        fun onJoinButtonClick(bookAppointmentResponse: BookAppointmentResponse, position: Int)
    }
}