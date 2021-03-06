package com.applocum.connecttomyhealth.ui.forgotpassword.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.changepassword.models.PasswordGlobalResponse
import com.applocum.connecttomyhealth.ui.forgotpassword.presenters.ForgotPasswordPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ForgotPasswordActivity : BaseActivity(),ForgotPasswordPresenter.View, TextWatcher {
    @Inject
    lateinit var presenter: ForgotPasswordPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_forgot_password

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        etEmailNumber.addTextChangedListener(this)

        RxView.clicks(btnSend).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(llForgotPassword.windowToken, 0)
                presenter.forgotPassword(etEmailNumber.text.toString())
            }
      }

    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(llForgotPassword, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun displaySuccessMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun storePassword(passwordGlobalResponse: PasswordGlobalResponse) {
        this.finish()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            val snackBar = Snackbar.make(llForgotPassword,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
    }

    override fun afterTextChanged(s: Editable?) {
        val result: String = s.toString().replace(" ", "")
        if (s.toString() != result) {
            etEmailNumber.setText(result)
            etEmailNumber.setSelection(result.length)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}