package com.applocum.connecttomyhealth.ui.login

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.applocum.connecttomyhealth.ui.signup.models.UserResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.regex.Pattern
import javax.inject.Inject

class LoginPresenter @Inject constructor(private val api: AppEndPoint) {
    private val disposables = CompositeDisposable()
    lateinit var view: View
    private var devicetype = "android"
    private var role = "patient"
    private var corporateId="83"
    private var playerId="temp"

    @Inject
    lateinit var userHolder: UserHolder

    fun injectview(view: View) {
        this.view = view
    }

    fun getLogin(email: String, password: String) {
        if (validateLogin(email, password)) {
            view.viewProgress(true)
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user[email]", email)
                .addFormDataPart("user[password]", password)
                .addFormDataPart("device_detail[device_type]", devicetype)
                .addFormDataPart("device_detail[player_id]", playerId)
                .addFormDataPart("device_detail[corporate_organization_id]",corporateId)
                .addFormDataPart("role", role)
                .build()
            api.signin(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewProgress(false)
                    when (it.status) {
                        Success -> {
                            val userObject: UserResponse = Gson().fromJson(it.data, UserResponse::class.java)
                            val user=userObject.user
                            userHolder.saveUser(user.id.toString(),user.firstName,user.lastName,user.email,user.gender,user.profile.dateOfBirth,user.authToken)
                            view.senduserdata(userObject.user)
                            view.displaySuccessMessage(it.message)
                        }

                        InvalidCredentials, InternalServer -> {
                            view.displaymessage(it.message)
                        }
                    }
                }, onError = {
                    view.viewProgress(false)
                    it.printStackTrace()
                }).let { disposables.add(it) }
        }
    }

    private fun validateLogin(email: String, password: String): Boolean {
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
            view.displaymessage("Please enter email")
            return false
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            view.displaymessage("Invalid email")
            return false
        }
        if (password.isEmpty()) {
            view.displaymessage("Please enter password")
            return false
        }
        return true
    }

    interface View {
        fun displaymessage(message: String?)
        fun displaySuccessMessage(message: String?)
        fun senduserdata(user:User)
        fun viewProgress(isShow: Boolean)
    }
}