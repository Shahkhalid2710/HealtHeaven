package com.applocum.connecttomyhealth.ui.booksession

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.booksession.models.Common
import com.applocum.connecttomyhealth.ui.booksession.models.Time
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class BookSessionPresenter@Inject constructor(private val api:AppEndPoint) {
   private var disposables=CompositeDisposable()
    lateinit var view: View

    @Inject
   lateinit var userHolder:UserHolder

    fun injectview(view: View)
    {
        this.view=view
    }

    fun getTimeSlots(doctorid:Int,date:String,sesionType:String?=null,sessionSlot:String?=null)
    {
        view.viewProgress(true)
        sesionType?.let {
            sessionSlot?.let { it1 ->
                api.getTimeSlots(userHolder.userToken!!,date,doctorid, it, it1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onNext={
                        view.viewProgress(false)
                        when(it.status) {
                            Success-> {
                                view.getTimeSlot(it.data)
                                view.getPrice(it.common)
                            }
                            InvalidCredentials,InternalServer -> {
                                view.displaymessage(it.message)
                            }
                        }
                    },onError = {
                        view.viewProgress(false)
                        it.printStackTrace()
                    }).let { disposables.addAll(it) }
            }
        }
    }

    interface View{
        fun getTimeSlot(list: ArrayList<Time>)
        fun displaymessage(message:String)
        fun viewProgress(isShow: Boolean)
        fun getPrice(common: Common)
    }
}