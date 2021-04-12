package com.applocum.connecttomyhealth.ui.login

import android.util.Log
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.signup.models.UserResponse
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.regex.Pattern
import javax.inject.Inject

class LoginPresenter @Inject constructor(val api: AppEndPoint) {
    val disposables = CompositeDisposable()
    lateinit var view: View
    var devicetype = "android"
    var role = "patient"

    @Inject
    lateinit var userHolder: UserHolder
    fun injectview(view: View) {
        this.view = view
    }

    fun getLogin(email: String, password: String) {
        if (validateLogin(email, password)) {
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user[email]", email)
                .addFormDataPart("user[password]", password)
                .addFormDataPart("device_detail[device_type]", devicetype)
                .addFormDataPart("role", role)
                .build()
            api.signin(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    val userObject: UserResponse = Gson().fromJson(it.data, UserResponse::class.java)
                    view.displaymessage(it.message)
                   view.senduserdata(userObject.user)
                }, onError = {
                    view.displaymessage("--"+it.message)
                }).let { disposables.add(it) }
        }
    }

    fun validateLogin(email: String, password: String): Boolean {
        val EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        if (email.isEmpty()) {
            view.displaymessage("Please Enter Email")
            return false
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            view.displaymessage("Invalid Email")
            return false
        }
        if (password.isEmpty()) {
            view.displaymessage("Please Enter Password")
            return false
        }
        return true
    }

    interface View {
        fun displaymessage(message: String?)
        fun senduserdata(user: User)
    }
}