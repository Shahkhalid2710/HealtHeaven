package com.applocum.connecttomyhealth.ui.specialists.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_specialists.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SpecialistsActivity : BaseActivity(), SpecilistPresenter.View {
    @Inject
    lateinit var presenter: SpecilistPresenter

    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int = R.layout.activity_specialists

    override fun handleInternetConnectivity(isConnect: Boolean?) {

    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        presenter.getDoctorlist()

        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvCancel).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = (Intent(this,BottomNavigationViewActivity::class.java))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finishAffinity()
                overridePendingTransition(0,0)
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
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun getdoctorlist(list: ArrayList<Specialist>) {
        rvDoctors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val specialistsAdapter =
            SpecialistsAdapter(this, list, object : SpecialistsAdapter.ItemClickListner {
                    override fun onItemClick(specialist: Specialist, position: Int) {
                        val intent = Intent(this@SpecialistsActivity, BookSessionActivity::class.java)
                        intent.putExtra("specialist", specialist)
                        startActivity(intent)
                        overridePendingTransition(0,0)
                    }

                    override fun onbookSession(specialist: Specialist, position: Int) {
                        val intent = Intent(this@SpecialistsActivity, AddSymptomActivity::class.java)
                        intent.putExtra("specialist", specialist)
                        val appointment = userHolder.getBookAppointmentData()
                        appointment.therapistId = specialist.id
                        appointment.therapistImage = specialist.image
                        appointment.threapistBio = specialist.bio
                        appointment.therapistName =
                            "${specialist.first_name} ${specialist.last_name}"
                        specialist.usual_address.apply {
                            appointment.therapistAddress = "$line1, $line2,$line3, $town, $pincode"
                        }
                        userHolder.saveBookAppointmentData(appointment)
                        startActivity(intent)
                        overridePendingTransition(0,0)
                    }
                })
        rvDoctors.adapter = specialistsAdapter
    }
    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            rvDoctors.visibility=View.GONE
            noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llSpecialist,R.string.no_internet, Snackbar.LENGTH_LONG).apply { view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5 }
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
        else{
            noInternet.visibility=View.GONE
            rvDoctors.visibility=View.VISIBLE
        }

    }
}