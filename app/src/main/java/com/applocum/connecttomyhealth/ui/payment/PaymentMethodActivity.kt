package com.applocum.connecttomyhealth.ui.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.AddCardActivity
import com.applocum.connecttomyhealth.ui.addcard.AddCardPresenter
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.applocum.connecttomyhealth.ui.payment.adapters.PaymentCardAdapter
import kotlinx.android.synthetic.main.activity_payment_method.*
import kotlinx.android.synthetic.main.custom_loader_progress.*
import kotlinx.android.synthetic.main.custom_payment.*
import javax.inject.Inject

class PaymentMethodActivity : BaseActivity(),AddCardPresenter.View {

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

    override fun displaymessage(message: String?) {
    }

    override fun displaySuccessmessage(message: String?) {
    }

    override fun addcard(card: Card) {
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun showcard(list: ArrayList<Card>) {
        if (list.isEmpty())
        {
            layoutnotfoundcard.visibility=View.VISIBLE
            llSavedCards.visibility=View.GONE
        }
        else
        {
            layoutnotfoundcard.visibility=View.GONE
            llSavedCards.visibility=View.VISIBLE
        }
        rvSavedCards.layoutManager=LinearLayoutManager(this)
        rvSavedCards.adapter=PaymentCardAdapter(this,list,false,object :PaymentCardAdapter.CardClickListener{
            override fun cardClick(card: Card, position: Int) {

            }
        })
    }
}