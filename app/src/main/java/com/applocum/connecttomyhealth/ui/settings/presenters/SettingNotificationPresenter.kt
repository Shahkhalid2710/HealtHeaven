package com.applocum.connecttomyhealth.ui.settings.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.settings.models.SettingNotification
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.UnknownHostException
import javax.inject.Inject

class SettingNotificationPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View

    fun injectView(view: View) {
        this.view = view
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun notificationSetting(
        textMessage: String,
        email: String,
        phone: String,
        pushNotification: String
    ) {
        view.viewFullProgress(true)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("is_notify_by_sms", textMessage)
            .addFormDataPart("is_notify_by_email", email)
            .addFormDataPart("is_notify_by_phone", phone)
            .addFormDataPart("is_notify_by_push_notification", pushNotification)
            .build()

        api.notificationSetting(userHolder.userToken, requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewFullProgress(false)
                when (it.status) {
                    Success -> {
                        view.noInternet(true)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }

            }, onError = {
                view.viewFullProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternet(false)
                }

            }).let { disposables.addAll(it) }
    }


    fun showNotification() {
        view.viewProgress(true)
        api.showNotification(userHolder.userToken)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewProgress(false)
                when (it.status) {
                    Success -> {
                        view.noInternet(true)
                        val notificationObject = Gson().fromJson(it.data, SettingNotification::class.java)
                        view.showNotification(notificationObject)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                view.viewProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternet(false)
                }

            }).let { disposables.add(it) }

    }

    interface View {
        fun displayMessage(message: String)
        fun viewFullProgress(isShow: Boolean)
        fun viewProgress(isShow: Boolean)
        fun showNotification(settingNotification: SettingNotification)
        fun noInternet(isConnect:Boolean)
    }
}