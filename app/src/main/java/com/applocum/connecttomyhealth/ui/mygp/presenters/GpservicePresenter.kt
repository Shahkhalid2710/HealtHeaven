package com.applocum.connecttomyhealth.ui.mygp.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.applocum.connecttomyhealth.ui.mygp.models.Surgery
import com.applocum.connecttomyhealth.ui.mygp.models.SurgeryResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.UnknownHostException
import javax.inject.Inject

class GpservicePresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View

    fun injectview(view: View) {
        this.view = view
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun getgpList(search: String) {
        view.viewProgress(true)
        api.getGpList(search)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                when (it.status) {
                    Success -> {
                        view.noInternetConnection(true)
                        view.viewProgress(false)
                        view.getGpList(it.data)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                view.viewProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternetConnection(false)
                }

            }).let { disposables.addAll(it) }
    }

    fun addGpService(surgeryId: Int) {
        view.viewFullProgress(true)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_surgery[surgery_id]", surgeryId.toString())
            .build()

        api.addGpService(userHolder.userToken, requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewFullProgress(false)
                when (it.status) {
                    Success -> {
                        view.noInternetConnection(true)
                        view.displayMessage(it.message)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }

            }, onError = {
                view.viewFullProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternetConnection(false)
                }
            }).let { disposables.addAll(it) }
    }

    fun getGpService() {
        view.viewFullProgress(true)
        api.getGpService(userHolder.userToken, userHolder.userid!!.toInt())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewFullProgress(false)
                when (it.status) {
                    Success -> {
                        view.noInternetConnection(true)
                        val surgeryObject = Gson().fromJson(it.data,SurgeryResponse::class.java)
                        val surgery = surgeryObject.surgery
                        surgery?.let { it1 -> view.showSurgery(it1) }?: kotlin.run{view.emptySurgery()}
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                view.viewFullProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternetConnection(false)
                }

            }).let { disposables.add(it) }
    }

    interface View {
        fun displayMessage(message: String)
        fun getGpList(list: ArrayList<GpService>)
        fun viewProgress(isShow: Boolean)
        fun viewFullProgress(isShow: Boolean)
        fun showSurgery(surgery: Surgery)
        fun emptySurgery()
        fun noInternetConnection(isConnect:Boolean)
    }
}