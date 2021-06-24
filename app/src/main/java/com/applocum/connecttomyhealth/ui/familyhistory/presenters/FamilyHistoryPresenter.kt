package com.applocum.connecttomyhealth.ui.familyhistory.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.familyhistory.models.FamilyHistory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.UnknownHostException
import javax.inject.Inject

class FamilyHistoryPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View

    fun injectView(view: View) {
        this.view = view
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun addFamilyHistory(name: String) {
        if (validateFamilyHistory(name)) {
            view.viewFamilyHistoryProgress(true)
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("snomed_code_id", name)
                .addFormDataPart("user_id", userHolder.userid.toString())
                .build()

            api.addFamilyHistory(userHolder.userToken, userHolder.clinicalToken, requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewFamilyHistoryProgress(false)
                    when (it.status) {
                        Success -> {
                            view.displaySuccessMessage(it.message)
                        }
                        InvalidCredentials, InternalServer -> {
                            view.displayErrorMessage(it.message)
                        }
                    }

                }, onError = {
                    view.viewFamilyHistoryProgress(false)
                    it.printStackTrace()

                    if (it is UnknownHostException)
                    {
                        view.noInternet(false)
                    }

                }).let { disposables.addAll(it) }
        }
    }

    fun showFamilyHistoryList() {
        view.viewFamilyHistoryProgress(true)
        api.showFamilyHistory(userHolder.userToken, userHolder.clinicalToken, 66)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                when (it.status) {
                    Success -> {
                        view.viewFamilyHistoryProgress(false)
                        view.familyHistoryList(it.data)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            }, onError = {
                view.viewFamilyHistoryProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternet(false)
                }

            }).let { disposables.add(it) }
    }

    private fun validateFamilyHistory(name: String): Boolean {
        if (name.isEmpty()) {
            view.displayErrorMessage("Please select disease from the list")
            return false
        }
        return true
    }

    interface View {
        fun displayErrorMessage(message: String)
        fun displaySuccessMessage(message: String)
        fun viewFamilyHistoryProgress(isShow: Boolean)
        fun familyHistoryList(list: ArrayList<FamilyHistory>)
        fun noInternet(isConnect:Boolean)
    }
}