package com.applocum.connecttomyhealth.ui.profiledetails.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.profiledetails.models.PatientResponse
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.applocum.connecttomyhealth.ui.signup.models.UserResponse
import com.google.gson.Gson
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProfileDetailsPresenter @Inject constructor(private val api: AppEndPoint) {
    private val disposables = CompositeDisposable()
    lateinit var view: View
    private var heightCategory = "metric"
    private var weightCategory = "imperial"

    @Inject
    lateinit var userHolder: UserHolder

    fun injectview(view: View) {
        this.view = view
    }

    fun showProfile() {
        view.viewprogress(true)
        api.showProfile(userHolder.userToken, userHolder.userid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewprogress(false)
                when (it.status) {
                    Success -> {
                        val patientObject = Gson().fromJson(it.data, PatientResponse::class.java)
                        val patient = patientObject.patient

                        view.showProfile(patient)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            }, onError = {
                view.viewprogress(false)
                it.printStackTrace()
            }).let { disposables.add(it) }
    }

    fun updateProfile(
        firstname: String,
        lastname: String,
        email: String,
        phoneno: String,
        gender: String,
        dob: String,
        heightValue1: String,
        heightValue2: String,
        weightValue1: String,
        weightValue2: String,
        bloodPressure: String
    ) {
        if (validateProfile(
                heightValue1,
                heightValue2,
                weightValue1,
                weightValue2,
                bloodPressure
            )
        ) {
            view.viewprogress(true)
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user[first_name]", firstname)
                .addFormDataPart("user[last_name]", lastname)
                .addFormDataPart("user[email]", email)
                .addFormDataPart("user[phone]", phoneno)
                .addFormDataPart("user[gender]", gender)
                .addFormDataPart("patient[date_of_birth]", dob)
                .addFormDataPart("patient[height_category]", heightCategory)
                .addFormDataPart("patient[height_value1]", heightValue1)
                .addFormDataPart("patient[height_value2]", heightValue2)
                .addFormDataPart("patient[weight_category]", weightCategory)
                .addFormDataPart("patient[weight_value1]", weightValue1)
                .addFormDataPart("patient[weight_value2]", weightValue2)
                .addFormDataPart("patient[blood_pressure]", bloodPressure)
                .build()

            api.updateProfile(userHolder.userToken, userHolder.userid, requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewprogress(false)
                    when (it.status) {
                        Success -> {
                            view.displayMessage("Profile updated successfully")
                            val patientObject =
                                Gson().fromJson(it.data, PatientResponse::class.java)
                            val patient = patientObject.patient
                            view.showProfile(patient)
                        }
                        InvalidCredentials, InternalServer -> {
                            view.displayErrorMessage(it.message)
                        }
                    }
                }, onError = {
                    view.viewprogress(false)
                    it.printStackTrace()
                }).let { disposables.addAll(it) }
        }
    }

    fun updateUser(image:File) {
        view.viewprogress(true)
        val multiPartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val requestBody = RequestBody.create("image/png".toMediaTypeOrNull(), image)
        multiPartBuilder.addFormDataPart("user[image]", image.nameWithoutExtension, requestBody)

        api.updateUser(userHolder.userToken, userHolder.userid, multiPartBuilder.build())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewprogress(false)
                when (it.status) {
                    Success -> {
                        val userObject = Gson().fromJson(it.data, UserResponse::class.java)
                        val user = userObject.user
                        view.userData(user)
                        view.displayMessage("Profile picture uploaded successfully")
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            }, onError = {
                view.viewprogress(false)
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

    private fun validateProfile(
        heightValue1: String,
        heightValue2: String,
        weightValue1: String,
        weightValue2: String,
        bloodPressure: String
    ): Boolean {
        if (heightValue1.isEmpty()) {
            view.displayErrorMessage("Please fill all the credentials")
            return false
        }
        if (heightValue2.isEmpty()) {
            view.displayErrorMessage("Please fill all the credentials")
            return false
        }
        if (weightValue1.isEmpty()) {
            view.displayErrorMessage("Please fill all the credentials")
            return false
        }
        if (weightValue2.isEmpty()) {
            view.displayErrorMessage("Please fill all the credentials")
            return false
        }
        if (bloodPressure.isEmpty()) {
            view.displayErrorMessage("Please fill all the credentials")
            return false
        }
        return true
    }

    interface View {
        fun showProfile(patient: Patient)
        fun displayMessage(message: String)
        fun displayErrorMessage(message: String)
        fun userData(user: User)
        fun viewprogress(isShow: Boolean)
    }
}