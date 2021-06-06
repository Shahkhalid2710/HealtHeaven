package com.applocum.connecttomyhealth.ui.photoid.activities

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
import com.applocum.connecttomyhealth.ui.photoid.adapters.PhotoIdAdapter
import com.applocum.connecttomyhealth.ui.photoid.presenters.PhotoIdPresenter
import com.applocum.connecttomyhealth.ui.profiledetails.models.Documents
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.verificationdocument.activities.VerifyIdentityActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_photo_id.*
import kotlinx.android.synthetic.main.custom_loader_progress.*
import kotlinx.android.synthetic.main.custom_remove_document_dialog.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhotoIdActivity : BaseActivity(),PhotoIdPresenter.View {

    @Inject
    lateinit var photoIdPresenter: PhotoIdPresenter

    lateinit var photoIdAdapter: PhotoIdAdapter

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        photoIdPresenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        RxView.clicks(btnUploadPhotoId).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val intent=Intent(this,VerifyIdentityActivity::class.java)
                startActivity(intent)
            }

        RxView.clicks(tvAddPhotoId).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val intent=Intent(this,VerifyIdentityActivity::class.java)
                startActivity(intent)
            }

        photoIdPresenter.showDocument()

    }

    override fun getLayoutResourceId(): Int =R.layout.activity_photo_id
    override fun displayMessage(message: String) {

    }

    override fun displayErrorMessage(message: String) {
    }

    override fun showDocument(patient: Patient) {
    }

    override fun showDocument(list: ArrayList<Documents>) {
        btnUploadPhotoId.isEnabled = list.isEmpty()
        rvPhotoId.layoutManager=LinearLayoutManager(this)
        photoIdAdapter= PhotoIdAdapter(this,list,object :PhotoIdAdapter.DocumentClick{
            override fun deleteDocument(documents: Documents, position: Int) {
                val showDialogView = LayoutInflater.from(this@PhotoIdActivity)
                    .inflate(R.layout.custom_remove_document_dialog, null, false)
                val dialog = AlertDialog.Builder(this@PhotoIdActivity).create()
                dialog.setView(showDialogView)

                showDialogView.btnYes.setOnClickListener {
                    photoIdPresenter.deleteDocument(documents.id)
                    list.removeAt(position)
                    photoIdAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                showDialogView.btnNo.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        })
        rvPhotoId.adapter=photoIdAdapter
        photoIdAdapter.notifyDataSetChanged()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {
        fullProgress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        photoIdPresenter.showDocument()
        super.onResume()
    }
}