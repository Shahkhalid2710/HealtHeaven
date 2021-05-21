package com.applocum.connecttomyhealth.ui.mygp

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.applocum.connecttomyhealth.ui.mygp.models.Surgery
import com.applocum.connecttomyhealth.ui.mygp.models.SurgeryResponse
import com.applocum.connecttomyhealth.ui.profiledetails.models.PatientResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class GpservicePresenter@Inject constructor(private val api:AppEndPoint) {
    public var disposables=CompositeDisposable()
    lateinit var view: View

    fun injectview(view: View)
    {
        this.view=view
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun getgpList(search:String)
    {
        view.viewProgress(true)
        api.getGpList(search)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext={
                when (it.status) {
                    Success -> {
                        view.viewProgress(false)
                        view.getGpList(it.data)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            },onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

    fun addGpService(surgeryId:Int)
    {
        view.viewFullProgress(true)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_surgery[surgery_id]",surgeryId.toString())
            .build()

        api.addGpService(userHolder.userToken,requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext={
                view.viewFullProgress(false)
                when(it.status)
                {
                    Success->
                    {
                        view.displayMessage(it.message)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }

            },onError = {
                view.viewFullProgress(false)
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

    fun getGpService()
    {
        view.viewFullProgress(true)
        api.getGpService(userHolder.userToken,userHolder.userid!!.toInt())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewFullProgress(false)
                when (it.status) {
                    Success -> {
                        val surgeryObject = Gson().fromJson(it.data,SurgeryResponse::class.java)
                        val surgery = surgeryObject.surgery
                        view.showSurgery(surgery)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                view.viewFullProgress(false)
                it.printStackTrace()
            }).let { disposables.add(it) }

    }

    interface View
    {
        fun displayMessage(message:String)
        fun getGpList(list:ArrayList<GpService>)
        fun viewProgress(isShow: Boolean)
        fun viewFullProgress(isShow: Boolean)
        fun showSurgery(surgery: Surgery)
    }
}