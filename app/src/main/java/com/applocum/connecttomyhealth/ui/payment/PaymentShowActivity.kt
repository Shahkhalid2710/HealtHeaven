package com.applocum.connecttomyhealth.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.AddCardPresenter
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.applocum.connecttomyhealth.ui.payment.adapters.PaymentCardAdapter
import com.applocum.connecttomyhealth.ui.verificationdocument.activities.VerifyIdentityActivity
import kotlinx.android.synthetic.main.activity_payment_method.*
import kotlinx.android.synthetic.main.activity_payment_show.*
import kotlinx.android.synthetic.main.activity_payment_show.progress
import kotlinx.android.synthetic.main.activity_payment_show.rvSavedCards
import kotlinx.android.synthetic.main.custom_loader_progress.*
import javax.inject.Inject

class PaymentShowActivity : BaseActivity(),AddCardPresenter.View {

    @Inject
    lateinit var presenter: AddCardPresenter

    override fun getLayoutResourceId(): Int=R.layout.activity_payment_show
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        btnConfirmSessionBooking.setOnClickListener {
            startActivity(Intent(this,VerifyIdentityActivity::class.java))
        }
        presenter.showSavedCards()

    }

    override fun displaymessage(message: String?) {}

    override fun addcard(card: Card) {}

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun showcard(list: ArrayList<Card>) {
        rvSavedCards.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rvSavedCards.adapter= PaymentCardAdapter(this,list,true)
    }
}