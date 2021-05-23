package com.applocum.connecttomyhealth.ui.payment.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.payment.models.MembershipResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MembershipPresenter@Inject constructor(private val api:AppEndPoint) {
    var disposables=CompositeDisposable()
    lateinit var view: View

    fun injectView(view: View)
    {
        this.view=view
    }

    companion object
    {
        const val corporateId=83
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun showSavedCodes()
    {
        view.viewProgress(true)
        api.getMembershipList(userHolder.userToken!!, corporateId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext={
                view.viewProgress(false)
                when(it.status)
                {
                    Success ->{
                        view.showMembershipList(it.data)
                    }
                    InvalidCredentials,InternalServer ->{
                        view.displayMessage(it.message)
                    }
                }
            },onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }


    interface View{
        fun displayMessage(message:String)
        fun viewProgress(isShow:Boolean)
        fun showMembershipList(membershipResponse:ArrayList<MembershipResponse>)
    }
}