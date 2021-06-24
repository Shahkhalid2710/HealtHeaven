package com.applocum.connecttomyhealth.ui.settings.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.settings.presenters.SettingNotificationPresenter
import com.applocum.connecttomyhealth.ui.settings.models.SettingNotification
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.ivBack
import kotlinx.android.synthetic.main.activity_setting.noInternet
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SettingActivity : BaseActivity(), SettingNotificationPresenter.View {

    @Inject
    lateinit var presenter: SettingNotificationPresenter

    private var switchText = ""
    private var switchEmail = ""
    private var switchphone = ""
    private var switchpuchNotification = ""
    private var Gpsstatus: Boolean = false

    override fun getLayoutResourceId(): Int = R.layout.activity_setting

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.showNotification()
            }

        checkGpsStatus()

        switchlocatinservices.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                turnOffGps()
            } else {
                turnOnGps()
            }
        }

        switchtextmessage.setOnCheckedChangeListener { _, isChecked ->
            switchText = if (isChecked) { "true" } else { "false" }
            presenter.notificationSetting(switchText,switchEmail,switchphone,switchpuchNotification)
        }

        switchemail.setOnCheckedChangeListener { _, isChecked ->
            switchEmail = if (isChecked) { "true" } else { "false" }
            presenter.notificationSetting(switchText, switchEmail, switchphone, switchpuchNotification)
        }

        switchPhone.setOnCheckedChangeListener { _, isChecked ->
            switchphone = if (isChecked) { "true" } else { "false" }
            presenter.notificationSetting(switchText, switchEmail,switchphone, switchpuchNotification)
        }

        switchPuchNotification.setOnCheckedChangeListener { _, isChecked ->
            switchpuchNotification = if (isChecked) { "true" } else { "false" }
            presenter.notificationSetting(switchText,switchEmail, switchphone, switchpuchNotification)
        }
    }
    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(llSetting, message, Snackbar.LENGTH_LONG)
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun viewFullProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showNotification(settingNotification: SettingNotification) {
        switchtextmessage.isChecked = settingNotification.is_notify_by_sms == true
        switchemail.isChecked = settingNotification.is_notify_by_email == true
        switchPhone.isChecked = settingNotification.is_notify_by_phone == true
        switchPuchNotification.isChecked = settingNotification.is_notify_by_push_notification == true
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            llSettings.visibility=View.GONE
            noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llSetting,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()

        }else{
            llSettings.visibility=View.VISIBLE
            noInternet.visibility=View.GONE
        }
    }

    private fun checkGpsStatus() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Gpsstatus = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        switchlocatinservices.isChecked = Gpsstatus == true
    }

    private fun turnOffGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
        builder.setMessage("To continue let your device turn on location.")
            .setCancelable(false)
            .setPositiveButton("OPEN") { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("CANCEL") { dialog, _ -> dialog.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun turnOnGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
        builder.setMessage("To continue let your device turn off location.")
            .setCancelable(false)
            .setPositiveButton("OPEN") { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("CANCEL") { dialog, _ -> dialog.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onResume() {
        checkGpsStatus()
        super.onResume()
        presenter.showNotification()
    }
}