package com.applocum.connecttomyhealth.ui.familyhistory.activities

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
import com.applocum.connecttomyhealth.ui.familyhistory.adapters.FamilyHistoryAdapter
import com.applocum.connecttomyhealth.ui.familyhistory.presenters.FamilyHistoryPresenter
import com.applocum.connecttomyhealth.ui.familyhistory.models.FamilyHistory
import com.applocum.connecttomyhealth.ui.securitycheck.activities.SecurityActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_family_history.*
import kotlinx.android.synthetic.main.activity_family_history.ivBack
import kotlinx.android.synthetic.main.activity_family_history.noInternet
import kotlinx.android.synthetic.main.custom_family_history_xml.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FamilyHistoryActivity : BaseActivity(), FamilyHistoryPresenter.View {

    @Inject
    lateinit var presenter: FamilyHistoryPresenter

    private lateinit var familyHistoryAdapter: FamilyHistoryAdapter

    private var isLoading = false

    var isFamilyHistory="isFamilyHistory"

    override fun getLayoutResourceId(): Int = R.layout.activity_family_history

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        familyHistoryAdapter= FamilyHistoryAdapter(this, ArrayList())
        rvFamilyHistory.layoutManager=LinearLayoutManager(this)
        rvFamilyHistory.adapter=familyHistoryAdapter

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvAddFamilyHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val intent=(Intent(this, AddFamilyHistoryActivity::class.java))
                startActivityForResult(intent,5)
                overridePendingTransition(0,0)
            }

        RxView.clicks(btnAddFamilyHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddFamilyHistoryActivity::class.java))
                startActivityForResult(intent,5)
                overridePendingTransition(0,0)
            }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
               presenter.showFamilyHistoryList()
            }

        familyHistoryAdapter.mList.clear()
        presenter.resetPage()
        presenter.showFamilyHistoryList()
        familyHistoryAdapter.notifyDataSetChanged()
    }

    override fun displayErrorMessage(message: String) {
        val snackBar = Snackbar.make(llFamilyHistory,message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun displaySuccessMessage(message: String) {}

    override fun viewFamilyHistoryProgress(isShow: Boolean) {}

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

        familyHistoryAdapter.mList.addAll(list)
        familyHistoryAdapter.notifyItemRangeInserted(familyHistoryAdapter.mList.size, list.size)
        RxRecyclerView.scrollEvents(rvFamilyHistory)
            .subscribe {
                val total = rvFamilyHistory.layoutManager?.itemCount ?: 0
                val last = (rvFamilyHistory.layoutManager as LinearLayoutManager)
                    .findLastVisibleItemPosition()
                if (total > 0 && total <= last + 2) {
                    if (!isLoading) {
                        presenter.showFamilyHistoryList()
                    }
                }
            }.let { presenter.disposables.add(it) }
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            rvFamilyHistory.visibility=View.GONE
            noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llFamilyHistory,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }else{
            rvFamilyHistory.visibility=View.VISIBLE
            noInternet.visibility=View.GONE
        }
    }

    override fun showProgress() {
        isLoading = true
        rvFamilyHistory.post {
            familyHistoryAdapter.mList.add(null)
            familyHistoryAdapter.notifyItemInserted(familyHistoryAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        rvFamilyHistory.post {
            familyHistoryAdapter.mList.remove(null)
            familyHistoryAdapter.notifyItemRemoved(familyHistoryAdapter.mList.size)
        }
    }

    override fun sessionExpired(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,SecurityActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 5)
        {
            if (resultCode == 5)
            {
                val familyHistory = data?.getBooleanExtra(isFamilyHistory,false)
                if (familyHistory!!)
                {
                    familyHistoryAdapter.mList.clear()
                    presenter.resetPage()
                    presenter.showFamilyHistoryList()
                    familyHistoryAdapter.notifyDataSetChanged()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.safeDispose()
    }
}