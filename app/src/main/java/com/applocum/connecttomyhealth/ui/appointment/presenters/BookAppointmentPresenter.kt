package com.applocum.connecttomyhealth.ui.appointment.presenters

import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.AlreadyExist
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InternalServer
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.InvalidCredentials
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.MissingParameter
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.Success
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.UnAuthorizedAccess
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import com.applocum.connecttomyhealth.shareddata.endpoints.BookAppointment
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.PaginationModel
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.UnknownHostException
import javax.inject.Inject

class BookAppointmentPresenter @Inject constructor(private val api: AppEndPoint) {
    companion object {
        const val UPCOMING_APPOINTMENT = "upcoming"
        const val COMPLETED_APPOINTMENT = "past"
        const val PATIENT_ARRIVED="patient_arrived"
    }

    val disposables = CompositeDisposable()
    lateinit var view: View

    var nextPage: String? = "1"

    @Inject
    lateinit var userHolder: UserHolder

    fun injectView(view: View) {
        this.view = view
    }

    fun bookAppointment(bookAppointment: BookAppointment,appointmentType:String,cardIdentifier: Int)
    {
        val pickImageFile = File(bookAppointment.pickedFilePath)
        view.viewFullProgress(true)
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        requestBody.addFormDataPart("appointment[start_time]", bookAppointment.appointmentTime)
        requestBody.addFormDataPart("appointment[duration]", bookAppointment.appointmentSlot)
        requestBody.addFormDataPart("appointment[comments]",bookAppointment.appointmentReason)
        requestBody.addFormDataPart("appointment_access[allow_geo_access]", bookAppointment.allowGeoAccess.toString())
        requestBody.addFormDataPart("appointment_access[consent_for_share_record_with_my_nhs_gp]", bookAppointment.sharedRecordWithNhs.toString())
        requestBody.addFormDataPart("appointment[appointment_type]",appointmentType)
        requestBody.addFormDataPart("appointment[doctor_id]", bookAppointment.therapistId.toString())
        requestBody.addFormDataPart("cardIdentifier", cardIdentifier.toString())
        requestBody.addFormDataPart("organization_id", bookAppointment.corporateId.toString())

        if (pickImageFile.exists()) {
            val fileBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), pickImageFile)
            requestBody.addFormDataPart("appointment_file[file]", "symptomPic", fileBody)
        }

        if (bookAppointment.isRecurring)
        {
            requestBody.addFormDataPart("is_recurring", bookAppointment.isRecurring.toString())
            requestBody.addFormDataPart("recurring[type]",bookAppointment.recurringType.toString())

            when(bookAppointment.recurringType.toString())
            {
                "monthly"->{
                    requestBody.addFormDataPart("recurring[day_of_month]", bookAppointment.recurringMonthDate.toString())
                }
                "weekly"->{
                    requestBody.addFormDataPart("recurring[day_of_week]",bookAppointment.recurringWeekDays.toString())
                }
            }
            requestBody.addFormDataPart("recurring[appointment_count]", bookAppointment.recurringSessionCount.toString())
        }
        requestBody.build()

        api.bookAppointment(userHolder.userToken!!,requestBody.build())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewFullProgress(false)
                when (it.status) {
                    Success -> {
                        view.noInternet(true)
                        view.displaySuccessMessage(it.message)
                    }
                    InvalidCredentials, InternalServer,UnAuthorizedAccess -> {
                        view.displayMessage(it.message)
                    }
                    AlreadyExist -> {
                        view.displayMessage(it.message)
                    }
                    MissingParameter -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                view.viewFullProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternet(false)
                }

            }).let { disposables.addAll(it) }
    }


    /* fun bookAppointment(time: String, duration: String, comment: String, allowGeoAccess: Boolean, sharedRecordsWithNhsGp: Boolean, appointmentType: String, doctorId: Int, cardIdentifier: Int, organizationId: Int,isRecurring:Boolean,recurringType:String,recurringDayOfMonth:String,recuuringDayOfWeek:String,appointmentCount:String)
    {
        view.viewFullProgress(true)
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            requestBody.addFormDataPart("appointment[start_time]", time)
            requestBody.addFormDataPart("appointment[duration]", duration)
            requestBody.addFormDataPart("appointment[comments]", comment)
            requestBody.addFormDataPart("appointment_access[allow_geo_access]", allowGeoAccess.toString())
            requestBody.addFormDataPart("appointment_access[consent_for_share_record_with_my_nhs_gp]", sharedRecordsWithNhsGp.toString())
            requestBody.addFormDataPart("appointment[appointment_type]", appointmentType)
            requestBody.addFormDataPart("appointment[doctor_id]", doctorId.toString())
            requestBody.addFormDataPart("cardIdentifier", cardIdentifier.toString())
            requestBody.addFormDataPart("organization_id", organizationId.toString())

            if (isRecurring)
            {
                requestBody.addFormDataPart("is_recurring", isRecurring.toString())
                requestBody.addFormDataPart("recurring[type]", recurringType)

                when(recurringType)
                {
                    recurringDayOfMonth->{
                        requestBody.addFormDataPart("recurring[day_of_month]",recurringDayOfMonth)
                    }
                    recuuringDayOfWeek->{
                        requestBody.addFormDataPart("recurring[day_of_week]", recuuringDayOfWeek)
                    }
                }
                requestBody.addFormDataPart("recurring[appointment_count]", appointmentCount)
            }
            requestBody.build()

        api.bookAppointment(userHolder.userToken!!,requestBody.build())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewFullProgress(false)
                when (it.status) {
                    Success -> {
                         view.noInternet(true)
                         view.displaySuccessMessage(it.message)
                    }
                    InvalidCredentials, InternalServer,UnAuthorizedAccess -> {
                        view.displayMessage(it.message)
                    }
                    AlreadyExist -> {
                        view.displayMessage(it.message)
                    }
                    MissingParameter -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                view.viewFullProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException)
                {
                    view.noInternet(false)
                }

            }).let { disposables.addAll(it) }
    }
*/
    fun showUpcomingSession() {
        nextPage?.let {
            view.showProgress()
            view.noInternet(true)

            nextPage?.let { page ->
                api.sessions(userHolder.userToken!!, UPCOMING_APPOINTMENT, 66, page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onNext = {
                        view.hideProgress()
                        when (it.body()?.status) {
                            Success -> {
                                val paginationModel = Gson().fromJson(
                                    it.headers()["X-Pagination"],
                                    PaginationModel::class.java
                                )

                                nextPage = paginationModel.nextPage
                                it.body()?.let {
                                    view.getSessions(it.data)
                                }
                            }
                            InternalServer, InvalidCredentials,UnAuthorizedAccess -> {
                                it.body()?.let {
                                    view.displayMessage(it.message)
                                }
                            }
                        }
                    }, onError = {
                        view.hideProgress()
                        it.printStackTrace()
                        if (it is UnknownHostException) {
                            view.noInternet(false)
                        }

                    }).let { disposables.addAll(it) }
            }
        }
    }

    fun showPastSession() {
        nextPage?.let {
            view.showProgress()
            view.noInternet(true)

            nextPage?.let {
                api.sessions(userHolder.userToken!!, COMPLETED_APPOINTMENT, 66, it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onNext = {
                        view.hideProgress()
                        when (it.body()?.status) {
                            Success -> {
                                val paginationModel = Gson().fromJson(
                                    it.headers()["X-Pagination"],
                                    PaginationModel::class.java
                                )
                                nextPage = paginationModel.nextPage
                                it.body()?.let {
                                    view.getSessions(it.data)
                                }
                            }
                            InternalServer, InvalidCredentials,UnAuthorizedAccess -> {
                                it.body()?.let {
                                    view.displayMessage(it.message)
                                }
                            }
                        }
                    }, onError = {
                        view.hideProgress()
                        it.printStackTrace()

                        if (it is UnknownHostException) {
                            view.noInternet(false)
                        }

                    }).let { disposables.addAll(it) }
            }
        }
     }

    fun deleteSession(id: Int) {
        view.viewFullProgress(true)
        api.deleteAppointment(userHolder.userToken!!, id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                view.viewFullProgress(false)
                when (it.status) {
                    Success -> {
                        view.noInternet(true)
                        view.displaySuccessMessage(it.message)
                        view.removeApppintment(id)
                    }
                    InvalidCredentials, InternalServer, MissingParameter,UnAuthorizedAccess -> {
                        view.displayMessage(it.message)
                    }
                }
            }, onError = {
                view.viewFullProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException) {
                    view.noInternet(false)
                }
            }).let { disposables.addAll(it) }
    }


    fun checkInAppointment(appointmentId:Int)
    {
        view.viewFullProgress(true)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("appointment[status]", PATIENT_ARRIVED)
            .build()

        api.checkInAppointment(userHolder.userToken,appointmentId,requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onNext = {
                view.viewFullProgress(false)
                when (it.status) {
                    Success -> {
                        view.noInternet(true)
                        view.displaySuccessCheckInMessage(it.message)
                    }
                    InvalidCredentials, InternalServer, MissingParameter,UnAuthorizedAccess -> {
                        view.displayMessage(it.message)
                    }
                }
            } ,onError = {
                view.viewFullProgress(false)
                it.printStackTrace()

                if (it is UnknownHostException) {
                    view.noInternet(false)
                }
            }).let { disposables.addAll(it) }
    }

    fun resetPage()
    {
        nextPage="1"
    }

    fun safeDispose() {
        disposables.clear()
        disposables.dispose()
    }

    interface View {
        fun displayMessage(mesage: String)
        fun displaySuccessMessage(message: String)
        fun displaySuccessCheckInMessage(message: String)
        fun getSessions(list: ArrayList<BookAppointmentResponse>)
        fun viewFullProgress(isShow: Boolean)
        fun showProgress()
        fun hideProgress()
        fun noInternet(isConnect:Boolean)
        fun removeApppintment(appointmentId:Int)
    }
}