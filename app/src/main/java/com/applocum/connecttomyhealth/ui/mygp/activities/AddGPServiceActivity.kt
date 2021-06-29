package com.applocum.connecttomyhealth.ui.mygp.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.mygp.adapters.GpServiceAdapter
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.applocum.connecttomyhealth.ui.mygp.models.Surgery
import com.applocum.connecttomyhealth.ui.mygp.presenters.GpservicePresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_g_p_service.*
import kotlinx.android.synthetic.main.activity_add_g_p_service.ivBack
import kotlinx.android.synthetic.main.activity_add_g_p_service.noInternet
import kotlinx.android.synthetic.main.custom_gp_service_dialog.view.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddGPServiceActivity : BaseActivity(), GpservicePresenter.View,
    GpServiceAdapter.ItemClickListner {
    private var isLoading = false

    var mList:ArrayList<GpService> = ArrayList()

    @Inject
    lateinit var presenter: GpservicePresenter

    private lateinit var gpServiceAdapter: GpServiceAdapter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_g_p_service

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        gpServiceAdapter = GpServiceAdapter(this, ArrayList(), this)
        rvAddGp.layoutManager = LinearLayoutManager(this)
        rvAddGp.adapter = gpServiceAdapter

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, GpServiceActivity::class.java))
                finish()
                overridePendingTransition(0, 0)
            }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                noInternet.visibility = View.GONE
                presenter.getgpList(etGpSearch.text.toString())
            }

        RxTextView.textChanges(etGpSearch)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                if (etGpSearch.text.toString().isEmpty()) {
                }
            }
            .observeOn(Schedulers.computation())
            .filter { s -> s.length >= 2 }
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                gpServiceAdapter.mList.remove(null)
                gpServiceAdapter.mList.clear()
                gpServiceAdapter.notifyDataSetChanged()
                presenter.resetPage()
                presenter.getgpList(etGpSearch.text.toString())
            }.subscribe().let { presenter.disposables.add(it) }
    }

    override fun displayMessage(message: String) {}

    override fun getGpList(list: ArrayList<GpService>) {
        mList=list

        if (list.isNullOrEmpty()) {
            rvAddGp.visibility = View.GONE
            NoGpService.visibility = View.VISIBLE
        } else {
            NoGpService.visibility = View.GONE
            rvAddGp.visibility = View.VISIBLE
        }

        gpServiceAdapter.mList.addAll(list)

        gpServiceAdapter.notifyItemRangeInserted(gpServiceAdapter.mList.size, list.size)
        RxRecyclerView.scrollEvents(rvAddGp)
            .subscribe {
                val total = rvAddGp.layoutManager?.itemCount ?: 0
                val last = (rvAddGp.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (total > 0 && total <= last + 2) {
                    if (!isLoading) {
                        presenter.getgpList(etGpSearch.text.toString())
                    }
                }
            }.let { presenter.disposables.add(it) }
    }

    override fun viewFullProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showSurgery(surgery: Surgery) {}

    override fun emptySurgery() {}

    override fun showProgress() {
        isLoading = true
        rvAddGp.post {
            gpServiceAdapter.mList.add(null)
            gpServiceAdapter.notifyItemInserted(gpServiceAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        rvAddGp.post {
            gpServiceAdapter.mList.remove(null)
            gpServiceAdapter.notifyItemRemoved(gpServiceAdapter.mList.size)
        }
    }

    override fun noInternetConnection(isConnect: Boolean) {
        if (!isConnect) {
            rvAddGp.visibility = View.GONE
            noInternet.visibility = View.VISIBLE

            val snackBar = Snackbar.make(llAddGp, R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        } else {
            rvAddGp.visibility = View.VISIBLE
            noInternet.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.safeDispose()
    }

    private fun capitalize(capString: String): String? {
        val capBuffer = StringBuffer()
        val capMatcher: Matcher =
            Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
        while (capMatcher.find()) {
            capMatcher.appendReplacement(
                capBuffer,
                capMatcher.group(1).toUpperCase(Locale.ROOT) + capMatcher.group(2).toLowerCase(
                    Locale.ROOT
                )
            )
        }
        return capMatcher.appendTail(capBuffer).toString()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, GpServiceActivity::class.java))
        super.onBackPressed()
    }


    override fun onItemClick(gpService: GpService, position: Int) {
        val showDialogView = LayoutInflater.from(this@AddGPServiceActivity)
            .inflate(R.layout.custom_gp_service_dialog, null, false)
        val dialog = AlertDialog.Builder(this@AddGPServiceActivity).create()
        dialog.setView(showDialogView)

        showDialogView.tvGpName.text = gpService.practice_name?.let { capitalize(it) }
        showDialogView.tvGpArea.text = gpService.address?.let { capitalize(it) }
        showDialogView.tvGpCity.text = gpService.city?.let { capitalize(it) }

        showDialogView.btnSubmit.setOnClickListener {
            gpService.id?.let { it1 -> presenter.addGpService(it1) }
            val intent = Intent(this@AddGPServiceActivity, GpServiceActivity::class.java)
            startActivity(intent)
            this@AddGPServiceActivity.finish()
            overridePendingTransition(0, 0)
            dialog.dismiss()
        }
        showDialogView.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}