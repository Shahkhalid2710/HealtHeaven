package com.applocum.connecttomyhealth.ui.medicalhistory.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.PaginationModel
import com.applocum.connecttomyhealth.ui.medicalhistory.models.*
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.UnknownHostException
import javax.inject.Inject

class MedicalPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View
    var nextPage: String? = "1"


    companion object {
        const val activeMedicalHistory = "active"
        const val pastMedicalHistory = "past"
        const val statusVerified = "verified"
        const val statusUnverified = "unverified"
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun injectView(view: View) {
        this.view = view
    }

    fun getDiseaseList(search: String) {
        nextPage.let {
            view.showProgress()
            view.noInternet(true)
            nextPage?.let {
                api.getDiseaseList(search, it)
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
                                    view.getDiseaseList(it.data)
                                }
                            }
                            InvalidCredentials, InternalServer -> {
                                it.body()?.let {
                                    view.displayMessage(it.message)
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

    fun addMedicalHistory(
        diseaseName: String,
        startMonth: String,
        startYear: String,
        activePast: Boolean,
        endMonth: String,
        endYear: String
    ) {
        if (validationMedicalHistory(
                diseaseName,
                startMonth,
                startYear,
                activePast,
                endMonth,
                endYear
            )
        ) {
            view.viewMedicalProgress(true)
            val startDate = "01/$startMonth/$startYear"
            val endDate = "01/$endMonth/$endYear"
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("medical_history[user_id]", userHolder.userid!!)
                .addFormDataPart("medical_history[start_date]", startDate)
                .addFormDataPart("medical_history[is_active]", activePast.toString())
                .addFormDataPart("medical_history[end_date]", endDate)
                .addFormDataPart("medical_history[snomed_code_id]", diseaseName)
                .build()

            api.addMedicalHistory(userHolder.userToken!!, userHolder.clinicalToken, requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewMedicalProgress(false)
                    when (it.status) {
                        Success -> {
                            view.noInternet(true)
                            val medicalObject =
                                Gson().fromJson(it.data, MedicalHistoryResponse::class.java)
                            val medicalHistory = medicalObject.medical_history
                            view.sendMedicalHistoryData(medicalHistory)
                        }
                        InvalidCredentials, InternalServer -> {
                            view.displayMessage(it.message)
                        }
                    }

                }, onError = {
                    view.viewMedicalProgress(false)
                    it.printStackTrace()

                    if (it is UnknownHostException) {
                        view.noInternet(false)
                    }

                }).let { disposables.addAll(it) }
        }
    }

    fun activeMedicalHistory() {
        api.showMedicalHistory(
            userHolder.userToken, userHolder.clinicalToken, userHolder.userid!!.toInt(),
            activeMedicalHistory,
            statusUnverified, 66
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                when (it.status) {
                    Success -> {
                        view.noInternet(true)
                        val medicalHistoryTrueFalseResponse =
                            Gson().fromJson(it.data, MedicalHistoryTrueFalseResponse::class.java)
                        view.showActiveMedicalHistory(medicalHistoryTrueFalseResponse.medical_history.trueMedicalHistory)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                it.printStackTrace()

                if (it is UnknownHostException) {
                    view.noInternet(false)
                }

            }).let { disposables.add(it) }
    }

    fun pastMedicalHistory() {
        api.showMedicalHistory(
            userHolder.userToken, userHolder.clinicalToken, userHolder.userid!!.toInt(),
            pastMedicalHistory, statusUnverified, 66
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                when (it.status) {
                    Success -> {
                        view.noInternet(true)
                        val medicalHistoryTrueFalseResponse =
                            Gson().fromJson(it.data, MedicalHistoryTrueFalseResponse::class.java)
                        view.showPastMedicalHistory(medicalHistoryTrueFalseResponse.medical_history.falseMedicalHistory)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                it.printStackTrace()

                if (it is UnknownHostException) {
                    view.noInternet(false)
                }

            }).let { disposables.add(it) }
    }


    fun resetPage()
    {
        nextPage="1"
    }

    fun safeDispose() {
        disposables.clear()
        disposables.dispose()
    }


    private fun validationMedicalHistory(
        diseaseName: String,
        startMonth: String,
        startYear: String,
        activePast: Boolean,
        endMonth: String,
        endYear: String
    ): Boolean {

        if (diseaseName.isEmpty()) {
            view.displayMessage("Please select disease name from the list")
            return false
        }
        if (startMonth.isEmpty()) {
            view.displayMessage("Please select start month")
            return false
        }
        if (startYear.isEmpty()) {
            view.displayMessage("Please select start year")
            return false
        }
        if (!activePast) {
            if (endMonth.isEmpty()) {
                view.displayMessage("Please select end month")
                return false
            }
            if (endYear.isEmpty()) {
                view.displayMessage("Please select end year")
                return false
            }
            return true
        }
        return true
    }

    interface View {
        fun displayMessage(message: String)
        fun getDiseaseList(list: ArrayList<Medical>)
        fun viewMedicalProgress(isShow: Boolean)
        fun sendMedicalHistoryData(medicalHistory: MedicalHistory)
        fun showActiveMedicalHistory(trueMedicalHistory: ArrayList<TrueMedicalHistory>)
        fun showPastMedicalHistory(falseMedicalHistory: ArrayList<FalseMedicalHistory>)
        fun noInternet(isConnect: Boolean)
        fun showProgress()
        fun hideProgress()
    }
}