package com.applocum.connecttomyhealth.ui.profiledetails.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.profiledetails.presenters.ProfileDetailsPresenter
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.applocum.connecttomyhealth.ui.verification.activities.VerificationActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_profile_details.*
import kotlinx.android.synthetic.main.activity_profile_details.etDOB
import kotlinx.android.synthetic.main.activity_profile_details.etEmail
import kotlinx.android.synthetic.main.activity_profile_details.etFirstName
import kotlinx.android.synthetic.main.activity_profile_details.etGender
import kotlinx.android.synthetic.main.activity_profile_details.etLastName
import kotlinx.android.synthetic.main.activity_profile_details.ivBack
import kotlinx.android.synthetic.main.custom_edit_phone_number_dialog.view.*
import kotlinx.android.synthetic.main.custom_gender_dialog.view.*
import kotlinx.android.synthetic.main.custom_profile_dialog.view.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProfileDetailsActivity : BaseActivity(), ProfileDetailsPresenter.View, DatePickerDialog.OnDateSetListener {
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var myDay: Int = 0
    private var myMonth: Int = 0
    private var myYear: Int = 0
    private var countryCode =" "

    @Inject
    lateinit var presenter: ProfileDetailsPresenter

    @Inject
    lateinit var userHolder: UserHolder

    private lateinit var patientData: Patient

    override fun getLayoutResourceId(): Int = R.layout.activity_profile_details

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvSave).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.updateProfile(
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    etEmail.text.toString(),
                    etPhoneNo.text.toString(),
                    etGender.text.toString().toLowerCase(Locale.ROOT),
                    etDOB.text.toString(),
                    etMeter.text.toString(),
                    etCentimeter.text.toString(),
                    etStone.text.toString(),
                    etLbs.text.toString(),
                    etBMI.text.toString(),
                    etBP.text.toString(),
                    etSmoker.text.toString(),
                    etAlcohol.text.toString(),
                    etPostcode.text.toString(),
                    etAddressLine1.text.toString(),
                    etAddressLine2.text.toString(),
                    etCity.text.toString()
                )
            }

        RxView.clicks(flPic).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val showDialogView =
                    LayoutInflater.from(this).inflate(R.layout.custom_profile_dialog, null, false)
                val dialog = androidx.appcompat.app.AlertDialog.Builder(this).create()
                dialog.setView(showDialogView)

                showDialogView.tvChooseImage.setOnClickListener {
                    CropImage.activity()
                        .setAllowFlipping(false)
                        .setAllowCounterRotation(false)
                        .setBorderLineColor(resources.getColor(R.color.green))
                        .setBorderCornerColor(resources.getColor(R.color.green))
                        .setMinCropResultSize(400,400)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setCropMenuCropButtonIcon(R.drawable.ic_yes)
                        .setRequestedSize(500, 500)
                        .start(this)
                    dialog.dismiss()
                }

                showDialogView.tvCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        editTextClicks()
        presenter.showProfile()
    }

    @SuppressLint("SimpleDateFormat")
    override fun showProfile(patient: Patient) {
        patientData=patient
        tvFName.text = patient.user.firstName
        tvLName.text = patient.user.lastName
        etFirstName.setText(patient.user.firstName)
        etLastName.setText(patient.user.lastName)
        etEmail.setText(patient.user.email)
        etPhoneNo.setText(patient.user.phone)
        etMeter.setText(patient.user.profile.heightValue1)
        etCentimeter.setText(patient.user.profile.heightValue2)
        etStone.setText(patient.user.profile.weightValue1)
        etLbs.setText(patient.user.profile.weightValue2)
        etBP.setText(patient.blood_pressure)

        val date = patient.date_of_birth
        var spf = SimpleDateFormat("yyyy-MM-dd")
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("dd/MM/yyyy")
        val newDateString = spf.format(newDate)
        println(newDateString)
        etDOB.setText(newDateString)

        if (patient.image.isEmpty())
        {
            Glide.with(this).load(R.drawable.ic_blank_profile_pic).into(ivProfile)
            ivPicWarning.visibility=View.VISIBLE
        }
        else {
            Glide.with(this).load(patient.image).into(ivProfile)
            ivPicWarning.visibility = View.GONE
        }

        when(patient.user.gender) {
            "male" -> {
                etGender.setText(R.string.male)
            }
            "female" -> {
                etGender.setText(R.string.female)
            }
            "transgender" -> {
                etGender.setText(R.string.transgender)
            }
            "gender neutral" -> {
                etGender.setText(R.string.gender_neutral)
            }
            "gender fluid" -> {
                etGender.setText(R.string.gender_fluid)
            }
            "prefer not to say" -> {
                etGender.setText(R.string.prefer_not_to_say)
            }
            "other" -> {
                etGender.setText(R.string.other)
            }
        }
        etPostcode.setText(patientData.addresses.primary.pincode)
        etAddressLine1.setText(patientData.addresses.primary.line1)
        etAddressLine2.setText(patientData.addresses.primary.line2)
        etCity.setText(patientData.addresses.primary.town)
        etSmoker.setText(patientData.smoke)
        etAlcohol.setText(patientData.alcohol)
    }

    override fun displayMessage(message: String) {}

    override fun displaySuccessMessage(message: String) {
        val snackbar = Snackbar.make(llProfileDetails, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        snackbar.show()
    }

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(llProfileDetails, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    @SuppressLint("SimpleDateFormat")
    override fun userData(user: User) {
        tvFName.text =user.firstName
        tvLName.text =user.lastName
        etFirstName.setText(user.firstName)
        etLastName.setText(user.lastName)
        etEmail.setText(user.email)
        etPhoneNo.setText(user.phone)
        etMeter.setText(user.profile.heightValue1)
        etCentimeter.setText(user.profile.heightValue2)
        etStone.setText(user.profile.weightValue1)
        etLbs.setText(user.profile.weightValue2)
        val date =user.profile.dateOfBirth
        var spf = SimpleDateFormat("yyyy-MM-dd")
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("dd/MM/yyyy")
        val newDateString = spf.format(newDate)
        println(newDateString)
        etDOB.setText(newDateString)

        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        if (user.image.isEmpty())
        {
            Glide.with(this).load(R.drawable.ic_blank_profile_pic).placeholder(circularProgressDrawable).into(ivProfile)
            ivPicWarning.visibility = View.VISIBLE
        }
        else {
            Glide.with(this).load(user.image).placeholder(circularProgressDrawable).into(ivProfile)
            ivPicWarning.visibility = View.GONE
        }

        when(user.gender) {
            "male" -> {
                etGender.setText(R.string.male)
            }
            "female" -> {
                etGender.setText(R.string.female)
            }
            "transgender" -> {
                etGender.setText(R.string.transgender)
            }
            "gender neutral" -> {
                etGender.setText(R.string.gender_neutral)
            }
            "gender fluid" -> {
                etGender.setText(R.string.gender_fluid)
            }
            "prefer not to say" -> {
                etGender.setText(R.string.prefer_not_to_say)
            }
            "other" -> {
                etGender.setText(R.string.other)
            }
        }

    }

    override fun viewprogress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        presenter.showProfile()
        super.onResume()
    }

    @SuppressLint("CheckResult")
    private fun editTextClicks() {

        RxView.clicks(etGender).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val showDialogView = LayoutInflater.from(this).inflate(R.layout.custom_gender_dialog, null, false)
                val dialog = AlertDialog.Builder(this).create()
                dialog.setView(showDialogView)

                when (etGender.text.toString()) {
                    "Male" -> showDialogView.rbMale.isChecked = true
                    "Female" -> showDialogView.rbFemale.isChecked = true
                    "Transgender" -> showDialogView.rbTransgender.isChecked = true
                    "Gender Neutral" -> showDialogView.rbGenderNeutral.isChecked = true
                    "Gender Fluid" -> showDialogView.rbGenderFluid.isChecked = true
                    "Prefer not to say" -> showDialogView.rbPreferNotToSay.isChecked = true
                    "Other" -> showDialogView.rbOther.isChecked = true
                }

                showDialogView.btnDone.setOnClickListener {
                    var selectedGender = ""
                    when {
                        showDialogView.rbMale.isChecked -> {
                            selectedGender = showDialogView.rbMale.text.toString()
                        }
                        showDialogView.rbFemale.isChecked -> {
                            selectedGender = showDialogView.rbFemale.text.toString()
                        }
                        showDialogView.rbTransgender.isChecked -> {
                            selectedGender = showDialogView.rbTransgender.text.toString()
                        }
                        showDialogView.rbGenderNeutral.isChecked -> {
                            selectedGender = showDialogView.rbGenderNeutral.text.toString()
                        }
                        showDialogView.rbGenderFluid.isChecked -> {
                            selectedGender = showDialogView.rbGenderFluid.text.toString()
                        }
                        showDialogView.rbPreferNotToSay.isChecked -> {
                            selectedGender = showDialogView.rbPreferNotToSay.text.toString()
                        }
                        showDialogView.rbOther.isChecked -> {
                            selectedGender = showDialogView.rbOther.text.toString()
                        }
                    }
                    etGender.setText(selectedGender)
                    dialog.dismiss()
                }
                showDialogView.btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }


        RxView.clicks(etDOB).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val calendar: Calendar = Calendar.getInstance()
                this.day = calendar.get(Calendar.DAY_OF_MONTH)
                this.month = calendar.get(Calendar.MONTH)
                this.year = calendar.get(Calendar.YEAR)

                val datePickerDialog =
                    DatePickerDialog(this, R.style.DialogTheme, this, year, month, day)
                datePickerDialog.datePicker.maxDate = (System.currentTimeMillis() - 568025136000L)
                datePickerDialog.show()
            }

        RxView.clicks(etBP).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectBloodPressure()
            }

        RxView.clicks(etSmoker).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectSmoker()
            }
        RxView.clicks(etAlcohol).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectAlcohol()
            }

        RxView.clicks(etMeter).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectMeter()
            }

        RxView.clicks(etCentimeter).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectCentimeter()
            }

        RxView.clicks(etStone).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectStone()
            }

        RxView.clicks(etLbs).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectPound()
            }

        RxView.clicks(tvPhoneNoEdit).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val showDialogView = LayoutInflater.from(this).inflate(R.layout.custom_edit_phone_number_dialog, null, false)
                val dialog = AlertDialog.Builder(this).create()
                dialog.setView(showDialogView)

                val font = Typeface.createFromAsset(this.assets, "fonts/montserrat_medium.ttf")
                showDialogView.ccp.setTypeFace(font)

                showDialogView.ccp.setOnCountryChangeListener {
                    countryCode = showDialogView.ccp.selectedCountryCode
                }

                showDialogView.etPhoneNumber.setText(patientData.phone_detail?.number)

                showDialogView.btnDonePhoneNumber.setOnClickListener {
                    if (validateMobileNumber(showDialogView.etPhoneNumber.text.toString())) {
                        val intent = Intent(this, VerificationActivity::class.java)
                        intent.putExtra("phoneNumber", showDialogView.etPhoneNumber.text.toString().trim())
                        intent.putExtra("countryCode",countryCode)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                }
                showDialogView.btnCancelPhoneNumber.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myYear = year
        myMonth = month
        myDay = dayOfMonth
        val date = "" + myDay + "/" + (myMonth + 1) + "/" + myYear
        var spf = SimpleDateFormat("d/M/yyyy")
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("dd/MM/yyyy")
        val newDateString = spf.format(newDate)
        println(newDateString)
        etDOB.setText(newDateString)
    }

    private fun selectBloodPressure() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Select option")
        val bloodPressure = resources.getStringArray(R.array.BloodPressure)
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, bloodPressure)
        builder.setAdapter(dataAdapter) { _, which ->
            etBP.setText(bloodPressure[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectSmoker() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Select option")
        val smoker = resources.getStringArray(R.array.Smoker)
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, smoker)
        builder.setAdapter(dataAdapter) { _, which ->
            etSmoker.setText(smoker[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectAlcohol() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Select option")
        val alcohol = resources.getStringArray(R.array.Alcohol)
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, alcohol)
        builder.setAdapter(dataAdapter) { _, which ->
            etAlcohol.setText(alcohol[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectMeter() {
        val meter = resources.getStringArray(R.array.Meter)
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Meter*")
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, meter)
        builder.setAdapter(dataAdapter) { _, which ->
            etMeter.setText(meter[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectCentimeter() {
        val centimeter = ArrayList<String>()
        for (i in 1..51) {
            centimeter.add("$i cm")
        }
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Centimeter*")
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, centimeter)
        builder.setAdapter(dataAdapter) { _, which ->
            etCentimeter.setText(centimeter[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectStone() {
        val centimeter = ArrayList<String>()
        for (i in 6..40) {
            centimeter.add("$i st")
        }
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Stone*")
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, centimeter)
        builder.setAdapter(dataAdapter) { _, which ->
            etStone.setText(centimeter[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectPound() {
        val centimeter = ArrayList<String>()
        for (i in 1..13) {
            centimeter.add("$i lbs")
        }
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Pound*")
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, centimeter)
        builder.setAdapter(dataAdapter) { _, which ->
            etLbs.setText(centimeter[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val fileOfPic = File(URI(result.uri.toString()))
                presenter.updateUser(fileOfPic)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*private fun calculateBMI() {
       val height = etHeight.text.toString()
       val weight = etWeight.text.toString()

       if ("" != height && "" != weight) {
           val heightValue = height.toFloat() / 100
           val weightValue = weight.toFloat()
           val bmi = weightValue / (heightValue * heightValue)
           displayBMI(bmi)
       }
   }*/

/*
    private fun displayBMI(bmi: Float) {
        val bmiLabel = if (bmi.compareTo(15f) <= 0) {
            getString(R.string.very_severely_underweight)
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            getString(R.string.severely_underweight)

        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            getString(R.string.underweight)

        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            getString(R.string.normal)

        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            getString(R.string._overweight)

        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            getString(R.string.obese_class_i)

        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            getString(R.string.obese_class_ii)

        } else {
            getString(R.string.obese_class_iii)
        }
        etBMI.setText("$bmi $bmiLabel")
    }
*/

    private fun validateMobileNumber(mobileNumber:String):Boolean
    {
        if (mobileNumber.isEmpty())
        {
            val snackbar = Snackbar.make(llProfileDetails,"Please enter valid Mobile Number", Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
            return false
        }
        if (mobileNumber.length < 10)
        {
            val snackbar = Snackbar.make(llProfileDetails,"Please enter valid Mobile Number", Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
            return false
        }
        return true
    }
}