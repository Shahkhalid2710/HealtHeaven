package com.applocum.connecttomyhealth.ui.help.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.chat.activities.ChatActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_help.*
import java.util.concurrent.TimeUnit


class HelpActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int= R.layout.activity_help
    override fun handleInternetConnectivity(isConnect: Boolean?) {

    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(llChat).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,ChatActivity::class.java))
            }

        RxView.clicks(llEmail).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
               /* val km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (km.isKeyguardSecure) {
                    val authIntent = km.createConfirmDeviceCredentialIntent(
                        getString(R.string.done),
                        getString(R.string.cancel)
                    )
                    startActivityForResult(authIntent, INTENT_AUTHENTICATE)
                }*/
            }
    }
}