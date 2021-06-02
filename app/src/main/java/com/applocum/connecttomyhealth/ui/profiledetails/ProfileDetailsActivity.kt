package com.applocum.connecttomyhealth.ui.profiledetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_profile_details.*
import kotlinx.android.synthetic.main.custom_edit_phone_number_dialog.view.*
import kotlinx.android.synthetic.main.custom_gender_dialog.view.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProfileDetailsActivity : BaseActivity(),ProfileDetailsPresenter.View, DatePickerDialog.OnDateSetListener {
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var myDay: Int = 0
    private var myMonth: Int = 0
    private var myYear: Int = 0

    @Inject
    lateinit var presenter: ProfileDetailsPresenter

    @Inject
    lateinit var userHolder: UserHolder
    override fun getLayoutResourceId(): Int =R.layout.activity_profile_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        (application as MyApplication).component.inject(this)
        presenter.injectview(this)
        presenter.showProfile()

        tvSave.setOnClickListener {
            presenter.updateProfile(etFirstName.text.toString(),etLastName.text.toString(),etEmail.text.toString(),etPhoneNo.text.toString(),etGender.text.toString().toLowerCase(Locale.ROOT),etDOB.text.toString(),etMeter.text.toString(),etCentimeter.text.toString(),etStone.text.toString(),etLbs.text.toString(),etBP.text.toString())
        }
        editTextClicks()

   }

    @SuppressLint("SimpleDateFormat")
    override fun showProfile(patient: Patient) {
        tvFName.text=patient.user.firstName
        tvLName.text=patient.user.lastName
        etFirstName.setText(patient.user.firstName)
        etLastName.setText(patient.user.lastName)
        etEmail.setText(patient.user.email)
        etPhoneNo.setText(patient.user.phone)
        etMeter.setText(patient.user.profile.heightValue1)
        etCentimeter.setText(patient.user.profile.heightValue2)
        etStone.setText(patient.user.profile.weightValue1)
        etLbs.setText(patient.user.profile.weightValue2)
        etBP.setText(patient.blood_pressure)
        val date = patient.user.profile.dateOfBirth
        var spf = SimpleDateFormat("yyyy-MM-dd")
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("dd/MM/yyyy")
        val newDateString = spf.format(newDate)
        println(newDateString)
        etDOB.setText(newDateString)

        Glide.with(this).load(patient.user.image).into(ivProfile)

        when(patient.user.gender)
        {
            "male"->{etGender.setText(R.string.male)}
            "female"->{etGender.setText(R.string.female)}
            "transgender"->{etGender.setText(R.string.transgender)}
            "gender neutral"->{etGender.setText(R.string.gender_neutral)}
            "gender fluid"->{etGender.setText(R.string.gender_fluid)}
            "prefer not to say"->{etGender.setText(R.string.prefer_not_to_say)}
            "other"->{etGender.setText(R.string.other)}
        }
    }

    override fun displayMessage(message: String) {
        /*val snackbar = Snackbar.make(llProfileDetails, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        snackbar.show()*/
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        this.finish()
    }

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(llProfileDetails, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun userData(user: User) {

    }

    override fun viewprogress(isShow: Boolean) {
        progress.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        presenter.showProfile()
        super.onResume()
    }
    private fun editTextClicks()
    {
        etGender.setOnClickListener {
            val showDialogView = LayoutInflater.from(this).inflate(R.layout.custom_gender_dialog, null, false)
            val dialog = AlertDialog.Builder(this).create()
            dialog.setView(showDialogView)

            showDialogView.btnDone.setOnClickListener  {
                var selectedGender=""
                when {
                    showDialogView.rbMale.isChecked -> {
                        selectedGender = showDialogView.rbMale.text.toString()
                    }
                    showDialogView.rbFemale.isChecked -> {
                        selectedGender = showDialogView.rbFemale.text.toString()
                    }
                    showDialogView.rbTransgender.isChecked -> {
                        selectedGender =showDialogView.rbTransgender.text.toString()
                    }
                    showDialogView.rbGenderNeutral.isChecked -> {
                        selectedGender = showDialogView.rbGenderNeutral.text.toString()
                    }
                    showDialogView.rbGenderFluid.isChecked -> {
                        selectedGender =showDialogView.rbGenderFluid.text.toString()
                    }
                    showDialogView.rbPreferNotToSay.isChecked -> {
                        selectedGender = showDialogView.rbPreferNotToSay.text.toString()
                    }
                    showDialogView.rbOther.isChecked -> {
                        selectedGender =showDialogView.rbOther.text.toString()
                    }
                }
                etGender.setText(selectedGender)
                dialog.dismiss()
            }
            showDialogView.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }

        etDOB.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            this.day = calendar.get(Calendar.DAY_OF_MONTH)
            this.month = calendar.get(Calendar.MONTH)
            this.year = calendar.get(Calendar.YEAR)

            val datePickerDialog =
                DatePickerDialog(this, R.style.DialogTheme, this, year, month, day)
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        etBP.setOnClickListener { selectBloodPressure()}
        etMeter.setOnClickListener { selectMeter() }
        etCentimeter.setOnClickListener { selectCentimeter() }
        etStone.setOnClickListener { selectStone() }
        etLbs.setOnClickListener { selectPound() }

        tvPhoneNoEdit.setOnClickListener {
            val showDialogView = LayoutInflater.from(this).inflate(R.layout.custom_edit_phone_number_dialog, null, false)
            val dialog = AlertDialog.Builder(this).create()
            dialog.setView(showDialogView)

            showDialogView.btnDonePhoneNumber.setOnClickListener {

            }
            showDialogView.btnCancelPhoneNumber.setOnClickListener {
                dialog.dismiss()
            }
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myYear = year
        myMonth = month
        myDay = dayOfMonth
        val date = "" + myDay + "/" + (myMonth + 1) + "/" + myYear
        etDOB.setText(date)
    }

    private fun selectBloodPressure()
    {
            val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
            builder.setTitle("Select blood pressure")
            val bloodPressure=resources.getStringArray(R.array.BloodPressure)
            val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,bloodPressure)
            builder.setAdapter(dataAdapter) { _, which ->
                etBP.setText(bloodPressure[which]).toString()
            }
            val dialog = builder.create()
            dialog.show()

    }

    private fun selectMeter()
    {
        val meter=resources.getStringArray(R.array.Meter)
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
        builder.setTitle("Select meter")
        val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,meter)
        builder.setAdapter(dataAdapter) { _, which ->
            etMeter.setText(meter[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectCentimeter()
    {
        val centimeter = ArrayList<String>()
        for (i in 1..51) {
            centimeter.add("$i cm")
        }
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
        builder.setTitle("Select centimeter")
        val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,centimeter)
        builder.setAdapter(dataAdapter) { _, which ->
            etCentimeter.setText(centimeter[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectStone()
    {
        val centimeter = ArrayList<String>()
        for (i in 6..40) {
            centimeter.add("$i st")
        }
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
        builder.setTitle("Select stone")
        val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,centimeter)
        builder.setAdapter(dataAdapter) { _, which ->
            etStone.setText(centimeter[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectPound()
    {
        val centimeter = ArrayList<String>()
        for (i in 1..13) {
            centimeter.add("$i lbs")
        }
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
        builder.setTitle("Select pound")
        val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,centimeter)
        builder.setAdapter(dataAdapter) { _, which ->
            etLbs.setText(centimeter[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
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

}