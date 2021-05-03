package com.applocum.connecttomyhealth.ui.mygp

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class GpservicePresenter@Inject constructor(private val api:AppEndPoint) {
    public var disposables=CompositeDisposable()
    lateinit var view: View

    fun injectview(view: View)
    {
        this.view=view
    }

    fun getgpList(search:String)
    {
        view.viewProgress(true)
        api.getGpList(search)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext={
                when (it.status) {
                    Success -> {
                        view.viewProgress(false)
                        view.getGpList(it.data)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayMessage(it.message)
                    }
                }
            },onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }


    interface View
    {
        fun displayMessage(message:String)
        fun getGpList(list:ArrayList<GpService>)
        fun viewProgress(isShow: Boolean)
    }
}