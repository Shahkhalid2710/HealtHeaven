package com.applocum.connecttomyhealth.ui.addcard

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.addcard.models.Card
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddCardPresenter@Inject constructor(val api:AppEndPoint) {
    private var disposables=CompositeDisposable()
    lateinit var view: View

    @Inject
    lateinit var userHolder: UserHolder

    fun injectview(view: View)
    {
        this.view=view
    }

    fun addCard(cardnumber: String,holdername:String,expirydate:String,cvv: String)
    {
        if (validateCard(cardnumber,holdername,expirydate,cvv))
        {
            view.viewProgress(true)
            val requestBody:RequestBody=MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("card[cardNumber]",cardnumber)
                .addFormDataPart("card[cardHolderName]",holdername)
                .addFormDataPart("card[expiryDate]",expirydate)
                .addFormDataPart("card[securityCode]",cvv)
                .build()

            api.addCard(userHolder.userToken!!,requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    view.viewProgress(false)
                    when (it.status) {
                        Success -> {
                            val cardobject = Gson().fromJson(it.data, Card::class.java)
                            view.addcard(cardobject)
                            println("MessageSignup:: ${it.message}")
                            view.displaymessage(it.message)
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
    }

    fun showCardDetails()
    {
        api.showCard(userHolder.userToken!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext={
                when(it.status)
                {
                    Success->{
                          view.showcard(it.data)
                    }
                    InvalidCredentials, InternalServer->{
                        view.displaymessage(it.message)
                    }
                }

            },onError = {
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

    private fun validateCard(cardnumber:String,cardholder:String,date:String,cvv:String):Boolean
    {
        if (cardnumber.isEmpty()) {
            view.displaymessage("Please enter card number")
            return false
        }
        if (cardnumber.length!=16)
        {
            view.displaymessage("Minimum 16 characters allowed")
            return false
        }
        if (cardholder.isEmpty()) {
            view.displaymessage("Please enter name")
            return false
        }
        if (cardholder.length!=10)
        {
            view.displaymessage("Minimum 10 characters allowed")
            return false
        }
        if (date.isEmpty()) {
            view.displaymessage("Please enter date")
            return false
        }

        if (date.length!=4)
        {
            view.displaymessage("Minimum 4 characters allowed")
            return false
        }
        if (cvv.isEmpty()) {
            view.displaymessage("Please enter cvv")
            return false
        }
        if (cvv.length!=3)
        {
            view.displaymessage("Minimum 3 characters allowed")
            return false
        }
        return true
    }

    interface View {
        fun displaymessage(message: String?)
        fun addcard(card: Card)
        fun viewProgress(isShow: Boolean)
        fun showcard(list:ArrayList<Card>)
    }
}