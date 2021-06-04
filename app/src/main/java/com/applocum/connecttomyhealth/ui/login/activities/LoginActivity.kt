package com.applocum.connecttomyhealth.ui.login.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_login.*
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

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        RxView.clicks(tvForgotPasword).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }

        RxView.clicks(tvSignup).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, SignupActivity::class.java))
            }

        RxView.clicks(btnLogin).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.getLogin(etEmail.text.toString(), etPassword.text.toString())
            }
    }

    override fun displaymessage(message: String?) {
        val snackbar = Snackbar.make(llLogin, message.toString(), Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun senduserdata(user: User) {
        val intent = Intent(this, BottomNavigationViewActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}