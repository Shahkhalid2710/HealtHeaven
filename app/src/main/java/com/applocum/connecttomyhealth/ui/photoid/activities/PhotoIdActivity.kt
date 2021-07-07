package com.applocum.connecttomyhealth.ui.photoid.activities

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
import com.applocum.connecttomyhealth.ui.photoid.adapters.PhotoIdAdapter
import com.applocum.connecttomyhealth.ui.photoid.presenters.PhotoIdPresenter
import com.applocum.connecttomyhealth.ui.profiledetails.models.Documents
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.verificationdocument.activities.VerifyIdentityActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_photo_id.*
import kotlinx.android.synthetic.main.activity_photo_id.ivBack
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.custom_remove_document_dialog.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhotoIdActivity : BaseActivity(), PhotoIdPresenter.View {

    @Inject
    lateinit var photoIdPresenter: PhotoIdPresenter

    lateinit var photoIdAdapter: PhotoIdAdapter

    var photoPosition = 0
    var mListPhotoId: ArrayList<Documents> = ArrayList()

    override fun getLayoutResourceId(): Int = R.layout.activity_photo_id

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        photoIdPresenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(btnUploadPhotoId).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = Intent(this, VerifyIdentityActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

        RxView.clicks(tvAddPhotoId).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = Intent(this, VerifyIdentityActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                photoIdPresenter.showDocument()
            }
    }

    override fun displayMessage(message: String) {
        mListPhotoId.removeAt(photoPosition)
        mListPhotoId.trimToSize()
        photoIdAdapter.notifyItemRemoved(photoPosition)
        checkList()
    }

    override fun displayErrorMessage(message: String) {
        val snackBar = Snackbar.make(llDocuments,message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun showDocument(patient: Patient) {}

    override fun showDocument(list: ArrayList<Documents>) {
        mListPhotoId = list
        checkList()

        rvPhotoId.layoutManager = LinearLayoutManager(this)
        photoIdAdapter = PhotoIdAdapter(this, list, object : PhotoIdAdapter.DocumentClick {
            override fun deleteDocument(documents: Documents, position: Int) {
                val showDialogView = LayoutInflater.from(this@PhotoIdActivity)
                    .inflate(R.layout.custom_remove_document_dialog, null, false)
                val dialog = AlertDialog.Builder(this@PhotoIdActivity).create()
                dialog.setView(showDialogView)

                photoPosition = position

                showDialogView.btnYes.setOnClickListener {
                    photoIdPresenter.deleteDocument(documents.id)
                    dialog.dismiss()
                }
                showDialogView.btnNo.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        })
        rvPhotoId.adapter = photoIdAdapter
        photoIdAdapter.notifyDataSetChanged()
    }

    override fun viewProgress(isShow: Boolean) {
        fullProgress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {
        fullProgress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect) {
            llPhotoId.visibility = View.GONE
            noInternet.visibility = View.VISIBLE

            val snackBar = Snackbar.make(llDocuments,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()

        } else {
            llPhotoId.visibility = View.VISIBLE
            noInternet.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        photoIdPresenter.showDocument()
    }

    private fun checkList() {
        if (mListPhotoId.isEmpty()) {
            btnUploadPhotoId.visibility = View.VISIBLE
            tvAddPhotoId.visibility = View.GONE
            NoPhotoId.visibility = View.VISIBLE
        } else {
            btnUploadPhotoId.visibility = View.GONE
            tvAddPhotoId.visibility = View.VISIBLE
            NoPhotoId.visibility = View.GONE
        }
    }
}