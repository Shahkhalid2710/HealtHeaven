package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.photoid.presenters.PhotoIdPresenter
import com.applocum.connecttomyhealth.ui.profiledetails.models.Documents
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_validation_valid_passport.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.io.File
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ValidationValidPassportActivity : BaseActivity(),PhotoIdPresenter.View {

    @Inject
    lateinit var photoIdPresenter: PhotoIdPresenter

    override fun getLayoutResourceId(): Int =R.layout.activity_validation_valid_passport

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        photoIdPresenter.injectView(this)

        val documentPhoto=intent.getStringExtra("documentPhoto")
        Glide.with(this).load(documentPhoto).into(ivPassport)
        val fileOfPic = File(URI(documentPhoto))


        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        RxView.clicks(btnSubmit).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                photoIdPresenter.uploadDocument(fileOfPic)
            }
    }

    override fun displayMessage(message: String) {
        startActivity(Intent(this@ValidationValidPassportActivity,VerifiedActivity::class.java))
        this.finish()
    }

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(llValidPassport, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun showDocument(patient: Patient) {}

    override fun showDocument(list: ArrayList<Documents>) {}

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewFullProgress(isShow: Boolean) {}
}