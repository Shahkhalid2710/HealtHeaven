package com.applocum.connecttomyhealth.ui.confirmbooking

import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.payment.PaymentActivity
import kotlinx.android.synthetic.main.activity_confirm_booking.*

class ConfirmBookingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        btnConfirm.setOnClickListener {
            startActivity(Intent(this,PaymentActivity::class.java))
        }
    }
    override fun getLayoutResourceId(): Int =R.layout.activity_confirm_booking
}