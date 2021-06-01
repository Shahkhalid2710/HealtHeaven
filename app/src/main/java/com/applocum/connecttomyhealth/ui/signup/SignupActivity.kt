package com.applocum.connecttomyhealth.ui.signup

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.PopupMenu
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
import kotlinx.android.synthetic.main.custom_progress.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SignupActivity : BaseActivity(), SignupPresenter.View, PopupMenu.OnMenuItemClickListener,
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.male -> etGender.setText(R.string.male)
            R.id.female -> etGender.setText(R.string.female)
            R.id.others -> etGender.setText(R.string.others)
            else -> {
                val snackbar = Snackbar.make(llSignup, "Please Select Gender", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }
        return true
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
    }
}