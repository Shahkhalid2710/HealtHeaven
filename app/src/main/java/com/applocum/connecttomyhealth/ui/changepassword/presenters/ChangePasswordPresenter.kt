package com.applocum.connecttomyhealth.ui.changepassword.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.UnAuthorizedAccess
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.changepassword.models.PasswordGlobalResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.UnknownHostException
import javax.inject.Inject

class ChangePasswordPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View

    @Inject
    lateinit var userHolder: UserHolder

    fun injectView(view: View) {
        this.view = view
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        if (validatePassword(currentPassword, newPassword, confirmPassword)) {
            view.viewProgress(true)
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("existing_password", currentPassword)
                .addFormDataPart("password", newPassword)
                .build()

            api.changePassword(userHolder.userToken!!, requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewProgress(false)
                    when (it.status) {
                        Success -> {
                            view.noInternet(true)
                            view.displaySuccessMessage("Password change successfully!")
                            view.storePassword(it)
                        }
                        InvalidCredentials, InternalServer,UnAuthorizedAccess -> {
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

    private fun validatePassword(currentPassword: String, newPassword: String, confirmPassword: String): Boolean {
        if (currentPassword.isEmpty()) {
            view.displayMessage("Please enter current password")
            return false
        }
        if (newPassword.isEmpty()) {
            view.displayMessage("Please enter new password")
            return false
        }
        if (confirmPassword.isEmpty()) {
            view.displayMessage("Please enter confirm password")
            return false
        }
        if (!confirmPassword.matches(newPassword.toRegex())) {
            view.displayMessage("You must enter the same password twice in order to confirm it")
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