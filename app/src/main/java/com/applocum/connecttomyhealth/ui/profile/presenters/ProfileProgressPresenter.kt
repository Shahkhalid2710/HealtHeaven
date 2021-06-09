package com.applocum.connecttomyhealth.ui.profile.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.profile.models.ProfileProgressResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class ProfileProgressPresenter@Inject constructor(private val api:AppEndPoint) {
    var disposables=CompositeDisposable()
    lateinit var view:View

    @Inject
    lateinit var userHolder: UserHolder

    fun injectView(view: View)
    {
        this.view=view
    }

    fun trackProfileProgress()
    {
        view.viewProfileProgress(true)
        api.trackProfileProgress(userHolder.userToken)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext ={
                view.viewProfileProgress(false)
                when(it.status)
                {
                    Success->
                    {
                        val progressObject=Gson().fromJson(it.data,ProfileProgressResponse::class.java)
                        view.profileProgressDetail(progressObject)

                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayProgressErrorMessage(it.message)
                    }
                }
            },onError = {
                view.viewProfileProgress(false)
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

    interface View{
        fun displayProgressErrorMessage(message:String)
        fun viewProfileProgress(isShow:Boolean)
        fun profileProgressDetail(progressResponse: ProfileProgressResponse)
    }
}