package com.applocum.connecttomyhealth.ui.profiledetails

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileDetailsPresenter@Inject constructor(private val api:AppEndPoint) {
    private val disposables=CompositeDisposable()
    lateinit var view: View
    @Inject
    lateinit var userHolder: UserHolder

    fun injectview(view: View)
    {
        this.view=view
    }

    fun showProfile()
    {
        view.viewprogress(true)
        api.showProfile(userHolder.userToken,userHolder.userid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewprogress(false)
                when (it.status) {
                    Success -> {
                        val patientObject = Gson().fromJson(it.data,PatientResponse::class.java)
                        val patient = patientObject.patient
                        view.showProfile(patient)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                view.viewprogress(false)
                it.printStackTrace()
            }).let { disposables.add(it) }
    }

    fun updateProfile(firstname:String, lastname:String,email:String, phoneno:String, gender:String, dob:String)
    {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user[first_name]", firstname)
            .addFormDataPart("user[last_name]", lastname)
            .addFormDataPart("user[email]", email)
            .addFormDataPart("user[phone]", phoneno)
            .addFormDataPart("user[gender]", gender)
            .addFormDataPart("patient[date_of_birth]",dob)
            .build()

        api.updateProfile(userHolder.userToken,userHolder.userid,requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext={
                when (it.status) {
                    Success -> {
                        view.displayMessage(it.message)
                        val patientObject = Gson().fromJson(it.data,PatientResponse::class.java)
                        val patient = patientObject.patient
                        view.showProfile(patient)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            },onError = {
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

   fun updateUser(image:String)
   {
       view.viewprogress(true)
       val requestBody: RequestBody = MultipartBody.Builder()
           .setType(MultipartBody.FORM)
           .addFormDataPart("user[image]",image)
           .build()

       api.updateUser(userHolder.userToken,userHolder.userid,requestBody)
           .observeOn(AndroidSchedulers.mainThread())
           .subscribeBy(onNext ={
               view.viewprogress(false)
               when(it.status)
               {
                   Success->
                   {
                       val userObject = Gson().fromJson(it.data, UserResponse::class.java)
                       val user = userObject.user
                       view.userData(user)
                       view.displayMessage("Profile picture uploaded successfully")
                   }
                   InvalidCredentials,InternalServer -> {
                       view.displayMessage(it.message)
                   }
               }

           },onError = {
               view.viewprogress(false)
               it.printStackTrace()
           }).let { disposables.addAll(it) }
   }
    interface View
    {
        fun showProfile(patient: Patient)
        fun displayMessage(message:String)
        fun userData(user: User)
        fun viewprogress(isShow: Boolean)
    }
}