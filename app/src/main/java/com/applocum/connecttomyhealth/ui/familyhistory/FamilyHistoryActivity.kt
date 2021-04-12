package com.applocum.connecttomyhealth.ui.familyhistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_family_history.*

class FamilyHistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener {
            finish()
        }
        tvAddFamilyHistory.setOnClickListener {
            startActivity(Intent(this,AddFamilyHistoryActivity::class.java))
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_family_history
}