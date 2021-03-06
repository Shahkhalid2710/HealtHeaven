package com.applocum.connecttomyhealth.ui.securitycheck.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
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

class SecurityActivity : BaseActivity(), SecurityPresenter.View {

    @Inject
    lateinit var presenter: SecurityPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_security

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = Intent(this, BottomNavigationViewActivity::class.java)
                intent.putExtra("isAppointmentBook","security")
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finishAffinity()
                overridePendingTransition(0,0)
            }

        RxView.clicks(btnDone).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(rlSecurity.windowToken, 0)
                presenter.validateSecurity(etPassword.text.toString())
            }
    }

    override fun security(security: Security) {
        val intent = Intent(this,ClinicalRecordsActivity::class.java)
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

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect) {
            val snackBar = Snackbar.make(rlSecurity, R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, BottomNavigationViewActivity::class.java)
        intent.putExtra("isAppointmentBook","security")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(0,0)
    }
}