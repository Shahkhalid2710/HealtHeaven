package com.applocum.connecttomyhealth.ui.appointment

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
import com.applocum.connecttomyhealth.ui.appointment.adapters.PastSessionAdapter
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.applocum.connecttomyhealth.ui.appointment.models.Session
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_past_session_appointment.*
import kotlinx.android.synthetic.main.fragment_past_session_appointment.view.*
import kotlinx.android.synthetic.main.fragment_upcoming_session_apointment.*
import javax.inject.Inject


class PastSessionAppointmentFragment : Fragment(),BookAppointmentPresenter.View {

    @Inject
    lateinit var presenter: BookAppointmentPresenter

    @Inject
    lateinit var userHolder: UserHolder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_past_session_appointment, container, false)

        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        presenter.showPastSession()

        return v
    }

    override fun displayMessage(mesage: String) {
        val snackbar = Snackbar.make(llUpcomingSession,mesage, Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.green))
        snackbar.show()

    }

    override fun getUpcomingSession(list: ArrayList<BookAppointmentResponse>) {
        rvPastSession.layoutManager=LinearLayoutManager(requireActivity())
        rvPastSession.adapter=PastSessionAdapter(requireActivity(),list)

    }

    override fun viewProgress(isShow: Boolean) {
    }
}