package com.applocum.connecttomyhealth.ui.investigation.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.investigation.models.Investigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class InvestigationPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View

    fun injectView(view: View) {
        this.view = view
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun addInvestigation(name: String, date: String, description: String) {
        if (validateInvestigation(name, date, description)) {
            view.viewInvestigationProgress(true)
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("investigation[description]", description)
                .addFormDataPart("investigation[taken_on]", date)
                .addFormDataPart("investigation[snomed_code_id]", name)
                .build()

            api.addInvestigation(userHolder.userToken, userHolder.clinicalToken, requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewInvestigationProgress(false)
                    when (it.status) {
                        Success -> {
                            view.displaySuccessMessage(it.message)
                        }
                        InvalidCredentials, InternalServer -> {
                            view.displayMessage(it.message)
                        }
                    }

                }, onError = {
                    view.viewInvestigationProgress(false)
                    it.printStackTrace()
                }).let { disposables.addAll(it) }
        }
    }

    fun showInvestigationList() {
        view.viewInvestigationProgress(true)
        api.showInvestigation(userHolder.userToken, userHolder.clinicalToken, 66)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                when (it.status) {
                    Success -> {
                        view.viewInvestigationProgress(false)
                        view.investigationList(it.data)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                view.viewInvestigationProgress(false)
                it.printStackTrace()
            }).let { disposables.add(it) }

    }

    private fun validateInvestigation(name: String, date: String, description: String): Boolean {
        if (name.isEmpty()) {
            view.displayMessage("Please select investigation from the list")
            return false
        }
        if (date.isEmpty()) {
            view.displayMessage("Please select date")
            return false
        }
        if (description.isEmpty()) {
            view.displayMessage("Please enter investigation description")
            return false
        }
        return true
    }

    interface View {
        fun displaySuccessMessage(message: String)
        fun displayMessage(message: String)
        fun viewInvestigationProgress(isShow: Boolean)
        fun investigationList(list: ArrayList<Investigation>)
    }
}