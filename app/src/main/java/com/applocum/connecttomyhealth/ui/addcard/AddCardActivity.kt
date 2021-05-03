package com.applocum.connecttomyhealth.ui.addcard

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.applocum.connecttomyhealth.ui.payment.PaymentMethodActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_card.*
import kotlinx.android.synthetic.main.custom_progress.*
import javax.inject.Inject

class AddCardActivity : BaseActivity(), TextWatcher, AddCardPresenter.View {
    var count = 0

    @Inject
    lateinit var presenter: AddCardPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_card
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        ivBack.setOnClickListener { finish() }
        etCardNumber.addTextChangedListener(this)

      /*  etHolderName.addTextChangedListener {
            if (etHolderName.text.length > 5) ivSuccessNameonCard.visibility =
                View.VISIBLE else ivSuccessNameonCard.visibility = View.GONE
        }*/

        etExpiryDate.addTextChangedListener {
            if (etExpiryDate.text.length == 4) ivSuccessExpiryDate.visibility =
                View.VISIBLE else ivSuccessExpiryDate.visibility = View.GONE
        }

        etCVV.addTextChangedListener {
            if (etCVV.text.length >= 1) ivCrossCVV.visibility =
                View.VISIBLE else ivCrossCVV.visibility = View.GONE

            etCardNumber.filters = arrayOf(InputFilter.LengthFilter(20))
            ivCrossCVV.setOnClickListener {
                etCVV.text.clear()
                ivCrossCVV.visibility = View.GONE
            }
        }

        if (etCardNumber.text.toString().contains("\\s")) {
            etCardNumber.text.toString().replace("\\s", "")
        }

        btnAdd.setOnClickListener {
            presenter.addCard(
                etCardNumber.text.toString(),
                etHolderName.text.toString(),
                etExpiryDate.text.toString(),
                etCVV.text.toString()
            )
        }

    }

    override fun afterTextChanged(s: Editable?) {
        /* if (count == 4) {
             var str = s.toString()
             str += " "
             etCardNumber.setText(str)
             etCardNumber.setSelection(str.length)*/
        count = 0
        // }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, a: Int) {
        count++
        if (s.length == 16) {
            ivSuccessCardNumber.visibility = View.VISIBLE
        } else {
            ivSuccessCardNumber.visibility = View.GONE
        }
    }

    override fun displaymessage(message: String?) {
        val snackbar = Snackbar.make(llAddCard, message.toString(), Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        snackbar.show()
    }

    override fun addcard(card: Card) {
        startActivity(Intent(this, PaymentMethodActivity::class.java))
        finish()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showcard(list: ArrayList<Card>) {
    }
}