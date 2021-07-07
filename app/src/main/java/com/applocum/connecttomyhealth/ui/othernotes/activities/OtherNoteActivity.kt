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
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_other_note.*
import kotlinx.android.synthetic.main.activity_other_note.ivBack
import kotlinx.android.synthetic.main.activity_other_note.noInternet
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OtherNoteActivity : BaseActivity(), DocumentPresenter.View,
    OtherNoteAdapter.NoteClickListner {

    @Inject
    lateinit var presenter: DocumentPresenter

    private var isLoading = false

    lateinit var otherNoteAdapter: OtherNoteAdapter

    override fun getLayoutResourceId(): Int = R.layout.activity_other_note

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        otherNoteAdapter = OtherNoteAdapter(this, ArrayList(), this)
        rvOtherNotes.layoutManager = LinearLayoutManager(this)
        rvOtherNotes.adapter = otherNoteAdapter

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
        val snackBar = Snackbar.make(llotherNotes,message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun getDocument(list: ArrayList<Document>) {
        if (list.isEmpty()) {
            layoutNotFoundOtherNotes.visibility = View.VISIBLE
            rvOtherNotes.visibility = View.GONE
        } else {
            layoutNotFoundOtherNotes.visibility = View.GONE
            rvOtherNotes.visibility = View.VISIBLE
        }

        otherNoteAdapter.mList.addAll(list)
        otherNoteAdapter.notifyItemRangeInserted(otherNoteAdapter.mList.size, list.size)
        RxRecyclerView.scrollEvents(rvOtherNotes)
            .subscribe {
                val total = rvOtherNotes.layoutManager?.itemCount ?: 0
                val last = (rvOtherNotes.layoutManager as LinearLayoutManager)
                    .findLastVisibleItemPosition()
                if (total > 0 && total <= last + 2) {
                    if (!isLoading) {
                        presenter.getOtherNote()
                    }
                }
            }.let { presenter.disposables.add(it) }

    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect) {
            rvOtherNotes.visibility = View.GONE
            noInternet.visibility = View.VISIBLE

            val snackBar = Snackbar.make(llotherNotes, R.string.no_internet, Snackbar.LENGTH_LONG)
                .apply {
                    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines =
                        5
                }
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        } else {
            noInternet.visibility = View.GONE
            rvOtherNotes.visibility = View.VISIBLE
        }
    }

    override fun showProgress() {
        isLoading = true
        rvOtherNotes.post {
            otherNoteAdapter.mList.add(null)
            otherNoteAdapter.notifyItemInserted(otherNoteAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        rvOtherNotes.post {
            otherNoteAdapter.mList.remove(null)
            otherNoteAdapter.notifyItemRemoved(otherNoteAdapter.mList.size)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.safeDispose()
    }

    override fun onNoteClick(document: Document, position: Int) {
        val intent = Intent(this@OtherNoteActivity, DocumentViewActivity::class.java)
        intent.putExtra("document", document)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}