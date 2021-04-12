package com.applocum.connecttomyhealth.ui.mydownloads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_my_downloads.*

class MyDownloadsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener {
            finish()
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_my_downloads
}