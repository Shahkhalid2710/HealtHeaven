package com.applocum.connecttomyhealth.ui.verification.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.applocum.connecttomyhealth.ui.verification.presenters.OtpPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.android.synthetic.main.activity_verification.ivBack
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class VerificationActivity : BaseActivity(),OtpPresenter.View {

    @Inject
    lateinit var otpPresenter: OtpPresenter

     var data:String?=null
     lateinit var user: User
    var mobileNumber:String?=null
    var number:String?=null

    override fun getLayoutResourceId(): Int= R.layout.activity_verification

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        otpPresenter.injectView(this)

        if (intent.hasExtra("PhoneVerify") )
        {
             data=intent.getStringExtra("PhoneVerify")
            val phoneNumber=intent.getStringExtra("phoneNumber")
            val countryCode=intent.getStringExtra("countryCode")
            mobileNumber="+$countryCode$phoneNumber"
            number = "<font color='#008976'>$mobileNumber</font>"
        }

        if( intent.hasExtra("user"))
        {
            user=intent.getSerializableExtra("user") as User
            number = "<font color='#008976'>${user.phone}</font>"
            mobileNumber=user.phone
        }

        tvVerificationNumber.text = HtmlCompat.fromHtml("Please enter the verification code we sent to your mobile number $number", HtmlCompat.FROM_HTML_MODE_LEGACY)

        etno1.addTextChangedListener(loginTextWatcher)
        etno2.addTextChangedListener(loginTextWatcher)
        etno3.addTextChangedListener(loginTextWatcher)
        etno4.addTextChangedListener(loginTextWatcher)
        etno5.addTextChangedListener(loginTextWatcher)
        etno6.addTextChangedListener(loginTextWatcher)

        mobileNumber?.let { otpPresenter.sendOtp(it) }

        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        RxView.clicks(tvResend).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                mobileNumber?.let { it1 -> otpPresenter.sendOtp(it1) }
            }


        RxView.clicks(btnVerify).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(llVerification.windowToken, 0)
                mobileNumber?.let { it1 -> otpPresenter.verifyOtp(it1,etno1.text.toString()+etno2.text.toString()+etno3.text.toString()+etno4.text.toString()+etno5.text.toString()+etno6.text.toString()) }
            }

        etno1.addTextChangedListener(GenericTextWatcher(etno1, etno2))
        etno2.addTextChangedListener(GenericTextWatcher(etno2, etno3))
        etno3.addTextChangedListener(GenericTextWatcher(etno3, etno4))
        etno4.addTextChangedListener(GenericTextWatcher(etno4, etno5))
        etno5.addTextChangedListener(GenericTextWatcher(etno5, etno6))
        etno6.addTextChangedListener(GenericTextWatcher(etno6, null))

        etno1.setOnKeyListener(GenericKeyEvent(etno1, null))
        etno2.setOnKeyListener(GenericKeyEvent(etno2, etno1))
        etno3.setOnKeyListener(GenericKeyEvent(etno3, etno2))
        etno4.setOnKeyListener(GenericKeyEvent(etno4,etno3))
        etno5.setOnKeyListener(GenericKeyEvent(etno5,etno4))
        etno6.setOnKeyListener(GenericKeyEvent(etno6,etno5))

    }

    class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.etno1 && currentView.text.isEmpty()) {
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }
    }

    class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) : TextWatcher {
        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            when (currentView.id) {
                R.id.etno1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.etno2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.etno3 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.etno4 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.etno5 -> if (text.length == 1) nextView!!.requestFocus()
            }
        }

        override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}

        override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
    }
    override fun displayMessage(message: String) {
        val snackbar = Snackbar.make(llVerification, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        snackbar.show()
    }

    override fun userData(user: User) {
         when(data)
         {
             "SignUp"->{
                 val intent=Intent(this,BottomNavigationViewActivity::class.java)
                 intent.putExtra("user",user)
                 startActivity(intent)
                 finish()
             }
             "ProfileDetails"->{
                 finish()
             }

             "LogIn"->{
                 val intent=Intent(this,BottomNavigationViewActivity::class.java)
                 intent.putExtra("user",user)
                 startActivity(intent)
                 finish()
             }
         }
    }

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(llVerification, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            val snackbar = Snackbar.make(llVerification,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
        }
    }

    private val loginTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val no1: String = etno1.text.toString().trim()
            val no2: String = etno2.text.toString().trim()
            val no3: String = etno3.text.toString().trim()
            val no4: String = etno4.text.toString().trim()
            val no5: String = etno5.text.toString().trim()
            val no6: String = etno6.text.toString().trim()
            btnVerify.isEnabled = no1.isNotEmpty() && no2.isNotEmpty() && no3.isNotEmpty() && no4.isNotEmpty() && no5.isNotEmpty() && no6.isNotEmpty()
        }
        override fun afterTextChanged(s: Editable) {}
    }
}