package com.applocum.connecttomyhealth.ui.signup

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.login.LoginActivity
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.google.android.material.snackbar.Snackbar
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
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SignupActivity : BaseActivity(), SignupPresenter.View,
    DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var presenter: SignupPresenter

    private var countrycode = ""
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var myDay: Int = 0
    private var myMonth: Int = 0
    private var myYear: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        btnRegister.setOnClickListener {
            presenter.getSignup(
                etFirstName.text.toString(),
                etLastName.text.toString(),
                etEmail.text.toString(),
                "+$countrycode",countrycode+""+etPhoneNumber.text.toString(),
                etPassword.text.toString(),
                etConfirmPassword.text.toString(),
                etGender.text.toString().toLowerCase(Locale.ROOT),
                etDOB.text.toString()
            )
        }
        clickHandler()
    }

    override fun getLayoutResourceId(): Int = R.layout.activity_signup

    override fun displaymessage(message: String?) {
        val snackbar = Snackbar.make(llSignup, message.toString(), Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessMessage(message: String?) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun sendUserData(user: User) {
        val intent=Intent(this,BottomNavigationViewActivity::class.java)
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

    private fun clickHandler()
    {
        ccp.setOnCountryChangeListener {
            countrycode = ccp.selectedCountryCode
        }
        tvSignin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        etGender.setOnClickListener {
            val showDialogView = LayoutInflater.from(this).inflate(R.layout.custom_gender_dialog, null, false)
            val dialog = AlertDialog.Builder(this).create()
            dialog.setView(showDialogView)

            when(etGender.text.toString())
            {
                "Male" -> showDialogView.rbMale.isChecked = true
                "Female" -> showDialogView.rbFemale.isChecked = true
                "Transgender" -> showDialogView.rbTransgender.isChecked = true
                "Gender Neutral" -> showDialogView.rbGenderNeutral.isChecked = true
                "Gender Fluid" -> showDialogView.rbGenderFluid.isChecked = true
                "Prefer not to say" -> showDialogView.rbPreferNotToSay.isChecked = true
                "Other" -> showDialogView.rbOther.isChecked = true
            }


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