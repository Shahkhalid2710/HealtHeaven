package com.applocum.connecttomyhealth.ui.specialists.presenters

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


class SpecilistPresenter @Inject constructor(private val api: AppEndPoint) {
    private val disposables = CompositeDisposable()
    lateinit var view: View

    @Inject
    lateinit var userHolder: UserHolder

    fun injectview(view: View) {
        this.view = view
    }

    fun getDoctorlist() {
        view.viewProgress(true)
        api.getdoctors(userHolder.userToken!!, 66)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                when (it.status) {
                    Success -> {
                        view.viewProgress(false)
                        view.getdoctorlist(it.data)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displaymessage(it.message)
                    }
                }
            }, onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposables.add(it) }
    }

    interface View {
        fun displaymessage(message: String)
        fun getdoctorlist(list: ArrayList<Specialist>)
        fun viewProgress(isShow: Boolean)
    }
}