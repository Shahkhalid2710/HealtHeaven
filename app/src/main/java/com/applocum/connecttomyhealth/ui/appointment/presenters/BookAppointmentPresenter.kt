package com.applocum.connecttomyhealth.ui.appointment.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.AlreadyExist
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.MissingParameter
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class BookAppointmentPresenter@Inject constructor(private val api:AppEndPoint) {
    val UPCOMING_APPOINTMENT = "upcoming"
    val COMPLETED_APPOINTMENT = "past"

    val disposables=CompositeDisposable()
    lateinit var view: View

    @Inject
    lateinit var userHolder: UserHolder

    fun injectView(view: View)
    {
        this.view=view
    }

    fun bookAppointment(time:String,duration: String,comment:String,allowGeoAccess:Boolean,sharedRecordsWithNhsGp:Boolean,appointmentType:String,doctorId:Int,cardIdentifier:Int,organizationId:Int)
    {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("appointment[start_time]",time)
            .addFormDataPart("appointment[duration]",duration)
            .addFormDataPart("appointment[comments]",comment)
            .addFormDataPart("appointment_access[allow_geo_access]",allowGeoAccess.toString())
            .addFormDataPart("appointment_access[consent_for_share_record_with_my_nhs_gp]",sharedRecordsWithNhsGp.toString())
            .addFormDataPart("appointment[appointment_type]",appointmentType)
            .addFormDataPart("appointment[doctor_id]",doctorId.toString())
            .addFormDataPart("cardIdentifier",cardIdentifier.toString())
            .addFormDataPart("organization_id",organizationId.toString())
            .build()
        api.bookAppointment(userHolder.userToken!!,requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext={
                when(it.status)
                {
                    Success->{
                       // view.displayMessage(it.message)
                    }
                    InvalidCredentials,InternalServer -> {
                        view.displayMessage(it.message)
                    }
                    AlreadyExist -> {
                        view.displayMessage(it.message)
                    }
                    MissingParameter ->
                    {
                        view.displayMessage(it.message)
                    }

                }
            },onError = {
              it.printStackTrace()
            }) .let { disposables.addAll(it) }
    }


    fun showUpcomingSession()
    {
        view.viewProgress(true)
        api.upcomingSession(userHolder.userToken!!,UPCOMING_APPOINTMENT,66)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onNext={
                view.viewProgress(false)
                when(it.status)
                {Success->
                    {
                        view.getSessions(it.data)
                    }
                    InternalServer, InvalidCredentials->
                    {
                        view.displayMessage(it.message)
                    }
                }
           },onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }

    fun showPastSession()
    {
        view.viewProgress(true)
        api.upcomingSession(userHolder.userToken!!,COMPLETED_APPOINTMENT,66)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onNext={
                view.viewProgress(false)
                when(it.status)
                {Success->
                {
                    view.getSessions(it.data)
                }
                    InternalServer, InvalidCredentials->
                    {
                        view.displayMessage(it.message)
                    }
                }
            },onError = {
                view.viewProgress(false)
                it.printStackTrace()
            }).let { disposables.addAll(it) }
    }


    fun deleteSession(id:Int)
    {
        view.viewFullProgress(true)
        api.deleteAppointment(userHolder.userToken!!,id)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeBy(onNext={
              view.viewFullProgress(false)
              when(it.status)
              {
                  Success->{
                      view.displaySuccessMessage(it.message)
                  }
                  InvalidCredentials, InternalServer,MissingParameter->
                  {
                      view.displayMessage(it.message)
                  }
              }
          },onError = {
              view.viewFullProgress(false)
              it.printStackTrace()
          }).let { disposables.addAll(it) }
    }

    interface View{
        fun displayMessage(mesage:String)
        fun displaySuccessMessage(message:String)
        fun getSessions(list:ArrayList<BookAppointmentResponse>)
        fun viewProgress(isShow: Boolean)
        fun viewFullProgress(isShow: Boolean)
    }
}