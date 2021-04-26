package com.applocum.connecttomyhealth.ui.specialists

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SpecilistPresenter@Inject constructor(private val api:AppEndPoint)  {
    val disposables=CompositeDisposable()
    lateinit var view:View
    var CORPORATE_ID="83"


    @Inject
    lateinit var userHolder:UserHolder
    fun injectview(view: View)
    {
        this.view=view
    }

    fun getlist()
    {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("AUTH_TOKEN",userHolder.userToken!!)
            .addFormDataPart("corporate_organization_id", CORPORATE_ID)
            .build()
        api.getdoctors()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
             when(it.status)
             {
                 Success->{
                    // val doctorobject:Specialist=Gson().fromJson(it.data,Specialist::class.java)
                     view.getdoctorlist(it.data)
                     view.displaymessage(it.message)
                 }
                 InvalidCredentials,InternalServer -> {
                     view.displaymessage(it.message)
                 }

             }
            }, onError = {
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }


    interface View
    {
        fun displaymessage(message:String)
        fun getdoctorlist(list:ArrayList<Specialist>)
    }
}