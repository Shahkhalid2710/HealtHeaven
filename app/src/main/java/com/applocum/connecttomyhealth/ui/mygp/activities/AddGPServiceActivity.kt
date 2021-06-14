package com.applocum.connecttomyhealth.ui.mygp.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.mygp.adapters.GpServiceAdapter
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.applocum.connecttomyhealth.ui.mygp.models.Surgery
import com.applocum.connecttomyhealth.ui.mygp.presenters.GpservicePresenter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_g_p_service.*
import kotlinx.android.synthetic.main.activity_add_g_p_service.ivBack
import kotlinx.android.synthetic.main.custom_gp_service_dialog.view.*
import kotlinx.android.synthetic.main.custom_progress.*
import kotlinx.android.synthetic.main.custom_small_progress.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddGPServiceActivity : BaseActivity(), GpservicePresenter.View {

    @Inject
    lateinit var presenter: GpservicePresenter

    private lateinit var gpServiceAdapter: GpServiceAdapter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_g_p_service

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)
        presenter.getgpList("")

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, GpServiceActivity::class.java))
                finish()
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
                presenter.getgpList(etGpSearch.text.toString())
            }.subscribe().let { presenter.disposables.add(it) }

    }

    override fun displayMessage(message: String) {}

    override fun getGpList(list: ArrayList<GpService>) {
        if (list.isEmpty()) {
            NoGpService.visibility = View.VISIBLE
            rvAddGp.visibility = View.GONE
        } else {
            NoGpService.visibility = View.GONE
            rvAddGp.visibility = View.VISIBLE
        }

        gpServiceAdapter =
            GpServiceAdapter(this, list, object :
                    GpServiceAdapter.ItemClickListner {
                    override fun onItemClick(gpService: GpService, position: Int) {
                        val showDialogView = LayoutInflater.from(this@AddGPServiceActivity).inflate(R.layout.custom_gp_service_dialog, null, false)
                        val dialog = AlertDialog.Builder(this@AddGPServiceActivity).create()
                        dialog.setView(showDialogView)

                        showDialogView.tvGpName.text = gpService.practice_name?.let { capitalize(it) }
                        showDialogView.tvGpArea.text = gpService.address?.let { capitalize(it) }
                        showDialogView.tvGpCity.text = gpService.city?.let { capitalize(it) }

                        showDialogView.btnSubmit.setOnClickListener {
                            gpService.id?.let { it1 -> presenter.addGpService(it1) }
                            val intent = Intent(this@AddGPServiceActivity,GpServiceActivity::class.java)
                            startActivity(intent)
                            this@AddGPServiceActivity.finish()
                            dialog.dismiss()
                        }
                        showDialogView.btnCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                })
        rvAddGp.layoutManager = LinearLayoutManager(this)
        rvAddGp.adapter = gpServiceAdapter
    }

    override fun viewProgress(isShow: Boolean) {
        progressSmall.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showSurgery(surgery: Surgery) {}

    private fun capitalize(capString: String): String? {
        val capBuffer = StringBuffer()
        val capMatcher: Matcher =
            Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase(Locale.ROOT) + capMatcher.group(2).toLowerCase(
                Locale.ROOT))
        }
        return capMatcher.appendTail(capBuffer).toString()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, GpServiceActivity::class.java))
        super.onBackPressed()
    }
}