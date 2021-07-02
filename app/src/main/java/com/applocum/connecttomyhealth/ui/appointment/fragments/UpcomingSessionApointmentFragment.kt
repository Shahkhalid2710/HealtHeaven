package com.applocum.connecttomyhealth.ui.appointment.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.appointment.adapters.UpcomingSessionAdapter
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.applocum.connecttomyhealth.ui.appointment.presenters.BookAppointmentPresenter
import com.applocum.connecttomyhealth.ui.mysessions.activities.MySessionActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_cancel_book_session_dialog.view.*
import kotlinx.android.synthetic.main.custom_location_permission_dialog.view.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.custom_walk_in_waiting_room_dialog.view.*
import kotlinx.android.synthetic.main.fragment_upcoming_session_apointment.*
import kotlinx.android.synthetic.main.fragment_upcoming_session_apointment.view.*
import kotlinx.android.synthetic.main.fragment_upcoming_session_apointment.view.noInternet
import kotlinx.android.synthetic.main.raw_session_xml.view.btnCancel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpcomingSessionApointmentFragment : Fragment(), BookAppointmentPresenter.View,
    UpcomingSessionAdapter.ItemClickListner {

    private lateinit var upcomingSessionAdapter: UpcomingSessionAdapter

    var locationRequestCode=241

    @Inject
    lateinit var presenter: BookAppointmentPresenter

    @Inject
    lateinit var userHolder: UserHolder

    var bookAppointmentPosition = 0

    var mListUpcomingSession: ArrayList<BookAppointmentResponse> = ArrayList()

    private var isLoading = false

    lateinit var v: View

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_upcoming_session_apointment, container, false)

        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        upcomingSessionAdapter = UpcomingSessionAdapter(requireActivity(), ArrayList(), this)
        v.rvUpcomingSession.layoutManager = LinearLayoutManager(requireActivity())
        v.rvUpcomingSession.adapter = upcomingSessionAdapter
        upcomingSessionAdapter.notifyDataSetChanged()

        RxView.clicks(v.noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                v.noInternet.visibility = View.GONE
                presenter.showUpcomingSession()
            }

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
        upcomingSessionAdapter.mList.removeAt(bookAppointmentPosition)

        checkList()
        val snackbar = Snackbar.make(llUpcomingSession, message, Snackbar.LENGTH_SHORT)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.blue))
        snackbar.show()
    }

    override fun getSessions(list: ArrayList<BookAppointmentResponse>) {
        mListUpcomingSession = list
        checkList()

        upcomingSessionAdapter.mList.addAll(list)
        upcomingSessionAdapter.notifyItemRangeInserted(upcomingSessionAdapter.mList.size, list.size)

        RxRecyclerView.scrollEvents(v.rvUpcomingSession)
            .subscribe {
                val total = v.rvUpcomingSession.layoutManager?.itemCount ?: 0
                val last = (v.rvUpcomingSession.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (total > 0 && total <= last + 2) {
                    if (!isLoading) {
                        presenter.showUpcomingSession()
                    }
                }
            }.let { presenter.disposables.add(it) }
    }

    override fun viewFullProgress(isShow: Boolean) {
        v.upcomingProgress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showProgress() {
        isLoading = true
        v.rvUpcomingSession.post {
            upcomingSessionAdapter.mList.add(null)
            upcomingSessionAdapter.notifyItemInserted(upcomingSessionAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        v.rvUpcomingSession.post {
            upcomingSessionAdapter.mList.remove(null)
            upcomingSessionAdapter.notifyItemRemoved(upcomingSessionAdapter.mList.size)
        }
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect) {
            v.rvUpcomingSession.visibility = View.GONE
            v.noInternet.visibility = View.VISIBLE

            val snackBar =
                Snackbar.make(llUpcomingSession, R.string.no_internet, Snackbar.LENGTH_LONG).apply {
                    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines =
                        5
                }
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            snackBar.show()
        } else {
            v.noInternet.visibility = View.GONE
            v.rvUpcomingSession.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.showUpcomingSession()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.safeDispose()
    }

    private fun checkList() {
        if (mListUpcomingSession.isNullOrEmpty()) {
            v.layoutNotFoundUpcomingSession.visibility = View.VISIBLE
            v.rvUpcomingSession.visibility = View.GONE
        } else {
            v.layoutNotFoundUpcomingSession.visibility = View.GONE
            v.rvUpcomingSession.visibility = View.VISIBLE
        }
    }

    override fun itemClick(bookAppointmentResponse: BookAppointmentResponse, position: Int) {
        bookAppointmentPosition = position
        val showDialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.custom_cancel_book_session_dialog, null, false)
        val dialog = AlertDialog.Builder(requireActivity()).create()
        dialog.setView(showDialogView)

        showDialogView.btnCancel.setOnClickListener {
            presenter.deleteSession(bookAppointmentResponse.id)
            dialog.dismiss()
        }
        showDialogView.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onCheckInButtonClick(bookAppointmentResponse: BookAppointmentResponse, position: Int) {
        val showDialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.custom_walk_in_waiting_room_dialog, null, false)
        val dialog = AlertDialog.Builder(requireActivity()).create()
        dialog.setView(showDialogView)

        showDialogView.btnContinue.setOnClickListener {

            dialog.dismiss()
        }
        showDialogView.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onJoinButtonClick(bookAppointmentResponse: BookAppointmentResponse, position: Int) {

        if (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            val showDialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.custom_location_permission_dialog, null, false)
            val dialog = AlertDialog.Builder(requireActivity()).create()
            dialog.setView(showDialogView)

            showDialogView.tvContinue.setOnClickListener {
                locationPermission()
                dialog.dismiss()
            }
            showDialogView.tvCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }else
        {
            startActivity(Intent(requireActivity(), MySessionActivity::class.java))
        }
    }

    private fun locationPermission() {
        ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), locationRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 241) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(Intent(requireActivity(),MySessionActivity::class.java))
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // now, user has denied permission (but not permanently!)
                } else {
                    val dialog = AlertDialog.Builder(context,R.style.AlertDialogCustom)
                    dialog.setTitle("Permission Required")
                    dialog.setCancelable(false)
                    dialog.setMessage("You have to Allow permission to access user location")
                    dialog.setPositiveButton("Settings"
                    ) { _, which ->
                        val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", requireActivity().packageName, null))
                        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivityForResult(i, 241)
                    }
                    val alertDialog = dialog.create()
                    alertDialog.show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
