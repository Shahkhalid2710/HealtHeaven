package com.applocum.connecttomyhealth.ui.prescription.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.mydownloads.activities.DocumentViewActivity
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import com.applocum.connecttomyhealth.ui.prescription.presenters.DocumentPresenter
import com.applocum.connecttomyhealth.ui.prescription.adapters.PrescriptionAdapter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_prescription.*
import kotlinx.android.synthetic.main.activity_prescription.ivBack
import kotlinx.android.synthetic.main.activity_prescription.noInternet
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PrescriptionActivity : BaseActivity(),
    DocumentPresenter.View, PrescriptionAdapter.PrescriptionClickListner {

    @Inject
    lateinit var presenter: DocumentPresenter

    private var isLoading = false

    lateinit var prescriptionAdapter: PrescriptionAdapter

    override fun getLayoutResourceId(): Int = R.layout.activity_prescription

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        prescriptionAdapter= PrescriptionAdapter(this, ArrayList(),this)
        rvPrescription.layoutManager=LinearLayoutManager(this)
        rvPrescription.adapter=prescriptionAdapter

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.getPrescription()
            }
    }

    override fun onResume() {
        super.onResume()
        presenter.getPrescription()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.safeDispose()
    }

    override fun displayErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getDocument(list: ArrayList<Document>) {
        if (list.isEmpty()) {
            layoutNotFoundPrescription.visibility = View.VISIBLE
            rvPrescription.visibility = View.GONE
        } else {
            layoutNotFoundPrescription.visibility = View.GONE
            rvPrescription.visibility = View.VISIBLE
        }

        prescriptionAdapter.mList.addAll(list)
        prescriptionAdapter.notifyItemRangeInserted(prescriptionAdapter.mList.size, list.size)
        RxRecyclerView.scrollEvents(rvPrescription)
            .subscribe {
                val total = rvPrescription.layoutManager?.itemCount ?: 0
                val last = (rvPrescription.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (total > 0 && total <= last + 2) {
                    if (!isLoading) {
                        presenter.getPrescription()
                    }
                }
            }.let { presenter.disposables.add(it) }

        }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            rvPrescription.visibility=View.GONE
            noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llPrescription,R.string.no_internet, Snackbar.LENGTH_LONG).apply { view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5 }
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
        else{
            noInternet.visibility=View.GONE
            rvPrescription.visibility=View.VISIBLE
        }
    }

    override fun showProgress() {
        isLoading = true
        rvPrescription.post {
            prescriptionAdapter.mList.add(null)
            prescriptionAdapter.notifyItemInserted(prescriptionAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        rvPrescription.post {
            prescriptionAdapter.mList.remove(null)
            prescriptionAdapter.notifyItemRemoved(prescriptionAdapter.mList.size)
        }
    }

    override fun onPrescriptionClick(document: Document, position: Int) {
        val intent = Intent(this@PrescriptionActivity, DocumentViewActivity::class.java)
        intent.putExtra("document", document)
        startActivity(intent)
        overridePendingTransition(0,0)
    }
}