package com.applocum.connecttomyhealth.ui.securitycheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.clinicalrecords.ClinicalRecordsActivity
import com.applocum.connecttomyhealth.ui.securitycheck.models.Security
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_security.*
import kotlinx.android.synthetic.main.custom_progress.*
import javax.inject.Inject

class SecurityActivity : BaseActivity(),SecurityPresenter.View {

    @Inject
    lateinit var presenter: SecurityPresenter


    override fun getLayoutResourceId(): Int =R.layout.activity_security
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)
        ivBack.setOnClickListener { finish() }

        btnDone.setOnClickListener {
            presenter.validateSecurity(etPassword.text.toString())
        }
    }

    override fun security(security: Security) {
        val intent=Intent(this,ClinicalRecordsActivity::class.java)
        startActivity(intent)
        Log.d("clinicalToken","->"+security.token)
        finish()
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