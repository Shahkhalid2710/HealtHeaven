package com.applocum.connecttomyhealth.ui.payment.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.verificationdocument.activities.VerifyIdentityActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_payment.*
import java.util.concurrent.TimeUnit

class PaymentActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_payment
    override fun handleInternetConnectivity(isConnect: Boolean?) {

    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        RxView.clicks(tvAddPaymentMethod).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, PaymentShowActivity::class.java))
            }

        RxView.clicks(btnConfirmSessionBooking).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, VerifyIdentityActivity::class.java))
            }

        RxView.clicks(etAddCode).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCodeActivity::class.java))
            }
    }
}