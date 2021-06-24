package com.applocum.connecttomyhealth.ui.fitnote.activities

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
import com.applocum.connecttomyhealth.ui.fitnote.adapters.FitNoteAdapter
import com.applocum.connecttomyhealth.ui.mydownloads.activities.DocumentViewActivity
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import com.applocum.connecttomyhealth.ui.prescription.presenters.DocumentPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_fit_note.*
import kotlinx.android.synthetic.main.activity_fit_note.ivBack
import kotlinx.android.synthetic.main.activity_fit_note.noInternet
import kotlinx.android.synthetic.main.custom_loader_progress.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FitNoteActivity : BaseActivity(),
    DocumentPresenter.View {
    @Inject
    lateinit var presenter: DocumentPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_fit_note

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.getFitNote()
            }
    }

    override fun onResume() {
        super.onResume()
        presenter.getFitNote()
    }

    override fun displayErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getDocument(list: ArrayList<Document>) {
        if (list.isEmpty()) {
            layoutNotFoundFitNote.visibility = View.VISIBLE
            rvFitNote.visibility = View.GONE
        } else {
            layoutNotFoundFitNote.visibility = View.GONE
            rvFitNote.visibility = View.VISIBLE
        }

        rvFitNote.layoutManager = LinearLayoutManager(this)
        rvFitNote.adapter = FitNoteAdapter(this, list, object : FitNoteAdapter.FitNoteClickListner {
            override fun onNoteClick(document: Document, position: Int) {
                val intent = Intent(this@FitNoteActivity, DocumentViewActivity::class.java)
                intent.putExtra("document", document)
                startActivity(intent)
                overridePendingTransition(0,0)
            }
        })
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            rvFitNote.visibility=View.GONE
            noInternet.visibility=View.VISIBLE
            layoutNotFoundFitNote.visibility=View.GONE

            val snackBar = Snackbar.make(llFitNote,R.string.no_internet, Snackbar.LENGTH_LONG).apply { view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5 }
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
        else{
            noInternet.visibility=View.GONE
            rvFitNote.visibility=View.VISIBLE
            layoutNotFoundFitNote.visibility=View.VISIBLE
        }
    }
}