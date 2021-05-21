package com.applocum.connecttomyhealth.ui.payment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.AddCardActivity
import com.applocum.connecttomyhealth.ui.addcard.AddCardPresenter
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.applocum.connecttomyhealth.ui.payment.adapters.PaymentCardAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_payment_method.*
import kotlinx.android.synthetic.main.custom_cancel_saved_card_dialog.view.*
import kotlinx.android.synthetic.main.custom_loader_progress.*
import kotlinx.android.synthetic.main.custom_payment.*
import javax.inject.Inject

class PaymentMethodActivity : BaseActivity(),AddCardPresenter.View {

    lateinit var paymentCardAdapter: PaymentCardAdapter

    @Inject
    lateinit var presenter: AddCardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        presenter.showSavedCards()


        tvAddPaymentMethod.setOnClickListener {
            startActivity(Intent(this,AddCardActivity::class.java))
        }
        btnAddCard.setOnClickListener {
            startActivity(Intent(this,AddCardActivity::class.java))
        }


    }

    override fun getLayoutResourceId(): Int=R.layout.activity_payment_method

    override fun displaymessage(message: String) {
        val snackbar = Snackbar.make(flPaymentMethod, message, Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this,R.color.red))
        snackbar.show()
    }

    override fun displaySuccessmessage(message: String) {}

    override fun addcard(card: Card) {
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {
        paymentmethodprogress.visibility=if (isShow)View.VISIBLE else View.GONE
    }

    override fun showcard(list: ArrayList<Card>) {
        if (list.isEmpty())
        {
            layoutnotfoundcard.visibility=View.VISIBLE
            llSavedCards.visibility=View.GONE
            tvAddPaymentMethod.visibility=View.GONE
        }
        else
        {
            layoutnotfoundcard.visibility=View.GONE
            llSavedCards.visibility=View.VISIBLE
            tvAddPaymentMethod.visibility=View.VISIBLE
        }
        rvSavedCards.layoutManager=LinearLayoutManager(this)
        paymentCardAdapter=PaymentCardAdapter(this,list,false,true,object :PaymentCardAdapter.CardClickListener{
            override fun cardClick(card: Card, position: Int) {

            }
            override fun deleteCardClick(card: Card, position: Int) {
                val showDialogView = LayoutInflater.from(this@PaymentMethodActivity)
                    .inflate(R.layout.custom_cancel_saved_card_dialog, null, false)
                val dialog = AlertDialog.Builder(this@PaymentMethodActivity).create()
                dialog.setView(showDialogView)
                dialog.setCanceledOnTouchOutside(false)

                showDialogView.btnRemove.setOnClickListener {
                    presenter.deleteCard(card.id)
                    paymentCardAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                showDialogView.btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        })
        rvSavedCards.adapter=paymentCardAdapter
        paymentCardAdapter.notifyDataSetChanged()
    }
}