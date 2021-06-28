package com.applocum.connecttomyhealth.ui.help.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.chat.activities.ChatActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_help.*
import java.util.concurrent.TimeUnit


class HelpActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int= R.layout.activity_help

    override fun handleInternetConnectivity(isConnect: Boolean?) {}
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

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "message/rfc822"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("healtheaven@gmail.com"))
                intent.setPackage("com.google.android.gm")
                if
                        (intent.resolveActivity(packageManager) != null) startActivity(intent)
                else
                    Toast.makeText(this, "Gmail App is not installed", Toast.LENGTH_SHORT).show()
            }
    }
}