package com.applocum.connecttomyhealth.ui.addcard

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_add_card.*


class AddCardActivity : BaseActivity(), TextWatcher {
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        etCardNumber.addTextChangedListener(this)
        etNameOnCard.addTextChangedListener {
            if (etNameOnCard.text.length > 10) {
                ivSuccessNameonCard.visibility = View.VISIBLE
            } else {
                ivSuccessNameonCard.visibility = View.GONE
            }
        }

        etExpiryDate.addTextChangedListener {
            if (etExpiryDate.text.length ==5) {
                ivSuccessExpiryDate.visibility = View.VISIBLE
            } else {
                ivSuccessExpiryDate.visibility = View.GONE
            }
        }


        etCardNumber.filters = arrayOf(InputFilter.LengthFilter(20))
        btnAdd.setOnClickListener { finish() }

    }

    override fun getLayoutResourceId(): Int = R.layout.activity_add_card

    override fun afterTextChanged(s: Editable?) {
        if (count == 4) {
            var str = s.toString()
            str += " "
            etCardNumber.setText(str)
            etCardNumber.setSelection(str.length)
            count = 0
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, a: Int) {
        count++
        if (s.length == 20) {
            ivSuccessCardNumber.visibility = View.VISIBLE
        } else {
            ivSuccessCardNumber.visibility = View.GONE
        }
    }
}