package com.applocum.connecttomyhealth.ui.addsymptoms.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.booksession.activities.SessionBookActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_symptom.*
import kotlinx.android.synthetic.main.activity_add_symptom.ivBack
import kotlinx.android.synthetic.main.activity_add_symptom.tvCancel
import kotlinx.android.synthetic.main.custom_profile_dialog.view.*
import java.io.File
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddSymptomActivity : BaseActivity() {
    private var selectedImagePath: String = ""
    private var fileOfPic:File? = null

    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int = R.layout.activity_add_symptom

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)

        val specialistId = intent.getIntExtra("specialistId",0)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish()}

        RxView.clicks(tvCancel).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = (Intent(this, BottomNavigationViewActivity::class.java))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finishAffinity()
                overridePendingTransition(0,0)
            }


        RxView.clicks(ivSymptom).throttleFirst(500,TimeUnit.MILLISECONDS)
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
                            .setActivityMenuIconColor(resources.getColor(R.color.black))
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

        val allDayDr = "<font color='#008976'>alldayDr</font>"
        val nhsGP = "<font color='#008976'>NHS GP.</font>"

        cbGeoLocation.text = HtmlCompat.fromHtml("I allow $allDayDr to access my geo Location. (This will only be used in an emergency)", HtmlCompat.FROM_HTML_MODE_LEGACY)
        cbRecords.text = HtmlCompat.fromHtml("I give consult to alldayDr to share my records with my $nhsGP", HtmlCompat.FROM_HTML_MODE_LEGACY)


        RxView.clicks(btnContinue).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                if (checkCondition(cbGeoLocation.isChecked, cbRecords.isChecked)) {
                    val intent = Intent(this,SessionBookActivity::class.java)
                    intent.putExtra("specialistId", specialistId)
                    val appointment = userHolder.getBookAppointmentData()
                    appointment.appointmentReason = etAddSymptoms.text.toString()
                    appointment.allowGeoAccess = cbGeoLocation.isChecked
                    appointment.sharedRecordWithNhs = cbRecords.isChecked
                    userHolder.saveBookAppointmentData(appointment)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                ivSymptom.setImageURI(result.uri)
                fileOfPic = File(URI(result.uri.toString()))
                selectedImagePath=fileOfPic!!.absolutePath
                val appointment = userHolder.getBookAppointmentData()
                appointment.pickedFilePath = selectedImagePath
                userHolder.saveBookAppointmentData(appointment)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkCondition(geolocation: Boolean, records: Boolean): Boolean {
        if (!geolocation) {
            val snackBar = Snackbar.make(lladdsymptoms, "Please agree to share your geo location", Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
            return false
        }
        if (!records) {
            val snackBar = Snackbar.make(lladdsymptoms,"Please agree to share your records with NHS GP.", Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackview = snackBar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
            return false
        }
        return true
    }
}