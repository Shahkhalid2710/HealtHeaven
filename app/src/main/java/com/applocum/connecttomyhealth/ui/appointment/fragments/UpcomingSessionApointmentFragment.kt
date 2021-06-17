package com.applocum.connecttomyhealth.ui.appointment.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.appointment.presenters.BookAppointmentPresenter
import com.applocum.connecttomyhealth.ui.appointment.adapters.UpcomingSessionAdapter
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.applocum.connecttomyhealth.ui.waitingarea.activities.WaitingAreaActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custom_cancel_book_session_dialog.view.*
import kotlinx.android.synthetic.main.custom_loader_progress.view.progress
import kotlinx.android.synthetic.main.fragment_upcoming_session_apointment.*
import kotlinx.android.synthetic.main.fragment_upcoming_session_apointment.view.*
import kotlinx.android.synthetic.main.raw_session_xml.view.btnCancel
import javax.inject.Inject

class UpcomingSessionApointmentFragment : Fragment(),BookAppointmentPresenter.View {

    lateinit var upcomingSessionAdapter: UpcomingSessionAdapter

    @Inject
    lateinit var presenter: BookAppointmentPresenter

    @Inject
    lateinit var userHolder: UserHolder

    var bookAppointmentPosition=0
    var mListUpcomingSession:ArrayList<BookAppointmentResponse> = ArrayList()

    lateinit var v: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_upcoming_session_apointment, container, false)

        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)
        checkList()

        presenter.showUpcomingSession()

        return v
    }

    override fun displayMessage(mesage: String) {
        val snackbar = Snackbar.make(llUpcomingSession, mesage, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
        snackbar.show()
    }

    override fun displaySuccessMessage(message: String) {
        mListUpcomingSession.removeAt(bookAppointmentPosition)
        upcomingSessionAdapter.notifyItemRemoved(bookAppointmentPosition)
        mListUpcomingSession.trimToSize()

        checkList()
        val snackbar = Snackbar.make(llUpcomingSession, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.blue))
        snackbar.show()
    }

    override fun getSessions(list: ArrayList<BookAppointmentResponse>) {
        mListUpcomingSession=list
        checkList()
        rvUpcomingSession.layoutManager = LinearLayoutManager(requireActivity())
        upcomingSessionAdapter = UpcomingSessionAdapter(requireActivity(), list,
            object : UpcomingSessionAdapter.ItemClickListner {
                override fun itemClick(bookAppointmentResponse: BookAppointmentResponse, position: Int) {
                    val showDialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.custom_cancel_book_session_dialog, null, false)
                    val dialog = AlertDialog.Builder(requireActivity()).create()
                    dialog.setView(showDialogView)

                    bookAppointmentPosition=position

                    showDialogView.btnCancel.setOnClickListener {
                        presenter.deleteSession(bookAppointmentResponse.id)
                        dialog.dismiss()
                    }
                    showDialogView.btnNo.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }

                override fun onButtonClick(bookAppointmentResponse: BookAppointmentResponse, position: Int) {
                    val intent = Intent(requireActivity(), WaitingAreaActivity::class.java)
                    startActivity(intent)
                   requireActivity().overridePendingTransition(0,0)
                }
            })

        rvUpcomingSession.adapter = upcomingSessionAdapter
        upcomingSessionAdapter.notifyDataSetChanged()
    }

    override fun viewProgress(isShow: Boolean) {
        v.progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {
        v.upcomingProgress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        presenter.showUpcomingSession()
        super.onResume()
    }

    private fun checkList()
    {
        if (mListUpcomingSession.isEmpty()) {
            v.layoutNotFoundUpcomingSession.visibility = View.VISIBLE
            v.rvUpcomingSession.visibility = View.GONE
        } else {
            v.layoutNotFoundUpcomingSession.visibility = View.GONE
            v.rvUpcomingSession.visibility = View.VISIBLE
        }
    }

}