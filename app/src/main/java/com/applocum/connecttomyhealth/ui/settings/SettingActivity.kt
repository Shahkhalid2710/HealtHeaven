package com.applocum.connecttomyhealth.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.settings.models.SettingNotification
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.custom_progress.*
import javax.inject.Inject


class SettingActivity : BaseActivity(),SettingNotificationPresenter.View {

    @Inject
    lateinit var presenter: SettingNotificationPresenter

    override fun getLayoutResourceId(): Int=R.layout.activity_setting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        presenter.showNotification()


        /*switchtextmessage.setOnCheckedChangeListener { _, isChecked ->
            presenter.notificationSetting(isChecked)
        }

        switchemail.setOnCheckedChangeListener { _, isChecked ->
        }

        switchPhone.setOnCheckedChangeListener { _, isChecked ->
        }

        switchPuchNotification.setOnCheckedChangeListener { _, isChecked ->

        }
*/
    }

    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(llSetting, message, Snackbar.LENGTH_LONG)
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun viewFullProgress(isShow: Boolean) {
       progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun showNotification(settingNotification: SettingNotification) {
        switchtextmessage.isChecked = settingNotification.is_notify_by_sms == true
        switchemail.isChecked = settingNotification.is_notify_by_email == true
        switchPhone.isChecked = settingNotification.is_notify_by_phone == true
        switchPuchNotification.isChecked = settingNotification.is_notify_by_push_notification == true
    }
}