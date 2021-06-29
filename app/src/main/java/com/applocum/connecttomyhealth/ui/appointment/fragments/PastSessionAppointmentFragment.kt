package com.applocum.connecttomyhealth.ui.appointment.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.addsymptoms.activities.AddSymptomActivity
import com.applocum.connecttomyhealth.ui.appointment.adapters.PastSessionAdapter
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.applocum.connecttomyhealth.ui.appointment.presenters.BookAppointmentPresenter
import com.applocum.connecttomyhealth.ui.sessiondetails.activities.SessionDetailsActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.fragment_past_session_appointment.*
import kotlinx.android.synthetic.main.fragment_past_session_appointment.view.*
import kotlinx.android.synthetic.main.fragment_past_session_appointment.view.noInternet
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PastSessionAppointmentFragment : Fragment(), BookAppointmentPresenter.View,
    PastSessionAdapter.ItemClickListner {

    @Inject
    lateinit var presenter: BookAppointmentPresenter

    @Inject
    lateinit var userHolder: UserHolder

    lateinit var pastSessionAdapter: PastSessionAdapter

    private var isLoading = false

    lateinit var v: View

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_past_session_appointment, container, false)

        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        pastSessionAdapter= PastSessionAdapter(requireActivity(), ArrayList(),this)
        v.rvPastSession.layoutManager=LinearLayoutManager(requireActivity())
        v.rvPastSession.adapter=pastSessionAdapter
        pastSessionAdapter.notifyDataSetChanged()

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
        pastSessionAdapter.mList.addAll(list)
        pastSessionAdapter.notifyItemRangeInserted(pastSessionAdapter.mList.size,list.size)

        RxRecyclerView.scrollEvents(v.rvPastSession)
            .subscribe {
                val total = v.rvPastSession.layoutManager?.itemCount ?: 0
                val last = (v.rvPastSession.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (total > 0 && total <= last + 2) {
                    if (!isLoading) {
                        presenter.showPastSession()
                    }
                }
            }.let { presenter.disposables.add(it) }
      }

    override fun viewFullProgress(isShow: Boolean) {}

    override fun showProgress() {
        isLoading = true
        v.rvPastSession.post {
            pastSessionAdapter.mList.add(null)
            pastSessionAdapter.notifyItemInserted(pastSessionAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        v.rvPastSession.post {
            pastSessionAdapter.mList.remove(null)
            pastSessionAdapter.notifyItemRemoved(pastSessionAdapter.mList.size)
        }
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            v.rvPastSession.visibility=View.GONE
            v.noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llPastSession,R.string.no_internet, Snackbar.LENGTH_LONG).apply { view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5 }
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            snackBar.show()
        }
        else{
            v.noInternet.visibility=View.GONE
            v.rvPastSession.visibility=View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.showPastSession()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.safeDispose()
    }

    override fun itemClick(bookAppointmentResponse: BookAppointmentResponse, position: Int) {
        val intent = Intent(requireActivity(), SessionDetailsActivity::class.java)
        intent.putExtra("bookAppointmentResponse", bookAppointmentResponse)
        startActivity(intent)
        requireActivity().overridePendingTransition(0,0)
    }

    override fun onButtonClick(bookAppointmentResponse: BookAppointmentResponse, position: Int) {
        val intent=Intent(requireActivity(), AddSymptomActivity::class.java)
        intent.putExtra("specialistId", bookAppointmentResponse.gp_details.id)
        startActivity(intent)
    }
}