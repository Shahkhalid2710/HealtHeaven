package com.applocum.connecttomyhealth.ui.prescription.models

import android.view.View
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class DocumentPresenter@Inject constructor(private val api:AppEndPoint) {
    var disposables=CompositeDisposable()
    lateinit var view: View

    fun injectView(view: View)
    {
        this.view=view
    }

    companion object
    {
        const val prescription="prescription"
        const val fitNote="fit_note"
        const val referralLetter="referral_letter"
        const val inVoice="invoice"
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun getPrescription()
    {
        view.viewProgress(true)
        api.getDocuments(userHolder.userToken, prescription)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext ={
                view.viewProgress(false)
                when (it.status) {
                    Success -> {
                        view.viewProgress(false)
                        view.getDocument(it.data)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            },onError = {
             it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

    fun getFitNote()
    {
        view.viewProgress(true)
        api.getDocuments(userHolder.userToken, fitNote)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext ={
                view.viewProgress(false)
                when (it.status) {
                    Success -> {
                        view.viewProgress(false)
                        view.getDocument(it.data)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            },onError = {
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

    fun getReferral()
    {
        view.viewProgress(true)
        api.getDocuments(userHolder.userToken, referralLetter)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext ={
                view.viewProgress(false)
                when (it.status) {
                    Success -> {
                        view.viewProgress(false)
                        view.getDocument(it.data)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            },onError = {
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

    fun getOtherNote()
    {
        view.viewProgress(true)
        api.getDocuments(userHolder.userToken, inVoice)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext ={
                view.viewProgress(false)
                when (it.status) {
                    Success -> {
                        view.viewProgress(false)
                        view.getDocument(it.data)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            },onError = {
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }



    interface View{
        fun displayErrorMessage(message:String)
        fun getDocument(list:ArrayList<Document>)
        fun viewProgress(isShow: Boolean)
    }
}