package com.applocum.connecttomyhealth.ui.booksession

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.booksession.models.Time
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class BookSessionPresenter@Inject constructor(private val api:AppEndPoint) {
   private var disposables=CompositeDisposable()
    lateinit var view: View
   // private var token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0aW1lc3RhbXAiOiIyMDIxLTA0LTI2VDEwOjM2OjMyLjE1NCswMDowMCIsImVtYWlsIjoic2FoaWxzQHlvcG1haWwuY29tIn0.zxN3RluOJwc9-e2dp0_RxlcIhyw741NB7LV7ySErnGM"

    @Inject
   lateinit var userHolder:UserHolder

    fun injectview(view: View)
    {
        this.view=view
    }

    fun getTimeSlots(doctorid:Int,date:String,sesionType:String?=null,sessionSlot:String?=null)
    {
        view.viewProgress(true)
        api.getTimeSlots(userHolder.userToken!!,date,doctorid,sesionType!!,sessionSlot!!)
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeBy(onNext={
                      when(it.status)
                      {
                          Success-> {
                              view.viewProgress(false)
                              view.getTimeSlot(it.data)
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

    private fun validation(date: String,sessiontype:String,slot:String):Boolean
    {
        if (date.isEmpty())
        {
            view.displaymessage("Please select date")
            return false
        }
        if (sessiontype.isEmpty())
        {
            view.displaymessage("Please select session type")
            return false
        }
        if (slot.isEmpty())
        {
            view.displaymessage("Please select slot")
            return false
        }
        return true
    }

    interface View{
        fun getTimeSlot(list: ArrayList<Time>)
        fun displaymessage(message:String)
        fun viewProgress(isShow: Boolean)
    }
}