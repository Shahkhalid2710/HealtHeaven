package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_validation_valid_passport.*
import kotlinx.android.synthetic.main.custom_profile_dialog.view.*
import kotlinx.android.synthetic.main.custom_progress.progress
import java.io.File
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ValidationValidPassportActivity : BaseActivity(),PhotoIdPresenter.View {

    @Inject
    lateinit var photoIdPresenter: PhotoIdPresenter

    private lateinit var fileOfPic:File

    override fun getLayoutResourceId(): Int =R.layout.activity_validation_valid_passport
    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        photoIdPresenter.injectView(this)

        val documentPhoto=intent.getStringExtra("documentPhoto")
        val name=intent.getStringExtra("name")

        tvDescription.text=("Your safety is our priority. To prevent misuse of your details, upload a valid form of $name")

        tvName.text = name
        Glide.with(this).load(documentPhoto).into(ivPassport)
        fileOfPic = File(URI(documentPhoto))

        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        RxView.clicks(btnSubmit).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                when(name)
                {
                    "Valid Passport"->{photoIdPresenter.uploadDocument(fileOfPic)}
                    "UK Driving License"->{photoIdPresenter.uploadLicenseDocument(fileOfPic)}
                }
            }

        RxView.clicks(btnRetakePhoto).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val showDialogView = LayoutInflater.from(this)
                    .inflate(R.layout.custom_profile_dialog, null, false)
                val dialog = androidx.appcompat.app.AlertDialog.Builder(this).create()
                dialog.setView(showDialogView)

                showDialogView.tvChooseImage.setOnClickListener {
                    this.let {
                        CropImage.activity()
                            .setAllowFlipping(false)
                            .setAllowCounterRotation(false)
                            .setBorderLineColor(resources.getColor(R.color.green))
                            .setBorderCornerColor(resources.getColor(R.color.green))
                            .setMinCropResultSize(400,400)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setCropMenuCropButtonIcon(R.drawable.ic_yes)
                            .setRequestedSize(500, 500)
                            .start(this)
                    }
                    dialog.dismiss()
                }

                showDialogView.tvCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
    }

    override fun displayMessage(message: String) {
        startActivity(Intent(this@ValidationValidPassportActivity,VerifiedActivity::class.java))
        this.finish()
        overridePendingTransition(0,0)
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

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            val snackbar = Snackbar.make(llValidPassport, R.string.no_internet, Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                ivPassport.setImageURI(result.uri)
                 fileOfPic = File(URI(result.uri.toString()))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}