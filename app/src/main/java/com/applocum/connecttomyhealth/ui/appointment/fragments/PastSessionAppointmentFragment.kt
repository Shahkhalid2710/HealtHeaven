package com.applocum.connecttomyhealth.ui.appointment.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.appointment.presenters.BookAppointmentPresenter
import com.applocum.connecttomyhealth.ui.appointment.adapters.PastSessionAdapter
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.applocum.connecttomyhealth.ui.sessiondetails.activities.SessionDetailsActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_loader_progress.view.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.fragment_past_session_appointment.*
import kotlinx.android.synthetic.main.fragment_past_session_appointment.view.*
import kotlinx.android.synthetic.main.fragment_past_session_appointment.view.noInternet
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PastSessionAppointmentFragment : Fragment(), BookAppointmentPresenter.View {

    @Inject
    lateinit var presenter: BookAppointmentPresenter

    @Inject
    lateinit var userHolder: UserHolder

    lateinit var v: View

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_past_session_appointment, container, false)

        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        RxView.clicks(v.noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                v.noInternet.visibility=View.GONE
                presenter.showPastSession()
            }

        return v
    }

    override fun displayMessage(mesage: String) {
        val snackbar = Snackbar.make(llPastSession, mesage, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
        snackbar.show()

    }

    override fun displaySuccessMessage(message: String) {}

    override fun getSessions(list: ArrayList<BookAppointmentResponse>) {
        if (list.isEmpty()) {
            layoutNotFoundPastSession.visibility = View.VISIBLE
            rvPastSession.visibility = View.GONE
        } else {
            layoutNotFoundPastSession.visibility = View.GONE
            rvPastSession.visibility = View.VISIBLE
        }
        rvPastSession.layoutManager = LinearLayoutManager(requireActivity())
        rvPastSession.adapter = PastSessionAdapter(requireActivity(), list, object : PastSessionAdapter.ItemClickListner {
                override fun itemClick(bookAppointmentResponse: BookAppointmentResponse, position: Int) {
                    val intent = Intent(requireActivity(), SessionDetailsActivity::class.java)
                    intent.putExtra("bookAppointmentResponse", bookAppointmentResponse)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0,0)
                }
                override fun onButtonClick(bookAppointmentResponse: BookAppointmentResponse,position: Int) {
                  /*  val intent=Intent(requireActivity(),AddSymptomActivity::class.java)
                    intent.putExtra("bookAppointmentResponse", bookAppointmentResponse)
                    startActivity(intent)*/
                }
            })
      }

    override fun viewProgress(isShow: Boolean) {
        v.progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {}

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            v.rvPastSession.visibility=View.GONE
            v.noInternet.visibility=View.VISIBLE
            v.layoutNotFoundPastSession.visibility=View.GONE

            val snackBar = Snackbar.make(llPastSession,R.string.no_internet, Snackbar.LENGTH_LONG).apply { view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5 }
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            snackBar.show()
        }
        else{
            v.noInternet.visibility=View.GONE
            v.rvPastSession.visibility=View.VISIBLE
            v.layoutNotFoundPastSession.visibility=View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.showPastSession()
    }
}