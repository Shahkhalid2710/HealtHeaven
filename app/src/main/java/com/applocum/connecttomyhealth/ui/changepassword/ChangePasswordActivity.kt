package com.applocum.connecttomyhealth.ui.changepassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.changepassword.models.PasswordGlobalResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_change_password.ivBack
import kotlinx.android.synthetic.main.activity_create_new_password.*
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

        //etCurrentPassword.addTextChangedListener(loginTextWatcher)
       // etNewPassword.addTextChangedListener(loginTextWatcher)
        //etConfirmPassword.addTextChangedListener(loginTextWatcher)

        btnUpdate.setOnClickListener {
            presenter.changePassword(etCurrentPassword.text.toString(),etNewPassword.text.toString(),etConfirmPassword.text.toString())
        }

    }

    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(llChangePassword, message, Snackbar.LENGTH_LONG)
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
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

   /* private val loginTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            val cp: String = etCurrentPassword.getText().toString().trim()
            val np: String = etNewPassword.getText().toString().trim()
            val rp: String = etConfirmPassword.getText().toString().trim()
            btnUpdate.isEnabled = cp.isNotEmpty() && np.isNotEmpty() && rp.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable) {}
    }*/
}