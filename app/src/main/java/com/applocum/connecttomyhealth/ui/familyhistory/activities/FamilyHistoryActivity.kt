package com.applocum.connecttomyhealth.ui.familyhistory.activities

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
import com.applocum.connecttomyhealth.ui.familyhistory.adapters.FamilyHistoryAdapter
import com.applocum.connecttomyhealth.ui.familyhistory.presenters.FamilyHistoryPresenter
import com.applocum.connecttomyhealth.ui.familyhistory.models.FamilyHistory
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_family_history.*
import kotlinx.android.synthetic.main.activity_family_history.ivBack
import kotlinx.android.synthetic.main.activity_family_history.noInternet
import kotlinx.android.synthetic.main.custom_family_history_xml.*
import kotlinx.android.synthetic.main.custom_loader_progress.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FamilyHistoryActivity : BaseActivity(), FamilyHistoryPresenter.View {

    @Inject
    lateinit var presenter: FamilyHistoryPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_family_history

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvAddFamilyHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddFamilyHistoryActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(btnAddFamilyHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddFamilyHistoryActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
               presenter.showFamilyHistoryList()
            }
    }

    override fun displayErrorMessage(message: String) {}

    override fun displaySuccessMessage(message: String) {}

    override fun viewFamilyHistoryProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun familyHistoryList(list: ArrayList<FamilyHistory>) {
        if (list.isEmpty()) {
            layoutNotFoundFamilyHistory.visibility = View.VISIBLE
            tvAddFamilyHistory.visibility = View.GONE
            rvFamilyHistory.visibility = View.GONE
        } else {
            layoutNotFoundFamilyHistory.visibility = View.GONE
            tvAddFamilyHistory.visibility = View.VISIBLE
            rvFamilyHistory.visibility = View.VISIBLE
        }
        rvFamilyHistory.layoutManager = LinearLayoutManager(this)
        rvFamilyHistory.adapter =
            FamilyHistoryAdapter(
                this,
                list
            )
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            rvFamilyHistory.visibility=View.GONE
            layoutNotFoundFamilyHistory.visibility=View.GONE
            noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llFamilyHistory,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }else{
            rvFamilyHistory.visibility=View.VISIBLE
            layoutNotFoundFamilyHistory.visibility=View.VISIBLE
            noInternet.visibility=View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.showFamilyHistoryList()
    }
}