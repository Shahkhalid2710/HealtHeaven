package com.applocum.connecttomyhealth.ui.signup

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

class SignupPresenter @Inject constructor(val api: AppEndPoint) {

    val disposables = CompositeDisposable()
    lateinit var view: View
    var devicetype = "android"

    @Inject
    lateinit var userHolder: UserHolder

    fun injectview(view: View) {
        this.view = view
    }

    fun getSignup(
        firstname: String,
        lastname: String,
        email: String,
        mobileno: String,
        password: String,
        confirmPassword: String,
        gender: String,
        date_of_birth: String
    ) {
        if (validateSignup(
                firstname,
                lastname,
                email,
                mobileno,
                password,
                confirmPassword,
                gender,
                date_of_birth
            )
        ) {
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user[email]", email)
                .addFormDataPart("user[first_name]", firstname)
                .addFormDataPart("user[last_name]", lastname)
                .addFormDataPart("user[phone]", mobileno)
                .addFormDataPart("user[gender]", gender)
                .addFormDataPart("user[password]", password)
                .addFormDataPart("profile[date_of_birth]", date_of_birth)
                .addFormDataPart("device_detail[device_type]", devicetype)
                .build()

            api.signUp(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    val userObject = Gson().fromJson(it.data, UserResponse::class.java)
                    val user = userObject.user
                    user.profile.dateOfBirth.let { it1 ->
                        userHolder.saveUser(
                            user.id.toString(),
                            user.firstName,
                            user.lastName,
                            user.email,
                            user.gender,
                            it1.toString()
                        )
                    }
                    view.sendUserData(userObject.user)
                    view.displaymessage(it.message)
                }, onError =
                {

                    view.displaymessage("demo" + it.message)
                }).let { disposables.add(it) }
        }
    }

    fun validateSignup(
        firstname: String,
        lastname: String,
        email: String,
        mobileno: String,
        password: String,
        confirmPassword: String,
        gender: String,
        date_of_birth: String
    ): Boolean {
        val EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        if (firstname.isEmpty()) {
            view.displaymessage("Please Enter First name")
            return false
        }
        if (lastname.isEmpty()) {
            view.displaymessage("Please Enter Last name")
            return false
        }
        if (email.isEmpty()) {
            view.displaymessage("Please Enter Email")
            return false
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            view.displaymessage("Invalid Email")
            return false
        }
        if (mobileno.isEmpty()) {
            view.displaymessage("Please Enter Mobile Number")
            return false
        }
        if (password.isEmpty()) {
            view.displaymessage("Please Enter Password")
            return false
        }
        if (confirmPassword.isEmpty()) {
            view.displaymessage("Please Enter Confirm Password")
            return false
        }
        if (gender.isEmpty()) {
            view.displaymessage("Please Select Gender")
            return false
        }
        if (date_of_birth.isEmpty()) {
            view.displaymessage("Please Select Date of Birth")
            return false
        }
        return true
    }

    interface View {
        fun displaymessage(message: String?)
        fun sendUserData(user: User)
    }
}