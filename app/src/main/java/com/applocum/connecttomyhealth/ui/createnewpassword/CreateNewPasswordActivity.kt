package com.applocum.connecttomyhealth.ui.createnewpassword

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_create_new_password.*

class CreateNewPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnReset.setOnClickListener {
            showDialog()
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_create_new_password

    fun showDialog() {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)

        val dialogView: View =
            LayoutInflater.from(this)
                .inflate(R.layout.custom_reset_successlly_dialog, viewGroup, false)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()

        val handler = Handler()
        handler.postDelayed(Runnable {
            alertDialog.cancel()
            alertDialog.dismiss()
        }, 2000L)

    }
}