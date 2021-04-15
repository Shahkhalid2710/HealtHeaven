package com.applocum.connecttomyhealth.ui.payment

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addcard.AddCardActivity
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.custom_booked_succesfully_dialog.*


class PaymentActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tvAddPaymentMethod.setOnClickListener {
            startActivity(Intent(this,AddCardActivity::class.java))
        }

        btnConfirmSessionBooking.setOnClickListener {
           openDialog()
        }

        etAddCode.setOnClickListener {
            startActivity(Intent(this,AddCodeActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int= R.layout.activity_payment

    private fun openDialog()
    {
        val dialog = Dialog(this,android.R.style.Theme_Translucent_NoTitleBar)
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
            /*val apointmentFragment= AppointmentFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.llPayment, apointmentFragment)
            transaction.addToBackStack(null)
            transaction.commit()*/
        }
        dialog.show()
    }
}