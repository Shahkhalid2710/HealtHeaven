package com.applocum.connecttomyhealth.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.payment.adapters.MembershipAdapter
import com.applocum.connecttomyhealth.ui.payment.models.MembershipResponse
import com.applocum.connecttomyhealth.ui.payment.presenters.MembershipPresenter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_member_ship.*
import kotlinx.android.synthetic.main.activity_member_ship.ivBack
import kotlinx.android.synthetic.main.custom_loader_progress.*
import kotlinx.android.synthetic.main.custom_membership.*
import javax.inject.Inject

class MemberShipActivity : BaseActivity(), MembershipPresenter.View {

    private lateinit var membershipAdapter: MembershipAdapter

    @Inject
    lateinit var membershipPresenter: MembershipPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        membershipPresenter.injectView(this)

        ivBack.setOnClickListener { finish() }
        tvAddmembershipcode.setOnClickListener {
            startActivity(Intent(this, AddCodeActivity::class.java))
        }
        btnAddCode.setOnClickListener {
            startActivity(Intent(this, AddCodeActivity::class.java))
        }

        membershipPresenter.showSavedCodes()

    }

    override fun getLayoutResourceId(): Int = R.layout.activity_member_ship

    override fun displayMessage(message: String) {
        val snackbar = Snackbar.make(flMembership, message, Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showMembershipList(membershipResponse: ArrayList<MembershipResponse>) {
        if (membershipResponse.isEmpty()) {
            layoutnotfoundcode.visibility = View.VISIBLE
            llSavedCodes.visibility = View.GONE
            tvAddmembershipcode.visibility = View.GONE
        }
        else
        {
            layoutnotfoundcode.visibility = View.GONE
            llSavedCodes.visibility = View.VISIBLE
            tvAddmembershipcode.visibility = View.VISIBLE
        }

        rvSavedCodes.layoutManager = LinearLayoutManager(this)
        membershipAdapter = MembershipAdapter(
            this,
            membershipResponse,
            false,
            object : MembershipAdapter.CodeClickListener {
                override fun codeClick(membershipResponse: MembershipResponse, position: Int) {

                }
            })
        rvSavedCodes.adapter = membershipAdapter
    }

    override fun onResume() {
        membershipPresenter.showSavedCodes()
        super.onResume()
    }
}