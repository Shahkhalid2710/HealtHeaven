package com.applocum.connecttomyhealth.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.forgotpassword.ForgotPasswordActivity
import com.applocum.connecttomyhealth.ui.signup.SignupActivity
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.custom_progress.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginPresenter.View {
    @Inject
    lateinit var presenter: LoginPresenter

    @Inject
    lateinit var userHolder: UserHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        tvForgotPasword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        btnLogin.setOnClickListener {
            presenter.getLogin(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    override fun getLayoutResourceId(): Int = R.layout.activity_login

    override fun displaymessage(message: String?) {
        val snackbar = Snackbar.make(llLogin, message.toString(), Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        snackbar.show()
    }

    override fun senduserdata(user: User) {
        val intent = Intent(this,BottomNavigationViewActivity::class.java)
        startActivity(intent)
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}