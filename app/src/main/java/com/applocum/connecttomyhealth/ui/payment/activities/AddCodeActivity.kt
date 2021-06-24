package com.applocum.connecttomyhealth.ui.payment.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.payment.models.MembershipResponse
import com.applocum.connecttomyhealth.ui.payment.presenters.MembershipPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_add_code.*
import kotlinx.android.synthetic.main.activity_add_code.ivBack
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddCodeActivity : BaseActivity(),MembershipPresenter.View {

    @Inject
    lateinit var membershipPresenter: MembershipPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_code
    override fun handleInternetConnectivity(isConnect: Boolean?) {

    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        membershipPresenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(btnAdd).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                membershipPresenter.addMembership(etCode.text.toString())
            }
      }

    override fun displayMessage(message: String) {
       this.finish()
    }

    override fun displayErrorMessage(message: String) {
        val snackBar = Snackbar.make(llAddCode, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun showMembershipList(membershipResponse: ArrayList<MembershipResponse>) {}
}