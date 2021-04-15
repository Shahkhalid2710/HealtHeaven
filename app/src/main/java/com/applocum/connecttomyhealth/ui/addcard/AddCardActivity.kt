package com.applocum.connecttomyhealth.ui.addcard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_add_card.*

class AddCardActivity : BaseActivity(), TextWatcher {
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        etCardNumber.addTextChangedListener(this)

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

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, a: Int) {
        count++
    }
}