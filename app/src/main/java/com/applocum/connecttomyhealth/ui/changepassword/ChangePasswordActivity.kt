package com.applocum.connecttomyhealth.ui.changepassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.changepassword.models.PasswordGlobalResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_change_password.etConfirmPassword
import kotlinx.android.synthetic.main.custom_progress.*
import javax.inject.Inject

class ChangePasswordActivity : BaseActivity(),ChangePasswordPresenter.View {

    @Inject
    lateinit var presenter: ChangePasswordPresenter

    override fun getLayoutResourceId(): Int=R.layout.activity_change_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        ivBack.setOnClickListener { finish() }

        btnUpdate.setOnClickListener {
            presenter.changePassword(etCurrentPassword.text.toString(),etNewPassword.text.toString(),etConfirmPassword.text.toString())
        }

    }

    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(llChangePassword, message, Snackbar.LENGTH_LONG)
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        snackBar.show()
    }

    override fun displaySuccessMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun storePassword(passwordGlobalResponse: PasswordGlobalResponse) {
        this.finish()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }
}