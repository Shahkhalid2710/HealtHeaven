package com.applocum.connecttomyhealth.ui.securitycheck.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.UnAuthorizedAccess
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.securitycheck.models.Security
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.UnknownHostException
import javax.inject.Inject

class SecurityPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View

    @Inject
    lateinit var userHolder: UserHolder

    fun injectView(view: View) {
        this.view = view
    }

    fun validateSecurity(password: String) {
        if (validation(password)) {
            view.viewProgress(true)
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("password", password)
                .build()

            api.securityCheck(userHolder.userToken!!, requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewProgress(false)
                    when (it.status) {
                        Success -> {
                            view.noInternet(true)
                            val security = Gson().fromJson(it.data, Security::class.java)
                            userHolder.saveClinicalToken(security.token)
                            view.security(security)
                        }
                        InvalidCredentials, InternalServer, UnAuthorizedAccess -> {
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

                }).let { disposables.addAll(it) }
        }
    }

    private fun validation(password: String): Boolean {
        if (password.isEmpty()) {
            view.displayMessage("Please provide all mandatory details")
            return false
        }
        return true
    }

    interface View {
        fun security(security: Security)
        fun displayMessage(message: String)
        fun viewProgress(isShow: Boolean)
        fun noInternet(isConnect:Boolean)
    }
}