package com.applocum.connecttomyhealth.ui.forgotpassword.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.NotFound
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.UnAuthorizedAccess
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.ui.changepassword.models.PasswordGlobalResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.UnknownHostException
import javax.inject.Inject

class ForgotPasswordPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View
    private var role = "patient"

    fun injectView(view: View) {
        this.view = view
    }

    fun forgotPassword(emailMobileNumber: String) {
        if (validatePassword(emailMobileNumber)) {
            view.viewProgress(true)
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", emailMobileNumber)
                .addFormDataPart("role", role)
                .build()

            api.forgetPassword(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewProgress(false)
                    when (it.status) {
                        Success -> {
                            view.displaySuccessMessage(it.message)
                            view.storePassword(it)
                        }
                        InvalidCredentials,InternalServer,NotFound,UnAuthorizedAccess -> {
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

    private fun validatePassword(emailMobileNumber: String): Boolean {
        if (emailMobileNumber.isEmpty()) {
            view.displayMessage("Please enter email/mobile number")
            return false
        }
        return true
    }

    interface View {
        fun displayMessage(message: String)
        fun displaySuccessMessage(message: String)
        fun storePassword(passwordGlobalResponse: PasswordGlobalResponse)
        fun viewProgress(isShow: Boolean)
        fun noInternet(isConnect:Boolean)
    }
}