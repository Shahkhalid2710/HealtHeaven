package com.applocum.connecttomyhealth.ui.createnewpassword.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_create_new_password.*
import java.util.concurrent.TimeUnit

class CreateNewPasswordActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_create_new_password

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(btnReset).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                showDialog()
            }
    }

    private fun showDialog() {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.custom_reset_successlly_dialog, viewGroup, false)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()

        val handler = Handler()
        handler.postDelayed({
            alertDialog.cancel()
            alertDialog.dismiss()
        }, 2000L)
    }
}