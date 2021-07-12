package com.applocum.connecttomyhealth.ui.payment.activities

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.activities.AddCardActivity
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.applocum.connecttomyhealth.ui.addcard.presenters.AddCardPresenter
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.applocum.connecttomyhealth.ui.appointment.presenters.BookAppointmentPresenter
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.payment.adapters.PaymentCardAdapter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_payment_show.*
import kotlinx.android.synthetic.main.custom_booked_succesfully_dialog.*
import kotlinx.android.synthetic.main.custom_payment_add.*
import kotlinx.android.synthetic.main.custom_payment_add.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PaymentShowActivity : BaseActivity(), AddCardPresenter.View, BookAppointmentPresenter.View {

    @Inject
    lateinit var presenter: AddCardPresenter

    @Inject
    lateinit var bookAppointmentPresenter: BookAppointmentPresenter

    @Inject
    lateinit var userHolder: UserHolder

    private var selectCard = 0

    private var appointmentType = ""

    override fun getLayoutResourceId(): Int = R.layout.activity_payment_show

    override fun handleInternetConnectivity(isConnect: Boolean?) {
        if (!isConnect!!) {
            val snackbar = Snackbar.make(llPaymentShow,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()        }
    }

    @SuppressLint("CheckResult", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)
        bookAppointmentPresenter.injectView(this)

        val date=intent.getStringExtra("date")
        val cost=intent.getStringExtra("cost")

        tvTotalCost.text=cost
        tvSessionTotalCost.text=cost

        RxView.clicks(customPaymentAdd.tvCancelCustom).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = (Intent(this,BottomNavigationViewActivity::class.java))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finishAffinity()
                overridePendingTransition(0,0)
            }

        RxView.clicks(tvCancelPayment).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = (Intent(this,BottomNavigationViewActivity::class.java))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finishAffinity()
                overridePendingTransition(0,0)
            }

        RxView.clicks(ivBackPayment).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {finish()}

        RxView.clicks(customPaymentAdd.ivBackCustom).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {finish()}

        val bookAppointment = userHolder.getBookAppointmentData()
        bookAppointment.corporateId = 66
        userHolder.saveBookAppointmentData(bookAppointment)


        tvDate.text = date

        customPaymentAdd.tvSessionDate.text = date


        RxView.clicks(tvAddNewCode).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCardActivity::class.java))
                overridePendingTransition(0,0)
            }
        RxView.clicks(tvAddmembershipcode).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCodeActivity::class.java))
                overridePendingTransition(0,0)
            }
        RxView.clicks(tvAddPaymentMethod).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCardActivity::class.java))
                overridePendingTransition(0,0)
            }

         RxView.clicks(customPaymentAdd.btnConfirmSessionBook).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {

                when (selectCard) {
                    0 -> {
                        val snackbar = Snackbar.make(llPaymentShow, "Please select at least one method ", Snackbar.LENGTH_LONG)
                        snackbar.changeFont()
                        val snackview = snackbar.view
                        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                        snackbar.show()
                    }
                    else -> {
                        when (bookAppointment.appointmentType) {
                            "phone_call_appointment" -> {
                                appointmentType = "phone_call"
                            }
                            "online_appointment" -> {
                                appointmentType = "video"
                            }
                            "offline_appointment" -> {
                                appointmentType = "face_to_face"
                            }
                        }

                        bookAppointmentPresenter.bookAppointment(bookAppointment,appointmentType,selectCard)
                    }
                }
            }
        RxView.clicks(btnConfirmSessionBooking).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                when (selectCard) {
                  0 -> {
                    val snackbar = Snackbar.make(llPaymentShow, "Please select at least one method ", Snackbar.LENGTH_LONG)
                    snackbar.changeFont()
                    val snackview = snackbar.view
                    snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                    snackbar.show()
                }
                else -> {
                    when (bookAppointment.appointmentType) {
                        "phone_call_appointment" -> {
                            appointmentType = "phone_call"
                        }
                        "online_appointment" -> {
                            appointmentType = "video"
                        }
                        "offline_appointment" -> {
                            appointmentType = "face_to_face"
                        }
                    }

                    bookAppointmentPresenter.bookAppointment(bookAppointment,appointmentType,selectCard)
                }
              }
            }

        RxView.clicks(etAddCode).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCodeActivity::class.java))
                overridePendingTransition(0,0)
            }
    }

    override fun displaymessage(message: String) {
        val snackbar = Snackbar.make(flPayment,message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessmessage(message: String) {}

    override fun addcard(card: Card) {}

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
        rvSavedCards.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun viewFullProgress(isShow: Boolean) {
        bookingProgress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun showProgress() {}

    override fun hideProgress() {}

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            val snackbar = Snackbar.make(flPayment, R.string.no_internet, Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
        }
    }

    override fun removeApppintment(appointmentId: Int) {}

    override fun showcard(list: ArrayList<Card>) {
        if (list.isEmpty()) {
            customPaymentAdd.visibility = View.VISIBLE
            llPaymentShow.visibility = View.GONE
        } else {
            customPaymentAdd.visibility = View.GONE
            llPaymentShow.visibility = View.VISIBLE
        }
        rvSavedCards.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvSavedCards.adapter = PaymentCardAdapter(this, list, true, false,
            object : PaymentCardAdapter.CardClickListener {
                override fun cardClick(card: Card, position: Int) {
                    selectCard = card.id
                }

                override fun deleteCardClick(card: Card, position: Int) {}
            })
    }

    private fun openDialog() {
        val dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_booked_succesfully_dialog)
        val window = dialog.window
        val wlp: WindowManager.LayoutParams = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )

        dialog.btnDone.setOnClickListener {

            val intent = (Intent(this, BottomNavigationViewActivity::class.java))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

            finishAffinity()
            overridePendingTransition(0,0)
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun displayMessage(mesage: String) {
        val snackbar = Snackbar.make(flPayment, mesage, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessMessage(message: String) {
        openDialog()
    }

    override fun displaySuccessCheckInMessage(message: String) {}

    override fun getSessions(list: ArrayList<BookAppointmentResponse>) {}

    override fun onResume() {
        super.onResume()
        presenter.showSavedCards()
    }
}