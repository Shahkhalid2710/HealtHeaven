package com.applocum.connecttomyhealth.ui.specialists.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.UnAuthorizedAccess
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.PaginationModel
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.net.UnknownHostException
import javax.inject.Inject

class SpecilistPresenter @Inject constructor(private val api: AppEndPoint) {
    val disposables = CompositeDisposable()
    lateinit var view: View
    var nextPage: String? = "1"

    @Inject
    lateinit var userHolder: UserHolder

    fun injectview(view: View) {
        this.view = view
    }

    fun getDoctorlist() {
        nextPage?.let {
            view.showProgress()
            view.viewProgress(true)
            view.noInternet(true)

            nextPage?.let { page ->
                api.getdoctors(userHolder.userToken!!, 66, page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onNext = {
                        view.hideProgress()
                        view.viewProgress(false)
                        when (it.body()?.status) {
                            Success -> {
                                val paginationModel = Gson().fromJson(it.headers()["X-Pagination"], PaginationModel::class.java)
                                nextPage = paginationModel.nextPage
                                it.body()?.let {
                                    view.getdoctorlist(it.data,nextPage)
                                }
                            }
                            InvalidCredentials, InternalServer,UnAuthorizedAccess -> {
                                it.body()?.let {
                                    view.displaymessage(it.message)
                                }
                            }
                        }
                    }, onError = {
                        view.hideProgress()
                        view.viewProgress(false)
                        it.printStackTrace()
                        if (it is UnknownHostException) {
                            view.noInternet(false)
                        }

                    }).let { disposables.add(it) }
            }
        }
    }

    fun safeDispose() {
        disposables.clear()
        disposables.dispose()
    }

    interface View {
        fun displaymessage(message: String)
        fun getdoctorlist(list: ArrayList<Specialist?>, page:String?)
        fun viewProgress(isShow: Boolean)
        fun showProgress()
        fun hideProgress()
        fun noInternet(isConnect: Boolean)
    }
}