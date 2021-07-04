package com.applocum.connecttomyhealth.ui.prescription.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.UnAuthorizedAccess
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.PaginationModel
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.net.UnknownHostException
import javax.inject.Inject

class DocumentPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View
    var nextPage: String? = "1"

    fun injectView(view: View) {
        this.view = view
    }

    companion object {
        const val prescription = "prescription"
        const val fitNote = "fit_note"
        const val referralLetter = "referral_letter"
        const val inVoice = "invoice"
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun getPrescription() {
        nextPage?.let {
            view.showProgress()
            view.noInternet(true)
            nextPage?.let {
                api.getDocuments(userHolder.userToken, prescription, it)
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
                                    view.getDocument(it.data)
                                }
                            }
                            InvalidCredentials, InternalServer -> {
                                it.body()?.let {
                                    view.displayErrorMessage(it.message)
                                }
                            }
                            UnAuthorizedAccess->{
                                it.body()?.let {
                                    view.displayErrorMessage(it.message)
                                }
                            }
                        }
                    }, onError = {
                        view.hideProgress()
                        it.printStackTrace()
                        if (it is UnknownHostException) {
                            view.noInternet(false)
                        }

                    }).let { disposables.addAll(it) }
            }
        }
    }

    fun getFitNote() {
        nextPage?.let {
            view.showProgress()
            view.noInternet(true)
            nextPage?.let {
                api.getDocuments(userHolder.userToken, fitNote, it)
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
                                    view.getDocument(it.data)
                                }
                            }
                            InvalidCredentials, InternalServer -> {
                                it.body()?.let {
                                    view.displayErrorMessage(it.message)
                                }
                            }
                            UnAuthorizedAccess->{
                                it.body()?.let {
                                    view.displayErrorMessage(it.message)
                                }
                            }
                        }
                    }, onError = {
                        view.hideProgress()
                        it.printStackTrace()
                        if (it is UnknownHostException) {
                            view.noInternet(false)
                        }

                    }).let { disposables.addAll(it) }
            }
        }
    }

    fun getReferral() {
        nextPage?.let {
            view.showProgress()
            view.noInternet(true)
            nextPage?.let {
                api.getDocuments(userHolder.userToken, referralLetter, it)
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
                                    view.getDocument(it.data)
                                }
                            }
                            InvalidCredentials, InternalServer -> {
                                it.body()?.let {
                                    view.displayErrorMessage(it.message)
                                }
                            }
                            UnAuthorizedAccess->{
                                it.body()?.let {
                                    view.displayErrorMessage(it.message)
                                }
                            }
                        }
                    }, onError = {
                        it.printStackTrace()
                        view.hideProgress()
                        if (it is UnknownHostException) {
                            view.noInternet(false)
                        }

                    }).let { disposables.addAll(it) }
            }
        }
    }

    fun getOtherNote() {
        nextPage?.let {
            view.showProgress()
            view.noInternet(true)
            nextPage?.let {
                api.getDocuments(userHolder.userToken, inVoice, it)
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
                                    view.getDocument(it.data)
                                }
                            }
                            InvalidCredentials, InternalServer -> {
                                it.body()?.let {
                                    view.displayErrorMessage(it.message)
                                }
                            }
                            UnAuthorizedAccess->{
                                it.body()?.let {
                                    view.displayErrorMessage(it.message)
                                }
                            }
                        }
                    }, onError = {
                        it.printStackTrace()
                        view.hideProgress()
                        if (it is UnknownHostException) {
                            view.noInternet(false)
                        }

                    }).let { disposables.addAll(it) }
            }
        }
    }

    fun safeDispose() {
        disposables.clear()
        disposables.dispose()
    }

    interface View {
        fun displayErrorMessage(message: String)
        fun getDocument(list: ArrayList<Document>)
        fun noInternet(isConnect: Boolean)
        fun showProgress()
        fun hideProgress()
    }
}