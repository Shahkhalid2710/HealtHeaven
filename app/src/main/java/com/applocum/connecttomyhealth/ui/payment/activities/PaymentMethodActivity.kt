package com.applocum.connecttomyhealth.ui.payment.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.activities.AddCardActivity
import com.applocum.connecttomyhealth.ui.addcard.presenters.AddCardPresenter
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.applocum.connecttomyhealth.ui.payment.adapters.PaymentCardAdapter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_payment_method.*
import kotlinx.android.synthetic.main.activity_payment_method.ivBack
import kotlinx.android.synthetic.main.custom_cancel_saved_card_dialog.view.*
import kotlinx.android.synthetic.main.custom_loader_progress.*
import kotlinx.android.synthetic.main.custom_payment.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PaymentMethodActivity : BaseActivity(), AddCardPresenter.View {

    lateinit var paymentCardAdapter: PaymentCardAdapter

    @Inject
    lateinit var presenter: AddCardPresenter

    var cardPosition:Int = 0
    var mList:ArrayList<Card> = ArrayList()

    override fun getLayoutResourceId(): Int = R.layout.activity_payment_method

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvAddPaymentMethod).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCardActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(btnAddCard).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCardActivity::class.java))
                overridePendingTransition(0,0)
            }

        presenter.showSavedCards()

    }

    override fun displaymessage(message: String) {
        val snackbar = Snackbar.make(flPaymentMethod, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessmessage(message: String) {
        mList.removeAt(cardPosition)
        mList.trimToSize()
        paymentCardAdapter.notifyItemRemoved(cardPosition)
        checkList()
    }

    override fun addcard(card: Card) {}

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {
        paymentmethodprogress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showcard(list: ArrayList<Card>) {
        mList=list
        checkList()
        rvSavedCards.layoutManager = LinearLayoutManager(this)
        paymentCardAdapter = PaymentCardAdapter(this, list, false, true,
            object : PaymentCardAdapter.CardClickListener {
                override fun cardClick(card: Card, position: Int) {}

                override fun deleteCardClick(card: Card, position: Int) {
                    val showDialogView = LayoutInflater.from(this@PaymentMethodActivity).inflate(R.layout.custom_cancel_saved_card_dialog, null, false)
                    val dialog = AlertDialog.Builder(this@PaymentMethodActivity).create()
                    dialog.setView(showDialogView)

                    cardPosition=position

                    showDialogView.btnRemove.setOnClickListener {
                        dialog.dismiss()
                        presenter.deleteCard(card.id)
                    }
                    showDialogView.btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            })
        rvSavedCards.adapter = paymentCardAdapter
        paymentCardAdapter.notifyDataSetChanged()
    }

    fun checkList(){

        if (mList.isEmpty()) {
            layoutnotfoundcard.visibility = View.VISIBLE
            llSavedCards.visibility = View.GONE
            tvAddPaymentMethod.visibility = View.GONE
        } else {
            layoutnotfoundcard.visibility = View.GONE
            llSavedCards.visibility = View.VISIBLE
            tvAddPaymentMethod.visibility = View.VISIBLE
        }

    }
    override fun onResume() {
        presenter.showSavedCards()
        super.onResume()
    }
}