package com.applocum.connecttomyhealth.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_add_code.*

class AddCodeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        btnAdd.setOnClickListener {
            finish()
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_add_code
}