package com.applocum.connecttomyhealth.ui.profiledetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.PopupMenu
import android.widget.Toast
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_profile_details.*
import kotlinx.android.synthetic.main.activity_profile_details.ivBack
import kotlinx.android.synthetic.main.custom_progress.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProfileDetailsActivity : BaseActivity(),ProfileDetailsPresenter.View,
    PopupMenu.OnMenuItemClickListener, DatePickerDialog.OnDateSetListener {
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
            presenter.updateProfile(etFirstName.text.toString(),etLastName.text.toString(),etEmail.text.toString(),etPhoneNo.text.toString(),etGender.text.toString().toLowerCase(Locale.ROOT),etDOB.text.toString())
            finish()
        }
        editTextClicks()
        etWeight.setOnClickListener {
            selectWeight()
            calculateBMI()
        }
        etHeight.setOnClickListener {
            selectHeight()
            calculateBMI()
        }

    }

    @SuppressLint("SimpleDateFormat")
    override fun showProfile(patient: Patient) {
        tvFName.text=patient.first_name
        tvLName.text=patient.last_name
        etFirstName.setText(patient.first_name)
        etLastName.setText(patient.last_name)
        etEmail.setText(patient.email)
        etPhoneNo.setText(patient.phone)
        etGender.setText(patient.gender)

        val date = patient.date_of_birth
        var spf = SimpleDateFormat("yyyy-MM-dd")
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("dd/MM/yyyy")
        val newDateString = spf.format(newDate)
        println(newDateString)
        etDOB.setText(newDateString)
    }

    override fun displayMessage(message: String) {
      Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    override fun userData(user: User) {

    }

    override fun viewprogress(isShow: Boolean) {
        progress.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    private fun editTextClicks()
    {
        etGender.setOnClickListener {
            val ctw = ContextThemeWrapper(this, R.style.CustomPopupTheme)
            val popupMenu = PopupMenu(ctw, etGender)
            popupMenu.menuInflater.inflate(R.menu.menu_gender, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
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
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.male -> etGender.setText(R.string.male)
            R.id.female -> etGender.setText(R.string.female)
            R.id.others -> etGender.setText(R.string.others)
            else -> {
                val snackbar = Snackbar.make(llProfileDetails, "Please Select Gender", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }
        return true
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



    private fun selectHeight()
    {
        val centiMeter = ArrayList<String>()
        for (i in 99..200) {
            centiMeter.add("$i")
        }

        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
        builder.setTitle("Select height")
        val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,centiMeter)
        builder.setAdapter(dataAdapter) { _, which ->
            etHeight.setText(centiMeter[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectWeight()
    {
        val weight = ArrayList<String>()
        for (i in 40..120) {
            weight.add("$i")
        }

        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
        builder.setTitle("Select weight")
        val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,weight)
        builder.setAdapter(dataAdapter) { _, which ->
            etWeight.setText(weight[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun calculateBMI() {
        val height = etHeight.text.toString()
        val weight = etWeight.text.toString()

        if ("" != height && "" != weight) {
            val heightValue = height.toFloat() / 100
            val weightValue = weight.toFloat()
            val bmi = weightValue / (heightValue * heightValue)
            displayBMI(bmi)
        }
    }

    @SuppressLint("SetTextI18n")
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

}