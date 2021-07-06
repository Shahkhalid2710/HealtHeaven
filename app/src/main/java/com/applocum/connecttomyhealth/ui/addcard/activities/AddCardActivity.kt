package com.applocum.connecttomyhealth.ui.addcard.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.applocum.connecttomyhealth.ui.addcard.presenters.AddCardPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_add_card.*
import kotlinx.android.synthetic.main.activity_add_card.ivBack
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AddCardActivity : BaseActivity(), AddCardPresenter.View {
    private var count2 = 0
    lateinit var v: View

    @Inject
    lateinit var presenter: AddCardPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_card

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        etCardNumber.addTextChangedListener(FourDigitCardFormatWatcher())
        etExpiryDate.addTextChangedListener(editTextDateWatcher)

        etCardNumber.addTextChangedListener {
            if (etCardNumber.text.length >=19) {
                ivSuccessCardNumber.visibility = View.VISIBLE
            } else {
                ivSuccessCardNumber.visibility = View.GONE
            }
        }

        etHolderName.addTextChangedListener {
            if (etHolderName.text.length >= 2) ivSuccessNameonCard.visibility = View.VISIBLE else ivSuccessNameonCard.visibility = View.GONE
        }

        etCVV.addTextChangedListener {
            if (etCVV.text.length >= 1) ivCrossCVV.visibility = View.VISIBLE else ivCrossCVV.visibility = View.GONE
            etCardNumber.filters = arrayOf(InputFilter.LengthFilter(20))

            ivCrossCVV.setOnClickListener {
                etCVV.text.clear()
                ivCrossCVV.visibility = View.GONE
            }
        }

        RxView.clicks(btnAdd).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(llAddCard.windowToken, 0)
                presenter.addCard(etCardNumber.text.toString().replace(" ",""),etHolderName.text.toString(), etExpiryDate.text.toString().replace("/",""), etCVV.text.toString())
            }
    }

    var editTextDateWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (etExpiryDate.text.length == 5) ivSuccessExpiryDate.visibility =
                View.VISIBLE else ivSuccessExpiryDate.visibility = View.GONE
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            count2 = etExpiryDate.text.toString().length
        }

        override fun afterTextChanged(s: Editable) {
                val str = s.length
                if ((count2 < str) && (str == 2 || str == 5)) {
                s.append("/")
            }
        }
    }

    override fun displaymessage(message: String) {
        val snackBar = Snackbar.make(llAddCard, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun displaySuccessmessage(message: String) {}

    override fun addcard(card: Card) {
        finish()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {}

    override fun showcard(list: ArrayList<Card>) {}

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            val snackBar = Snackbar.make(llAddCard,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
    }

    class FourDigitCardFormatWatcher : TextWatcher {
        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {}

        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {}

        override fun afterTextChanged(s: Editable) {
            if (s.isNotEmpty() && s.length % 5 == 0) {
                val c = s[s.length - 1]
                if (space == c) {
                    s.delete(s.length - 1, s.length)
                }
            }
            if (s.isNotEmpty() && s.length % 5 == 0) {
                val c = s[s.length - 1]
                if (Character.isDigit(c) && TextUtils.split(s.toString(), space.toString()).size <= 3) {
                    s.insert(s.length - 1, space.toString())
                }
            }
        }

        companion object {
            private const val space = ' '
        }
    }
}
