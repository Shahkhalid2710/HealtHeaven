package com.applocum.connecttomyhealth.ui.signup.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.login.activities.LoginActivity
import com.applocum.connecttomyhealth.ui.signup.presenters.SignupPresenter
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.etDOB
import kotlinx.android.synthetic.main.activity_signup.etEmail
import kotlinx.android.synthetic.main.activity_signup.etFirstName
import kotlinx.android.synthetic.main.activity_signup.etGender
import kotlinx.android.synthetic.main.activity_signup.etLastName
import kotlinx.android.synthetic.main.custom_gender_dialog.view.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SignupActivity : BaseActivity(), SignupPresenter.View, DatePickerDialog.OnDateSetListener {
    @Inject
    lateinit var presenter: SignupPresenter

    private var countrycode = ""
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var myDay: Int = 0
    private var myMonth: Int = 0
    private var myYear: Int = 0

    override fun getLayoutResourceId(): Int = R.layout.activity_signup

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        countrycode=ccp.selectedCountryCode

        RxView.clicks(btnRegister).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.getSignup(
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    etEmail.text.toString(),
                    "+$countrycode",
                    etPhoneNumber.text.toString(),
                    etPassword.text.toString(),
                    etConfirmPassword.text.toString(),
                    etGender.text.toString().toLowerCase(Locale.ROOT),
                    etDOB.text.toString()
                )
            }

        clickHandler()
        val font = Typeface.createFromAsset(this.assets, "fonts/montserrat_medium.ttf")
        ccp.setTypeFace(font)

    }

    override fun displaymessage(message: String?) {
        val snackbar = Snackbar.make(llSignup, message.toString(), Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun sendUserData(user: User) {
        val intent = Intent(this, BottomNavigationViewActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myYear = year
        myMonth = month
        myDay = dayOfMonth

        val date = "" + myDay + "/" + (myMonth + 1) + "/" + myYear
        var spf = SimpleDateFormat("dd/MM/yy")
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("dd/MM/yyyy")
        val newDateString = spf.format(newDate)
        println(newDateString)
        etDOB.setText(newDateString)
    }

    @SuppressLint("CheckResult")
    private fun clickHandler() {
        ccp.setOnCountryChangeListener {
            countrycode = ccp.selectedCountryCode
        }

        RxView.clicks(tvSignin).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe{
                startActivity(Intent(this, LoginActivity::class.java))
                this.finish()
                overridePendingTransition(0,0)
            }

        etGender.setOnClickListener {
            val showDialogView =
                LayoutInflater.from(this).inflate(R.layout.custom_gender_dialog, null, false)
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
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        etDOB.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            this.day = calendar.get(Calendar.DAY_OF_MONTH)
            this.month = calendar.get(Calendar.MONTH)
            this.year = calendar.get(Calendar.YEAR)

            val datePickerDialog =
                DatePickerDialog(this, R.style.DialogTheme, this, year, month, day)
            datePickerDialog.datePicker.maxDate = (System.currentTimeMillis() - 568025136000L)
            datePickerDialog.show()
        }
    }
}