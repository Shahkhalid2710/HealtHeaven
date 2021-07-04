package com.applocum.connecttomyhealth.ui.profiledetails.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.UnAuthorizedAccess
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
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.net.UnknownHostException
import javax.inject.Inject

class ProfileDetailsPresenter @Inject constructor(private val api: AppEndPoint) {
    private val disposables = CompositeDisposable()
    lateinit var view: View
    private var heightCategory = "metric"
    private var weightCategory = "imperial"
    private var heightValue2 = "null"
    private var weightValue2 = "null"

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
                        view.noInternetConnection(true)
                        val patientObject = Gson().fromJson(it.data, PatientResponse::class.java)
                        val patient = patientObject.patient
                        view.showProfile(patient)
                    }
                    InvalidCredentials, InternalServer, UnAuthorizedAccess -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            }, onError = {
                view.viewprogress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternetConnection(false)
                }

            }).let { disposables.add(it) }
    }

    fun updateProfile(firstname: String, lastname: String, email: String, phoneno: String, gender:String, dob: String, heightValue1: String, weightValue1: String,bmi:String, bloodPressure: String,smoker:String,alcohol:String,postcode:String,addressline1:String,addressline2: String,city:String) {
        if (validateProfile(heightValue1,weightValue1,bmi,bloodPressure,alcohol,smoker,postcode,addressline1, addressline2, city))
        {
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
                .addFormDataPart("patient[bmi]",bmi)
                .addFormDataPart("patient[alcohol]",alcohol)
                .addFormDataPart("patient[smoke]",smoker)
                .addFormDataPart("address[pincode]",postcode)
                .addFormDataPart("address[line1]",addressline1)
                .addFormDataPart("address[line2]",addressline2)
                .addFormDataPart("address[town]",city)
                .addFormDataPart("patient[blood_pressure]",bloodPressure)
                .build()

            api.updateProfile(userHolder.userToken,userHolder.userid, requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewprogress(false)
                    when (it.status) {
                        Success -> {
                            view.noInternetConnection(true)
                            view.displaySuccessMessage("Profile updated successfully")
                            val userObject = Gson().fromJson(it.data,UserResponse::class.java)
                            val user = userObject.user
                            view.userData(user)
                        }
                        InvalidCredentials, InternalServer,UnAuthorizedAccess -> {
                            view.displayErrorMessage(it.message)
                        }
                    }
                }, onError = {
                    view.viewprogress(false)
                    it.printStackTrace()

                    if (it is UnknownHostException)
                    {
                        view.noInternetConnection(false)
                    }

                }).let { disposables.addAll(it) }
        }
    }

    fun updateUser(image:File) {
        view.viewprogress(true)
        val multiPartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val requestBody = image.asRequestBody("image/png".toMediaTypeOrNull())
        multiPartBuilder.addFormDataPart("user[image]", image.nameWithoutExtension, requestBody)

        api.updateUser(userHolder.userToken, userHolder.userid, multiPartBuilder.build())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewprogress(false)
                when (it.status) {
                    Success -> {
                        view.noInternetConnection(true)
                        val userObject = Gson().fromJson(it.data, UserResponse::class.java)
                        val user = userObject.user
                        view.userData(user)
                        view.displayMessage("Profile picture uploaded successfully")
                    }
                    InvalidCredentials, InternalServer,UnAuthorizedAccess -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            }, onError = {
                view.viewprogress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternetConnection(false)
                }

            }).let { disposables.addAll(it) }
    }

    private fun validateProfile(heightValue1: String, weightValue1: String, bmi: String, bloodPressure: String,alcohol: String,smoker: String,postcode: String,addressline1: String,addressline2: String,city: String): Boolean {
        if (heightValue1.isEmpty()) {
            view.displayErrorMessage("Please provide all mandatory details")
            return false
        }
        if (weightValue1.isEmpty()) {
            view.displayErrorMessage("Please provide all mandatory details")
            return false
        }
        if (bmi.isEmpty()) {
            view.displayErrorMessage("Please provide all mandatory details")
            return false
        }
        if (bloodPressure.isEmpty()) {
            view.displayErrorMessage("Please provide all mandatory details")
            return false
        }
        if (alcohol.isEmpty()) {
            view.displayErrorMessage("Please provide all mandatory details")
            return false
        }
        if (smoker.isEmpty()) {
            view.displayErrorMessage("Please provide all mandatory details")
            return false
        }
        if (postcode.isEmpty()) {
            view.displayErrorMessage("Please provide all mandatory details")
            return false
        }
        if (addressline1.isEmpty()) {
            view.displayErrorMessage("Please provide all mandatory details")
            return false
        }
        if (addressline2.isEmpty()) {
            view.displayErrorMessage("Please provide all mandatory details")
            return false
        }
        if (city.isEmpty()) {
            view.displayErrorMessage("Please provide all mandatory details")
            return false
        }
        return true
    }

    interface View {
        fun showProfile(patient:Patient)
        fun displayMessage(message: String)
        fun displaySuccessMessage(message: String)
        fun displayErrorMessage(message: String)
        fun userData(user: User)
        fun viewprogress(isShow: Boolean)
        fun noInternetConnection(isConnect:Boolean)
    }
}