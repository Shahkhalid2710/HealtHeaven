package com.applocum.connecttomyhealth.ui.passwordsecurity.activities

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_password_security.*
import java.util.concurrent.TimeUnit


class PasswordSecurityActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_password_security

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(tvNotNow).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(btnDone).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(btnUseDeviceSecurity).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val keyguardManager=getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                val isSecure =  keyguardManager.isKeyguardSecure

                if (isSecure)
                {
                    val intent = keyguardManager.createConfirmDeviceCredentialIntent(null, null)
                    startActivityForResult(intent, 241)

                }else{
                    val snackBar = Snackbar.make(llPasswordSecurity,"Please setup device security from Device's Setting screen", Snackbar.LENGTH_LONG)
                    snackBar.changeFont()
                    val snackView = snackBar.view
                    snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                    snackBar.show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            241 -> if (resultCode == RESULT_OK) {
                llDeviceSecurity.visibility = View.GONE
                llPasswordReady.visibility = View.VISIBLE
            } else {
                llDeviceSecurity.visibility = View.VISIBLE
                llPasswordReady.visibility = View.GONE
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}