package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_valid_passport.*

class ValidPassportActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
    }

    override fun getLayoutResourceId(): Int=R.layout.activity_valid_passport
}