package com.applocum.connecttomyhealth.ui.addcard.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.presenters.AddCardPresenter
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_add_card.*
import kotlinx.android.synthetic.main.activity_add_card.ivBack
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddCardActivity : BaseActivity(), TextWatcher,
    AddCardPresenter.View {
    private var count = 0
    lateinit var v: View

    @Inject
    lateinit var presenter: AddCardPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_card

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        etCardNumber.addTextChangedListener(this)

        etHolderName.addTextChangedListener {
            if (etHolderName.text.length >= 5) ivSuccessNameonCard.visibility =
                View.VISIBLE else ivSuccessNameonCard.visibility = View.GONE
        }

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

        RxView.clicks(btnAdd).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.addCard(
                    etCardNumber.text.toString().trim(),
                    etHolderName.text.toString(),
                    etExpiryDate.text.toString(),
                    etCVV.text.toString()
                )
            }
    }

    override fun afterTextChanged(s: Editable?) {
/*         if (count == 4) {
             var str = s.toString()
             str += " "
             etCardNumber.setText(str)
             etCardNumber.setSelection(str.length)
             count = 0
         }*/
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, a: Int) {
        count++
        if (s.length >= 16) {
            ivSuccessCardNumber.visibility = View.VISIBLE
        } else {
            ivSuccessCardNumber.visibility = View.GONE
        }

    }

    override fun displaymessage(message: String) {
        val snackBar = Snackbar.make(llAddCard, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun displaySuccessmessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun addcard(card: Card) {
        finish()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {}

    override fun showcard(list: ArrayList<Card>) {}
}