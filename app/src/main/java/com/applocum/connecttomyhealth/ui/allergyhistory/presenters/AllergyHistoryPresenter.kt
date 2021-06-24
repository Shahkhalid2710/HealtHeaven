package com.applocum.connecttomyhealth.ui.allergyhistory.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.allergyhistory.models.AllergyResponse
import com.applocum.connecttomyhealth.ui.allergyhistory.models.FalseAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.models.TrueAllergy
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.UnknownHostException
import javax.inject.Inject

class AllergyHistoryPresenter@Inject constructor(private val api:AppEndPoint) {

    val disposables = CompositeDisposable()

    lateinit var view: View

    companion object
    {
        const val activeAllergy="active"
        const val pastAllergy="past"
        const val statusVerified="verified"
        const val statusUnverified="unverified"
    }

    fun injectView(view: View)
    {
        this.view=view
    }

    @Inject
    lateinit var userHolder: UserHolder

    fun addAllergy(allergyName: String,activePast: Boolean)
    {
        if (validateAllergy(allergyName))
        {
            view.viewAllergyProgress(true)
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_allergy[snomed_code_id]",allergyName)
                .addFormDataPart("user_allergy[user_id]",userHolder.userid.toString())
                .addFormDataPart("user_allergy[is_active]",activePast.toString())
                .build()

            api.addAllergyHistory(userHolder.userToken,userHolder.clinicalToken,requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext={
                    view.viewAllergyProgress(false)
                    when(it.status)
                    {
                        Success->
                        {
                            view.noInternetConnection(true)
                            view.displaySuccessMessage(it.message)
                        }
                        InvalidCredentials,InternalServer -> {
                            view.displayErrorMessage(it.message)
                        }
                    }

                },onError = {
                    view.viewAllergyProgress(false)
                    it.printStackTrace()

                    if (it is UnknownHostException)
                    {
                        view.noInternetConnection(false)
                    }

                }).let { disposables.addAll(it) }
        }
    }

    fun activeAllergy()
    {
        view.viewProgress(true)
        api.showAllergyHistory(userHolder.userToken,userHolder.clinicalToken,userHolder.userid!!.toInt(),
            activeAllergy,
            statusUnverified,66)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                when (it.status) {
                    Success -> {
                        view.noInternetConnection(true)
                        view.viewProgress(false)
                        val allergyResponse = Gson().fromJson(it.data,AllergyResponse::class.java)
                        view.showActiveAllergy(allergyResponse.trueAllergy)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            }, onError = {
                view.viewProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternetConnection(false)
                }

            }).let { disposables.addAll(it) }
    }

   fun pastAllergy()
    {
        view.viewProgress(true)
        api.showAllergyHistory(userHolder.userToken,userHolder.clinicalToken,userHolder.userid!!.toInt(),
            pastAllergy,
            statusUnverified,66)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                when (it.status) {
                    Success -> {
                        view.noInternetConnection(true)
                        view.viewProgress(false)
                        val allergyResponse = Gson().fromJson(it.data,AllergyResponse::class.java)
                        view.showPastAllergy(allergyResponse.falseAllergy)
                    }
                    InvalidCredentials, InternalServer -> {
                        view.displayErrorMessage(it.message)
                    }
                }
            }, onError = {
                view.viewProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternetConnection(false)
                }

            }).let { disposables.addAll(it) }
    }


    private fun validateAllergy(allergyName:String):Boolean
  {
      if (allergyName.isEmpty()) {
          view.displayErrorMessage("Please select allergy from the list")
          return false
      }
      return true
  }

 interface View
 {
     fun displaySuccessMessage(message:String)
     fun displayErrorMessage(message: String)
     fun showActiveAllergy(activeAllergy:ArrayList<TrueAllergy>)
     fun showPastAllergy(pastAllergy:ArrayList<FalseAllergy>)
     fun viewProgress(isShow: Boolean)
     fun viewAllergyProgress(isShow: Boolean)
     fun noInternetConnection(isConnect:Boolean)
  }
}