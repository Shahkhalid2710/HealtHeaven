package com.applocum.connecttomyhealth.ui.securitycheck.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.clinicalrecords.activities.ClinicalRecordsActivity
import com.applocum.connecttomyhealth.ui.securitycheck.models.Security
import com.applocum.connecttomyhealth.ui.securitycheck.presenters.SecurityPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_security.*
import kotlinx.android.synthetic.main.activity_security.ivBack
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SecurityActivity : BaseActivity(),
    SecurityPresenter.View {

    @Inject
    lateinit var presenter: SecurityPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_security

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(btnDone).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.validateSecurity(etPassword.text.toString())
            }
    }

    override fun security(security: Security) {
        val intent = Intent(this, ClinicalRecordsActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(0,0)
    }

    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(rlSecurity, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}