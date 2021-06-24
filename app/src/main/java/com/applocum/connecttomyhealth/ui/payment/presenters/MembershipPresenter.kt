package com.applocum.connecttomyhealth.ui.payment.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.NotFound
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.payment.models.MembershipResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.UnknownHostException
import javax.inject.Inject

class MembershipPresenter @Inject constructor(private val api: AppEndPoint) {
    var disposables = CompositeDisposable()
    lateinit var view: View

    fun injectView(view: View) {
        this.view = view
    }

    companion object {
        const val corporateId = 83
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun addMembership(code:String)
    {
        if (validateCode(code))
        {
            view.viewProgress(true)
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("subscribe_code",code)
                .build()

            api.addMembership(userHolder.userToken,requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewProgress(false)
                    when(it.status)
                    {
                        Success->
                        {
                            view.noInternet(true)
                            view.displayMessage(it.message)
                        }
                        InternalServer, InvalidCredentials,NotFound->
                        {
                            view.displayErrorMessage(it.message)
                        }
                    }
                },onError = {
                    view.viewProgress(false)
                    it.printStackTrace()

                    if (it is UnknownHostException)
                    {
                        view.noInternet(false)
                    }

                }).let { disposables.addAll(it) }
           }
      }

    fun showSavedCodes() {
        view.viewProgress(true)
        api.getMembershipList(userHolder.userToken!!, corporateId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewProgress(false)
                when (it.status) {
                    Success -> {
                        view.noInternet(true)
                        view.showMembershipList(it.data)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayMessage(it.message)
                    }
                    NotFound -> {
                        view.showMembershipList(it.data)
                    }
                }
            }, onError = {
                view.viewProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternet(false)
                }

            }).let { disposables.addAll(it) }
    }

    private fun validateCode(code: String):Boolean {
        if (code.isEmpty())
        {
            view.displayErrorMessage("Please enter membership code to access additional benefits")
            return false
        }
        return true
    }

    interface View {
        fun displayMessage(message: String)
        fun displayErrorMessage(message: String)
        fun viewProgress(isShow: Boolean)
        fun showMembershipList(membershipResponse: ArrayList<MembershipResponse>)
        fun noInternet(isConnect:Boolean)
    }
}