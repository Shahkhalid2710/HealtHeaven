package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import kotlinx.android.synthetic.main.activity_verified.*
import kotlinx.android.synthetic.main.custom_booked_succesfully_dialog.*


class VerifiedActivity : BaseActivity() {
    override fun getLayoutResourceId(): Int = R.layout.activity_verified

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnContinue.setOnClickListener {
            openDialog()
        }
    }

    private fun openDialog() {
        val dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_booked_succesfully_dialog)
        val window = dialog.window
        val wlp: WindowManager.LayoutParams = window!!.getAttributes()
        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)

        dialog.btnDone.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, BottomNavigationViewActivity::class.java))
            this.finish()
        }
        dialog.show()
    }
}