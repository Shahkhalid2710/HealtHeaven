package com.applocum.connecttomyhealth.ui.specialists.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.BookAppointment
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addsymptoms.activities.AddSymptomActivity
import com.applocum.connecttomyhealth.ui.booksession.activities.BookSessionActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.specialists.adapters.SpecialistsAdapter
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.applocum.connecttomyhealth.ui.specialists.presenters.SpecilistPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_specialists.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SpecialistsActivity : BaseActivity(), SpecilistPresenter.View,
    SpecialistsAdapter.ItemClickListner {

    private lateinit var specialistsAdapter: SpecialistsAdapter

    private var isLoading = false

    @Inject
    lateinit var presenter: SpecilistPresenter

    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int = R.layout.activity_specialists

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        specialistsAdapter = SpecialistsAdapter(this, ArrayList(), this)
        rvDoctors.layoutManager = LinearLayoutManager(this)
        rvDoctors.adapter = specialistsAdapter

        presenter.getDoctorlist()

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvCancel).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = (Intent(this, BottomNavigationViewActivity::class.java))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finishAffinity()
                overridePendingTransition(0, 0)
            }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.getDoctorlist()
            }

        val appointment = BookAppointment()
        appointment.corporateId = 66
        userHolder.saveBookAppointmentData(appointment)

    }

    override fun displaymessage(message: String) {
        val snackBar = Snackbar.make(flSpecialist,message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.safeDispose()
    }

    override fun getdoctorlist(list: ArrayList<Specialist?>, page:String?) {
        if (page == "1")
        {
            specialistsAdapter.updateList(list)
        }
        else {
            specialistsAdapter.mList.addAll(list)
            specialistsAdapter.notifyItemRangeInserted(specialistsAdapter.mList.size, list.size)
            RxRecyclerView.scrollEvents(rvDoctors)
                .subscribe {
                    val total = rvDoctors.layoutManager?.itemCount ?: 0
                    val last = (rvDoctors.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()
                    if (total > 0 && total <= last + 2) {
                        if (!isLoading) {
                            presenter.getDoctorlist()
                        }
                    }
                }.let { presenter.disposables.add(it) }
        }
    }

    override fun viewProgress(isShow: Boolean) {}

    override fun showProgress() {
        isLoading = true
        rvDoctors.post {
            specialistsAdapter.mList.add(null)
            specialistsAdapter.notifyItemInserted(specialistsAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        rvDoctors.post {
            specialistsAdapter.mList.remove(null)
            specialistsAdapter.notifyItemRemoved(specialistsAdapter.mList.size)
        }
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect) {
            rvDoctors.visibility = View.GONE
            noInternet.visibility = View.VISIBLE

            val snackBar = Snackbar.make(flSpecialist, R.string.no_internet, Snackbar.LENGTH_LONG)
                .apply { view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5 }
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        } else {
            noInternet.visibility = View.GONE
            rvDoctors.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(specialist: Specialist, position: Int) {
        val intent = Intent(this@SpecialistsActivity, BookSessionActivity::class.java)
        intent.putExtra("specialist", specialist)
        intent.putExtra("specialistId", specialist.id)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onbookSession(specialist: Specialist, position: Int) {
        val intent = Intent(this@SpecialistsActivity, AddSymptomActivity::class.java)
        intent.putExtra("specialist", specialist)
        intent.putExtra("specialistId", specialist.id)
        val appointment = userHolder.getBookAppointmentData()
        appointment.therapistId = specialist.id
        appointment.therapistImage = specialist.image
        appointment.threapistBio = specialist.bio
        appointment.therapistName =
            "${specialist.first_name} ${specialist.last_name}"
        specialist.usual_address?.apply {
            appointment.therapistAddress = "$line1, $line2,$line3, $town, $pincode"
        }
        userHolder.saveBookAppointmentData(appointment)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}