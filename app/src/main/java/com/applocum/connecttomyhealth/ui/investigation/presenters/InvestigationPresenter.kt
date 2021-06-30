package com.applocum.connecttomyhealth.ui.investigation.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.SessionExpired
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.PaginationModel
import com.applocum.connecttomyhealth.ui.investigation.models.Investigation
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.UnknownHostException
import javax.inject.Inject

class InvestigationPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View
    var nextPage: String? = "1"

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
                            view.noInternet(true)
                            view.displaySuccessMessage(it.message)
                        }
                        InvalidCredentials, InternalServer -> {
                            view.displayMessage(it.message)
                        }
                        SessionExpired ->
                        {
                            view.sessionExpired(it.message)
                        }
                    }

                }, onError = {
                    view.viewInvestigationProgress(false)
                    it.printStackTrace()

                    if (it is UnknownHostException)
                    {
                        view.noInternet(false)
                    }

                }).let { disposables.addAll(it) }
        }
    }

    fun showInvestigationList() {
        nextPage?.let {
            view.showProgress()
            view.noInternet(true)
            nextPage?.let {
                api.showInvestigation(userHolder.userToken, userHolder.clinicalToken, 66, it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onNext = {
                        view.hideProgress()
                        when (it.body()?.status) {
                            Success -> {
                                val paginationModel = Gson().fromJson(
                                    it.headers()["X-Pagination"],
                                    PaginationModel::class.java
                                )
                                nextPage = paginationModel.nextPage
                                it.body()?.let {
                                    view.investigationList(it.data,nextPage)
                                }
                            }
                            InvalidCredentials, InternalServer -> {
                                it.body()?.let {
                                    view.displayMessage(it.message)
                                }
                            }
                            SessionExpired ->
                            {
                                it.body()?.let {
                                    view.sessionExpired(it.message)
                                }
                            }
                        }
                    }, onError = {
                        view.hideProgress()
                        it.printStackTrace()
                        if (it is UnknownHostException) {
                            view.noInternet(false)
                        }

                    }).let { disposables.add(it) }
            }
        }
    }

    fun resetPage()
    {
        nextPage="1"
    }

    fun safeDispose() {
        disposables.clear()
        disposables.dispose()
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
        fun investigationList(list: ArrayList<Investigation?>, page:String?)
        fun noInternet(isConnect:Boolean)
        fun showProgress()
        fun hideProgress()
        fun sessionExpired(message: String)
    }
}