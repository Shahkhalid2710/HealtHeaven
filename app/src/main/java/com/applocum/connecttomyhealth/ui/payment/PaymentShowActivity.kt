package com.applocum.connecttomyhealth.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.AddCardPresenter
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.applocum.connecttomyhealth.ui.verificationdocument.activities.VerifyIdentityActivity
import kotlinx.android.synthetic.main.activity_payment_show.*
import javax.inject.Inject

class PaymentShowActivity : BaseActivity(),AddCardPresenter.View {

    @Inject
    lateinit var presenter: AddCardPresenter
    override fun getLayoutResourceId(): Int=R.layout.activity_payment_show
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnConfirmSessionBooking.setOnClickListener {
            startActivity(Intent(this,VerifyIdentityActivity::class.java))
        }

    }

    override fun displaymessage(message: String?) {}

    override fun addcard(card: Card) {}

    override fun viewProgress(isShow: Boolean) {}

    override fun showcard(list: ArrayList<Card>) {

    }
}