package com.applocum.connecttomyhealth.ui.photoid.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.MissingParameter
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.profiledetails.models.Documents
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.profiledetails.models.PatientResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class PhotoIdPresenter@Inject constructor(private val api:AppEndPoint) {
    var disposable=CompositeDisposable()
    lateinit var view: View

    @Inject
    lateinit var userHolder: UserHolder

    fun injectView(view: View)
    {
        this.view=view
    }

    fun uploadDocument(documentImage:File)
    {
        view.viewProgress(true)
        val multiPartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val requestBody = RequestBody.create("image/png".toMediaTypeOrNull(),documentImage)
        multiPartBuilder.addFormDataPart("document[0][file]", documentImage.nameWithoutExtension, requestBody)

        api.updateDocument(userHolder.userToken, userHolder.userid,multiPartBuilder.build())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewProgress(false)
                when (it.status) {
                    Success -> {
                        view.displayMessage(it.message)
                        val patientObject = Gson().fromJson(it.data, PatientResponse::class.java)
                        val patient = patientObject.patient
                        view.showDocument(patient)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            }, onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposable.addAll(it) }
    }

    fun showDocument() {
        view.viewProgress(true)
        api.showProfile(userHolder.userToken, userHolder.userid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewProgress(false)
                when (it.status) {
                    Success -> {
                        val patientObject = Gson().fromJson(it.data, PatientResponse::class.java)
                        val patient = patientObject.patient
                        view.showDocument(patient.documents)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            }, onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposable.add(it) }
    }

    fun deleteDocument(id: Int) {
        view.viewFullProgress(true)
        api.deleteDocument(userHolder.userToken, id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewFullProgress(false)
                when (it.status) {
                    Success -> {
                    }
                    InvalidCredentials, InternalServer,MissingParameter -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            }, onError = {
                view.viewFullProgress(false)
                it.printStackTrace()
            }).let { disposable.addAll(it) }
    }

    interface View
    {
        fun displayMessage(message:String)
        fun displayErrorMessage(message:String)
        fun showDocument(patient: Patient)
        fun showDocument(list:ArrayList<Documents>)
        fun viewProgress(isShow:Boolean)
        fun viewFullProgress(isShow:Boolean)
    }
}