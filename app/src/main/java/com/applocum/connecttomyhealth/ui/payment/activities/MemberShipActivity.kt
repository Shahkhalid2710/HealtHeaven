package com.applocum.connecttomyhealth.ui.payment.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.payment.adapters.MembershipAdapter
import com.applocum.connecttomyhealth.ui.payment.models.MembershipResponse
import com.applocum.connecttomyhealth.ui.payment.presenters.MembershipPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_member_ship.*
import kotlinx.android.synthetic.main.activity_member_ship.ivBack
import kotlinx.android.synthetic.main.activity_member_ship.noInternet
import kotlinx.android.synthetic.main.custom_loader_progress.*
import kotlinx.android.synthetic.main.custom_membership.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MemberShipActivity : BaseActivity(), MembershipPresenter.View {

    private lateinit var membershipAdapter: MembershipAdapter

    @Inject
    lateinit var membershipPresenter: MembershipPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_member_ship

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        membershipPresenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvAddmembershipcode).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCodeActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(btnAddCode).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddCodeActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                membershipPresenter.showSavedCodes()
            }
    }

    override fun displayMessage(message: String) {
        val snackbar = Snackbar.make(flMembership, message, Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displayErrorMessage(message: String) {
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
        } else {
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

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            rvSavedCodes.visibility=View.GONE
            noInternet.visibility=View.VISIBLE
            layoutnotfoundcode.visibility=View.GONE

            val snackBar = Snackbar.make(llMemberships,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }else{
            rvSavedCodes.visibility=View.VISIBLE
            noInternet.visibility=View.GONE
            layoutnotfoundcode.visibility=View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        membershipPresenter.showSavedCodes()
    }
}