package com.applocum.connecttomyhealth.ui.login.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.forgotpassword.activities.ForgotPasswordActivity
import com.applocum.connecttomyhealth.ui.login.presenters.LoginPresenter
import com.applocum.connecttomyhealth.ui.signup.activities.SignupActivity
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.applocum.connecttomyhealth.ui.verification.activities.VerificationActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etEmail
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_login.tvSignup
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginActivity : BaseActivity(),
    LoginPresenter.View {
    @Inject
    lateinit var presenter: LoginPresenter

    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int = R.layout.activity_login

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        RxView.clicks(tvForgotPasword).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(tvSignup).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,SignupActivity::class.java))
                this.finish()
                overridePendingTransition(0,0)
            }

        RxView.clicks(btnLogin).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(llLogin.windowToken, 0)

                presenter.getLogin(etEmail.text.toString(), etPassword.text.toString())
            }
    }

    override fun displaymessage(message: String?) {
        val snackbar = Snackbar.make(llLogin, message.toString(), Snackbar.LENGTH_LONG).apply { view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5 }
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessMeessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun senduserdata(user: User) {
        if (user.isPhoneVerified)
        {
            userHolder.saveUserPhoto(user.image)
            val intent = Intent(this,BottomNavigationViewActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0,0)
        }else
        {
            userHolder.saveUserPhoto(user.image)
            val intent = Intent(this,VerificationActivity::class.java)
            intent.putExtra("PhoneVerify","LogIn")
            intent.putExtra("user",user)
            startActivity(intent)
            this.finish()
            overridePendingTransition(0,0)
        }
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            val snackbar = Snackbar.make(llLogin,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
        }
    }
}