package com.applocum.connecttomyhealth.ui.payment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_add_code.*
import kotlinx.android.synthetic.main.activity_add_code.ivBack
import java.util.concurrent.TimeUnit

class AddCodeActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe{
                finish()
            }

        RxView.clicks(btnAdd).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe{
                finish()
            }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_add_code
}