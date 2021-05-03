package com.applocum.connecttomyhealth.ui.payment

import android.content.Intent
import android.os.Bundle
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_member_ship.*
import kotlinx.android.synthetic.main.custom_membership.*

class MemberShipActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        tvAddMembership.setOnClickListener {
            startActivity(Intent(this,AddCodeActivity::class.java))
        }
        btnAddCode.setOnClickListener {
            startActivity(Intent(this,AddCodeActivity::class.java))
        }

    }

    override fun getLayoutResourceId(): Int =R.layout.activity_member_ship
}