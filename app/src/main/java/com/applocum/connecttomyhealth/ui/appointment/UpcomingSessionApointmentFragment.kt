package com.applocum.connecttomyhealth.ui.appointment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.appointment.adapters.UpcomingSessionAdapter
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custom_cancel_book_session_dialog.view.*
import kotlinx.android.synthetic.main.custom_loader_progress.view.*
import kotlinx.android.synthetic.main.fragment_upcoming_session_apointment.*
import kotlinx.android.synthetic.main.raw_session_xml.view.btnCancel
import javax.inject.Inject


class UpcomingSessionApointmentFragment : Fragment(),BookAppointmentPresenter.View {

    lateinit var upcomingSessionAdapter: UpcomingSessionAdapter

    @Inject
    lateinit var presenter: BookAppointmentPresenter

    @Inject
    lateinit var userHolder: UserHolder

    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_upcoming_session_apointment, container, false)

        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        presenter.showUpcomingSession()

        return v
    }

    override fun displayMessage(mesage: String) {
        val snackbar = Snackbar.make(llUpcomingSession,mesage, Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
        snackbar.show()
    }

    override fun getSessions(list: ArrayList<BookAppointmentResponse>) {
        rvUpcomingSession.layoutManager=LinearLayoutManager(requireActivity())
        upcomingSessionAdapter= UpcomingSessionAdapter(requireActivity(),list,object:UpcomingSessionAdapter.ItemClickListner{
            override fun itemClick(bookAppointmentResponse: BookAppointmentResponse, position: Int) {
                val showDialogView = LayoutInflater.from(requireActivity())
                    .inflate(R.layout.custom_cancel_book_session_dialog, null, false)
                val dialog = AlertDialog.Builder(requireActivity()).create()
                dialog.setView(showDialogView)
                dialog.setCanceledOnTouchOutside(false)

                showDialogView.btnCancel.setOnClickListener {
                     presenter.deleteSession(bookAppointmentResponse.id)
                     list.removeAt(position)
                     upcomingSessionAdapter.notifyItemRemoved(position)
                     upcomingSessionAdapter.notifyItemRangeChanged(position,list.size)
                     dialog.dismiss()
                }
                showDialogView.btnNo.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }

            override fun onButtonClick(bookAppointmentResponse: BookAppointmentResponse, position: Int) {

            }
        })
        rvUpcomingSession.adapter=upcomingSessionAdapter
        upcomingSessionAdapter.notifyDataSetChanged()
    }

    override fun viewProgress(isShow: Boolean) {
       v.progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}