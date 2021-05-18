package com.applocum.connecttomyhealth.ui.medicalhistory.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.medicalhistory.models.*
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MedicalPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View

    companion object
    {
        const val activeMedicalHistory="active"
        const val pastMedicalHistory="past"
        const val statusVerified="verified"
        const val statusUnverified="unverified"
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun injectView(view: View) {
        this.view = view
    }

    fun getDiseaseList(search: String) {
        view.viewProgress(true)
        api.getDiseaseList(search)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewProgress(false)
                when (it.status) {
                    Success -> {
                        view.getDiseaseList(it.data)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

    fun addMedicalHistory(
        diseaseName: String,
        startMonth: String,
        startYear: String,
        activePast:Boolean,
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
            val startDate= "01/$startMonth/$startYear"
            val endDate= "01/$endMonth/$endYear"
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("medical_history[user_id]", userHolder.userid!!)
                .addFormDataPart("medical_history[start_date]", startDate)
                .addFormDataPart("medical_history[end_date]", endDate)
                .addFormDataPart("medical_history[is_active]", activePast.toString())
                .addFormDataPart("medical_history[snomed_code_id]", diseaseName)
                .build()

            api.addMedicalHistory(userHolder.userToken!!, userHolder.clinicalToken, requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    when (it.status) {
                        Success -> {
                            val medicalObject = Gson().fromJson(it.data,MedicalHistoryResponse::class.java)
                            val medicalHistory = medicalObject.medical_history
                            view.sendMedicalHistoryData(medicalHistory)
                        }
                        InvalidCredentials, InternalServer -> {
                            view.displayMessage(it.message)
                        }
                    }

                }, onError = {
                    it.printStackTrace()
                }).let { disposables.addAll(it) }
        }
    }

    fun activeMedicalHistory()
    {
        api.showMedicalHistory(userHolder.userToken,userHolder.clinicalToken,userHolder.userid!!.toInt(),
            activeMedicalHistory,
            statusUnverified,66)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                when (it.status) {
                    Success -> {
                        val medicalHistoryTrueFalseResponse = Gson().fromJson(it.data,MedicalHistoryTrueFalseResponse::class.java)
                        view.showActiveMedicalHistory(medicalHistoryTrueFalseResponse.medical_history.trueMedicalHistory)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                it.printStackTrace()
            }).let { disposables.add(it) }
    }

    fun pastMedicalHistory()
    {
        api.showMedicalHistory(userHolder.userToken,userHolder.clinicalToken,userHolder.userid!!.toInt(),
            pastMedicalHistory,statusUnverified,66)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                when (it.status) {
                    Success -> {
                        val medicalHistoryTrueFalseResponse = Gson().fromJson(it.data,MedicalHistoryTrueFalseResponse::class.java)
                        view.showPastMedicalHistory(medicalHistoryTrueFalseResponse.medical_history.falseMedicalHistory)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                it.printStackTrace()
            }).let { disposables.add(it) }
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
            return false
        }
        return true
    }

    interface View {
        fun displayMessage(message: String)
        fun getDiseaseList(list: ArrayList<Medical>)
        fun viewProgress(isShow: Boolean)
        fun sendMedicalHistoryData(medicalHistory: MedicalHistory)
        fun showActiveMedicalHistory(trueMedicalHistory:ArrayList<TrueMedicalHistory>)
        fun showPastMedicalHistory(falseMedicalHistory:ArrayList<FalseMedicalHistory>)
    }

}