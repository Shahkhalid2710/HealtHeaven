package com.applocum.connecttomyhealth.ui.payment

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.AddCardActivity
import com.applocum.connecttomyhealth.ui.verificationdocument.activities.VerifyIdentityActivity
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.custom_booked_succesfully_dialog.*


class PaymentActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tvAddPaymentMethod.setOnClickListener {
            startActivity(Intent(this,PaymentShowActivity::class.java))
        }

        btnConfirmSessionBooking.setOnClickListener {
            startActivity(Intent(this,VerifyIdentityActivity::class.java))

        }

        etAddCode.setOnClickListener {
            startActivity(Intent(this,AddCodeActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int= R.layout.activity_payment
}