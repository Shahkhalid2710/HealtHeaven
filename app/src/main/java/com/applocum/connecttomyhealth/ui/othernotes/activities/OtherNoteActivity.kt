package com.applocum.connecttomyhealth.ui.othernotes.activities

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
import com.applocum.connecttomyhealth.ui.othernotes.adapters.OtherNoteAdapter
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import com.applocum.connecttomyhealth.ui.prescription.presenters.DocumentPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_other_note.*
import kotlinx.android.synthetic.main.activity_other_note.ivBack
import kotlinx.android.synthetic.main.activity_other_note.noInternet
import kotlinx.android.synthetic.main.custom_loader_progress.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OtherNoteActivity : BaseActivity(), DocumentPresenter.View {

    @Inject
    lateinit var presenter: DocumentPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_other_note

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
                presenter.getOtherNote()
            }
    }

    override fun onResume() {
        super.onResume()
        presenter.getOtherNote()
    }

    override fun displayErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getDocument(list: ArrayList<Document>) {
        if (list.isEmpty()) {
            layoutNotFoundOtherNotes.visibility = View.VISIBLE
            rvOtherNotes.visibility = View.GONE
        } else {
            layoutNotFoundOtherNotes.visibility = View.GONE
            rvOtherNotes.visibility = View.VISIBLE
        }

        rvOtherNotes.layoutManager = LinearLayoutManager(this)
        rvOtherNotes.adapter = OtherNoteAdapter(this, list, object : OtherNoteAdapter.NoteClickListner {
                override fun onNoteClick(document: Document, position: Int) {
                    val intent = Intent(this@OtherNoteActivity,DocumentViewActivity::class.java)
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
            rvOtherNotes.visibility=View.GONE
            noInternet.visibility=View.VISIBLE
            layoutNotFoundOtherNotes.visibility=View.GONE

            val snackBar = Snackbar.make(llotherNotes,R.string.no_internet, Snackbar.LENGTH_LONG).apply { view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5 }
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
        else{
            noInternet.visibility=View.GONE
            rvOtherNotes.visibility=View.VISIBLE
            layoutNotFoundOtherNotes.visibility=View.VISIBLE
        }
    }
}