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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.*
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.activities.AddCardActivity
import com.applocum.connecttomyhealth.ui.addcard.presenters.AddCardPresenter
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.applocum.connecttomyhealth.ui.appointment.presenters.BookAppointmentPresenter
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.payment.adapters.PaymentCardAdapter
import com.applocum.connecttomyhealth.ui.verificationdocument.activities.VerifyIdentityActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_payment_show.*
import kotlinx.android.synthetic.main.activity_payment_show.btnConfirmSessionBooking
import kotlinx.android.synthetic.main.activity_payment_show.progress
import kotlinx.android.synthetic.main.activity_payment_show.rvSavedCards
import kotlinx.android.synthetic.main.custom_booked_succesfully_dialog.*
import kotlinx.android.synthetic.main.custom_payment_add.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

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

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)
        bookAppointmentPresenter.injectView(this)

        val bookAppointment = userHolder.getBookAppointmentData()
        bookAppointment.corporateId = 66
        userHolder.saveBookAppointmentData(bookAppointment)

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

                        bookAppointmentPresenter.bookAppointment(
                            bookAppointment.appointmentTime,
                            bookAppointment.appointmentSlot,
                            bookAppointment.appointmentReason,
                            bookAppointment.allowGeoAccess,
                            bookAppointment.sharedRecordWithNhs,
                            appointmentType,
                            bookAppointment.therapistId,
                            selectCard,
                            bookAppointment.corporateId
                        )
                        openDialog()
                    }
                }
            }


        RxView.clicks(tvAddNewCode).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCardActivity::class.java))
            }
        RxView.clicks(tvAddmembershipcode).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCodeActivity::class.java))
            }
        RxView.clicks(tvAddPaymentMethod).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCardActivity::class.java))
            }
        RxView.clicks(btnConfirmSessionBook).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, VerifyIdentityActivity::class.java))
            }

        RxView.clicks(etAddCode).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCodeActivity::class.java))
            }

        presenter.showSavedCards()

    }

    override fun displaymessage(message: String) {}

    override fun displaySuccessmessage(message: String) {}

    override fun addcard(card: Card) {}

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
        rvSavedCards.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun viewFullProgress(isShow: Boolean) {}

    override fun showcard(list: ArrayList<Card>) {
        if (list.isEmpty()) {
            llPayment.visibility = View.VISIBLE
            llPaymentShow.visibility = View.GONE
        } else {
            llPayment.visibility = View.GONE
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
            dialog.dismiss()
            val intent = (Intent(this, BottomNavigationViewActivity::class.java))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finishAffinity()
        }
        dialog.show()
    }

    override fun displayMessage(mesage: String) {
        Toast.makeText(this, mesage, Toast.LENGTH_SHORT).show()
    }

    override fun displaySuccessMessage(message: String) {}

    override fun getSessions(list: ArrayList<BookAppointmentResponse>) {}

    override fun onResume() {
        presenter.showSavedCards()
        super.onResume()
    }
}