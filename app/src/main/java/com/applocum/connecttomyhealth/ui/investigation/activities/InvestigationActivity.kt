package com.applocum.connecttomyhealth.ui.investigation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.investigation.adapters.InvestigationAdapter
import com.applocum.connecttomyhealth.ui.investigation.presenters.InvestigationPresenter
import com.applocum.connecttomyhealth.ui.investigation.models.Investigation
import com.applocum.connecttomyhealth.ui.securitycheck.activities.SecurityActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_investigation.*
import kotlinx.android.synthetic.main.activity_investigation.ivBack
import kotlinx.android.synthetic.main.activity_investigation.noInternet
import kotlinx.android.synthetic.main.custom_investigation_xml.btnAddInvestigation
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InvestigationActivity : BaseActivity(), InvestigationPresenter.View {

    @Inject
    lateinit var investigationPresenter: InvestigationPresenter

    lateinit var investigationAdapter: InvestigationAdapter

    private var isLoading = false

    override fun getLayoutResourceId(): Int = R.layout.activity_investigation

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        investigationPresenter.injectView(this)

        investigationAdapter= InvestigationAdapter(this, ArrayList())
        rvInvestigation.layoutManager=LinearLayoutManager(this)
        rvInvestigation.adapter=investigationAdapter

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvAddInvestigation).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddInvestigationActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(btnAddInvestigation).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddInvestigationActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                investigationPresenter.showInvestigationList()
            }
    }

    override fun displaySuccessMessage(message: String) {}

    override fun displayMessage(message: String) {}

    override fun viewInvestigationProgress(isShow: Boolean) {}

    override fun investigationList(list: ArrayList<Investigation?>,page:String?) {
        if (list.isEmpty()) {
            layoutNotFoundInvestigation.visibility = View.VISIBLE
            tvAddInvestigation.visibility = View.GONE
            rvInvestigation.visibility = View.GONE
        } else {
            layoutNotFoundInvestigation.visibility = View.GONE
            tvAddInvestigation.visibility = View.VISIBLE
            rvInvestigation.visibility = View.VISIBLE
        }

        investigationAdapter.mList.addAll(list)
        investigationAdapter.notifyItemRangeInserted(investigationAdapter.mList.size,list.size)
        RxRecyclerView.scrollEvents(rvInvestigation)
                 .subscribe {
                     val total = rvInvestigation.layoutManager?.itemCount ?: 0
                     val last = (rvInvestigation.layoutManager as LinearLayoutManager)
                         .findLastVisibleItemPosition()
                     if (total > 0 && total <= last + 2) {
                         if (!isLoading) {
                             investigationPresenter.showInvestigationList()
                         }
                     }
                 }.let { investigationPresenter.disposables.add(it) }
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            rvInvestigation.visibility=View.GONE
            noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llInvestigation,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }else{
            rvInvestigation.visibility=View.VISIBLE
            noInternet.visibility=View.GONE
        }
    }

    override fun showProgress() {
        isLoading = true
        rvInvestigation.post {
            investigationAdapter.mList.add(null)
            investigationAdapter.notifyItemInserted(investigationAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        rvInvestigation.post {
            investigationAdapter.mList.remove(null)
            investigationAdapter.notifyItemRemoved(investigationAdapter.mList.size)
        }
    }

    override fun sessionExpired(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        val intent=Intent(this,SecurityActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        investigationAdapter.mList.clear()
        investigationAdapter.notifyDataSetChanged()
        investigationPresenter.resetPage()
        investigationPresenter.showInvestigationList()
    }

    override fun onDestroy() {
        super.onDestroy()
        investigationPresenter.safeDispose()
    }
}