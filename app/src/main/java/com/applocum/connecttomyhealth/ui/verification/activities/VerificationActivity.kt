package com.applocum.connecttomyhealth.ui.verification.activities

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.verification.presenters.OtpPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.android.synthetic.main.activity_verification.ivBack
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class VerificationActivity : BaseActivity(),OtpPresenter.View {

    @Inject
    lateinit var otpPresenter: OtpPresenter

    override fun getLayoutResourceId(): Int= R.layout.activity_verification

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        otpPresenter.injectView(this)

        val phoneNumber=intent.getStringExtra("phoneNumber")
        val countryCode=intent.getStringExtra("countryCode")

        val mobileNumber= "+$countryCode$phoneNumber"

        val number = "<font color='#008976'>$phoneNumber</font>"

        tvVerificationNumber.text = HtmlCompat.fromHtml("Please enter the verification code we sent to your mobile number $number", HtmlCompat.FROM_HTML_MODE_LEGACY)

        etno1.addTextChangedListener(loginTextWatcher)
        etno2.addTextChangedListener(loginTextWatcher)
        etno3.addTextChangedListener(loginTextWatcher)
        etno4.addTextChangedListener(loginTextWatcher)
        etno5.addTextChangedListener(loginTextWatcher)
        etno6.addTextChangedListener(loginTextWatcher)


        otpPresenter.sendOtp(mobileNumber)

        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        RxView.clicks(tvResend).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                otpPresenter.sendOtp(mobileNumber)
            }


        RxView.clicks(btnVerify).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                otpPresenter.verifyOtp(mobileNumber,etno1.text.toString()+etno2.text.toString()+etno3.text.toString()+etno4.text.toString()+etno5.text.toString()+etno6.text.toString())
            }
    }

    override fun displayMessage(message: String) {
        val snackbar = Snackbar.make(llVerification, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        snackbar.show()
    }

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(llVerification, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    private val loginTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val no1: String = etno1.text.toString().trim()
            val no2: String = etno2.text.toString().trim()
            val no3: String = etno3.text.toString().trim()
            val no4: String = etno4.text.toString().trim()
            val no5: String = etno5.text.toString().trim()
            val no6: String = etno6.text.toString().trim()
            btnVerify.isEnabled = no1.isNotEmpty() && no2.isNotEmpty() && no3.isNotEmpty() && no4.isNotEmpty() && no5.isNotEmpty() && no6.isNotEmpty()
        }
        override fun afterTextChanged(s: Editable) {}
    }
}