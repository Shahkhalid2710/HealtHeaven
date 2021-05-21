package com.applocum.connecttomyhealth.ui.payment

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
import com.applocum.connecttomyhealth.ui.addcard.AddCardActivity
import com.applocum.connecttomyhealth.ui.addcard.AddCardPresenter
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.applocum.connecttomyhealth.ui.appointment.BookAppointmentPresenter
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.applocum.connecttomyhealth.ui.bottomnavigationview.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.payment.adapters.PaymentCardAdapter
import com.applocum.connecttomyhealth.ui.verificationdocument.activities.VerifyIdentityActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_payment_show.*
import kotlinx.android.synthetic.main.activity_payment_show.btnConfirmSessionBooking
import kotlinx.android.synthetic.main.activity_payment_show.progress
import kotlinx.android.synthetic.main.activity_payment_show.rvSavedCards
import kotlinx.android.synthetic.main.custom_booked_succesfully_dialog.*
import kotlinx.android.synthetic.main.custom_payment_add.*
import javax.inject.Inject

class PaymentShowActivity : BaseActivity(),AddCardPresenter.View,BookAppointmentPresenter.View {

    @Inject
    lateinit var presenter: AddCardPresenter

    @Inject
    lateinit var bookAppointmentPresenter: BookAppointmentPresenter

    @Inject
    lateinit var userHolder: UserHolder

    private var selectCard = 0

    override fun getLayoutResourceId(): Int=R.layout.activity_payment_show
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)
        bookAppointmentPresenter.injectView(this)

        val bookAppointment=userHolder.getBookAppointmentData()
        bookAppointment.corporateId = 66
        userHolder.saveBookAppointmentData(bookAppointment)

        btnConfirmSessionBooking.setOnClickListener {
            when (selectCard) {
                0 -> {
                    val snackbar =
                        Snackbar.make(llPaymentShow, "Please select card", Snackbar.LENGTH_LONG)
                    val snackview = snackbar.view
                    snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                    snackbar.show()
                }
                else -> {
                    bookAppointmentPresenter.bookAppointment(
                        bookAppointment.appointmentTime,
                        bookAppointment.appointmentSlot,
                        bookAppointment.appointmentReason,
                        bookAppointment.allowGeoAccess,
                        bookAppointment.sharedRecordWithNhs,
                        bookAppointment.appointmentType,
                        bookAppointment.therapistId,
                        selectCard,
                        bookAppointment.corporateId)

                    openDialog()
                }
            }
        }
        tvAddNewCode.setOnClickListener {
            startActivity(Intent(this,AddCardActivity::class.java))
        }
        tvAddmembershipcode.setOnClickListener {
            startActivity(Intent(this,AddCodeActivity::class.java))
        }
        tvAddPaymentMethod.setOnClickListener {
            startActivity(Intent(this,AddCardActivity::class.java))
        }
        btnConfirmSessionBook.setOnClickListener {
            startActivity(Intent(this,VerifyIdentityActivity::class.java))
        }

        etAddCode.setOnClickListener {
            startActivity(Intent(this,AddCodeActivity::class.java))
        }
        presenter.showSavedCards()

    }

    override fun displaymessage(message: String) {}

    override fun displaySuccessmessage(message: String) {}

    override fun addcard(card: Card) {}

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {

    }

    override fun showcard(list: ArrayList<Card>) {
        if (list.isEmpty())
        {
            llPayment.visibility=View.VISIBLE
            llPaymentShow.visibility=View.GONE
        }
        else
        {
            llPayment.visibility=View.GONE
            llPaymentShow.visibility=View.VISIBLE
        }
        rvSavedCards.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rvSavedCards.adapter= PaymentCardAdapter(this,list,true,false,object:PaymentCardAdapter.CardClickListener{
            override fun cardClick(card: Card, position: Int) {
                selectCard=card.id
            }

            override fun deleteCardClick(card: Card, position: Int) {

            }
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
            val intent=(Intent(this, BottomNavigationViewActivity::class.java))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()

        }
        dialog.show()
    }

    override fun displayMessage(mesage: String) {
        Toast.makeText(this,mesage,Toast.LENGTH_SHORT).show()
    }

    override fun displaySuccessMessage(message: String) {

    }

    override fun getSessions(list: ArrayList<BookAppointmentResponse>) {}
}