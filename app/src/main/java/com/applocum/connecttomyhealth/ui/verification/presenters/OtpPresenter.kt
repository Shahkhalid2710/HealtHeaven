package com.applocum.connecttomyhealth.ui.verification.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.AlreadyExist
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidOtp
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.MissingParameter
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.OtpTryAgain
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class OtpPresenter@Inject constructor(private val api:AppEndPoint) {
    var disposable=CompositeDisposable()
    lateinit var view:View
    var role="patient"

    @Inject
    lateinit var userHolder: UserHolder

    fun injectView(view: View)
    {
        this.view=view
    }

    fun sendOtp(phoneNumber:String)
    {
        view.viewProgress(true)
        api.sendOtp(phoneNumber, role)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewProgress(false)
                when(it.status)
                {
                    Success->
                    {
                        view.displayMessage(it.message)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            },onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposable.addAll(it) }
    }

    fun verifyOtp(mobileNumber:String,otp:String)
    {
        view.viewProgress(true)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("phone",mobileNumber)
            .addFormDataPart("otp",otp)
            .addFormDataPart("user_id",userHolder.userid!!)
            .addFormDataPart("role",role)
            .build()

        api.verifyOtp(userHolder.userToken,requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewProgress(false)
                    when(it.status)
                    {
                        Success->{
                            view.displaySuccessMessage(it.message)
                        }
                        InvalidCredentials, InternalServer->
                        {
                            view.displayErrorMessage(it.message)
                        }
                        InvalidOtp, MissingParameter,AlreadyExist,OtpTryAgain->
                        {
                            view.displayErrorMessage(it.message)
                        }
                    }
            },onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposable.addAll(it) }
    }

    interface View{
        fun displayMessage(message:String)
        fun displayErrorMessage(message:String)
        fun displaySuccessMessage(message: String)
        fun viewProgress(isShow:Boolean)
    }
}