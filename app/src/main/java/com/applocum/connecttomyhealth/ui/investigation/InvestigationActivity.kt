package com.applocum.connecttomyhealth.ui.investigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.investigation.models.Investigation
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_investigation.*
import kotlinx.android.synthetic.main.activity_investigation.ivBack
import kotlinx.android.synthetic.main.custom_investigation_xml.*
import kotlinx.android.synthetic.main.custom_loader_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InvestigationActivity : BaseActivity(),InvestigationPresenter.View {

    @Inject
    lateinit var investigationPresenter: InvestigationPresenter

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        investigationPresenter.injectView(this)


        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                   finish()
            }

        RxView.clicks(tvAddInvestigation).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,AddInvestigationActivity::class.java))
            }

        RxView.clicks(btnAddInvestigation).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,AddInvestigationActivity::class.java))
            }

        investigationPresenter.showInvestigationList()
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_investigation
    override fun displaySuccessMessage(message: String) {
    }

    override fun displayMessage(message: String) {
    }

    override fun viewInvestigationProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun investigationList(list: ArrayList
    <Investigation>) {
        if (list.isEmpty())
        {
            layoutNotFoundInvestigation.visibility=View.VISIBLE
            tvAddInvestigation.visibility=View.GONE
            rvInvestigation.visibility=View.GONE
        }
        else
        {
            layoutNotFoundInvestigation.visibility=View.GONE
            tvAddInvestigation.visibility=View.VISIBLE
            rvInvestigation.visibility=View.VISIBLE
        }
        rvInvestigation.layoutManager=LinearLayoutManager(this)
        rvInvestigation.adapter=InvestigationAdapter(this,list)
    }

    override fun onResume() {
        investigationPresenter.showInvestigationList()
        super.onResume()
    }
}