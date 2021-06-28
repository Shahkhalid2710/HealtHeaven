package com.applocum.connecttomyhealth.shareddata.endpoints

import com.applocum.connecttomyhealth.ui.addcard.models.CardGlobalResponse
import com.applocum.connecttomyhealth.ui.addcard.models.CardResponse
import com.applocum.connecttomyhealth.ui.allergyhistory.models.AllergyGlobalResponse
import com.applocum.connecttomyhealth.ui.allergyhistory.models.AllergyHistoryGlobalResponse
import com.applocum.connecttomyhealth.ui.appointment.models.BookAppointmentGlobalResponse
import com.applocum.connecttomyhealth.ui.booksession.models.TimeResponse
import com.applocum.connecttomyhealth.ui.changepassword.models.PasswordGlobalResponse
import com.applocum.connecttomyhealth.ui.familyhistory.models.FamilyHistoryGlobalResponse
import com.applocum.connecttomyhealth.ui.familyhistory.models.FamilyHistoryResponse
import com.applocum.connecttomyhealth.ui.investigation.models.InvestigationGlobalResponse
import com.applocum.connecttomyhealth.ui.investigation.models.InvestigationResponse
import com.applocum.connecttomyhealth.ui.medicalhistory.models.MedicalGlobalResponse
import com.applocum.connecttomyhealth.ui.medicalhistory.models.MedicalHistoryGlobalResponse
import com.applocum.connecttomyhealth.ui.mygp.models.GpResponse
import com.applocum.connecttomyhealth.ui.mygp.models.GpServiceGlobalResponse
import com.applocum.connecttomyhealth.ui.mygp.models.SurgeryGlobaResponse
import com.applocum.connecttomyhealth.ui.payment.models.MembershipGlobalResponse
import com.applocum.connecttomyhealth.ui.photoid.models.PhotoIdGlobalResponse
import com.applocum.connecttomyhealth.ui.prescription.models.DocumentGlobalResponse
import com.applocum.connecttomyhealth.ui.profile.models.ProfileProgressGlobalResponse
import com.applocum.connecttomyhealth.ui.securitycheck.models.SecurityGlobalResponse
import com.applocum.connecttomyhealth.ui.settings.models.SettingNotificationGlobalResponse
import com.applocum.connecttomyhealth.ui.settings.models.SettingNotificationResponse
import com.applocum.connecttomyhealth.ui.signup.models.GlobalResponse
import com.applocum.connecttomyhealth.ui.signup.models.SignOutGlobalResponse
import com.applocum.connecttomyhealth.ui.specialists.models.DoctorResponse
import com.applocum.connecttomyhealth.ui.verification.models.OtpGlobalResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AppEndPoint {

    @POST("/api/priory/users/sign_up.json")
    fun signUp(@Body requestBody: RequestBody): Observable<GlobalResponse>

    @POST("/api/priory/users/sign_in.json")
    fun signin(@Body requestBody: RequestBody): Observable<GlobalResponse>

    @DELETE("/api/priory/users/sign_out.json")
    fun signOut(@Header("AUTH_TOKEN") authtoken:String?):Observable<SignOutGlobalResponse>

    @GET("/api/profiles/search.json")
    fun getdoctors(@Header("AUTH_TOKEN") authtoken:String?,
                   @Query("corporate_organization_id") corporateId:Int,
                   @Query("page")page:String):Observable<Response<DoctorResponse>>

    @GET("/api/patients/time_slots.json")
    fun getTimeSlots(@Header("AUTH_TOKEN") authtoken: String?,
                     @Query("date") date:String,
                     @Query("doctor_id") doctorid:Int,
                     @Query("session_type") sessiontype:String,
                     @Query("session_slot") sessionslot:String):Observable<TimeResponse>

    @POST("/api/cards.json")
    fun addCard(@Header("AUTH_TOKEN") authtoken: String?,
                @Body requestBody: RequestBody):Observable<CardGlobalResponse>

    @GET("/api/cards.json")
    fun showCard(@Header("AUTH_TOKEN") authtoken: String?):Observable<CardResponse>

    @DELETE("/api/cards/{id}.json")
    fun deleteCard(@Header("AUTH_TOKEN")authtoken: String?,
                   @Path("id")cardId:Int ):Observable<CardGlobalResponse>

    @GET("/api/priory/patients/{id}.json")
    fun showProfile(@Header("AUTH_TOKEN") authtoken: String?,
                    @Path("id") userid: String?):Observable<GlobalResponse>

    @PATCH("/api/priory/patients/{id}.json")
    fun updateProfile(@Header("AUTH_TOKEN") authtoken: String?,
                      @Path("id") userid: String?,
                      @Body body:RequestBody?):Observable<GlobalResponse>

    @PATCH("/api/users/{id}.json")
    fun updateUser(@Header("AUTH_TOKEN") authtoken: String?,
                   @Path("id") userid: String?,
                   @Body body: MultipartBody):Observable<GlobalResponse>

    @PATCH("/api/priory/patients/{id}.json")
    fun updateDocument(@Header("AUTH_TOKEN") authtoken: String?,
                      @Path("id") userid: String?,
                      @Body body:MultipartBody):Observable<GlobalResponse>

    @DELETE("/api/documents/{id}.json")
    fun deleteDocument(@Header("AUTH_TOKEN") authtoken: String?,
                       @Path("id")documentId:Int):Observable<PhotoIdGlobalResponse>

    @GET("/api/priory/surgeries/search.json")
    fun getGpList(@Query("search")search:String?):Observable<GpServiceGlobalResponse>

    @POST("/api/priory/user_surgeries.json")
    fun addGpService(@Header("AUTH_TOKEN") authtoken: String?,
                     @Body requestBody: RequestBody):Observable<GpResponse>

    @GET("/api/priory/user_surgeries.json")
    fun getGpService(@Header("AUTH_TOKEN") authtoken: String?,
                     @Query("user_id")userId: Int):Observable<SurgeryGlobaResponse>

    @POST("/api/priory/patient/appointments.json")
    fun bookAppointment(@Header("AUTH_TOKEN")authtoken: String?,
                        @Body requestBody: RequestBody):Observable<BookAppointmentGlobalResponse>

    @GET("/api/priory/doctors/appointments.json")
    fun sessions(@Header("AUTH_TOKEN")authtoken: String?,
                        @Query("appointments_type")appointmentType:String,
                        @Query("appointment_corporate_id")corporateId:Int,
                        @Query("page")page:String):Observable<Response<BookAppointmentGlobalResponse>>

    @DELETE("/api/priory/patient/appointments/{id}.json")
    fun deleteAppointment(@Header("AUTH_TOKEN")authtoken: String?,
                          @Path("id")appointmentId:Int ):Observable<BookAppointmentGlobalResponse>

    @POST("/api/priory/users/password/change.json")
    fun changePassword(@Header("AUTH_TOKEN")authtoken: String?,
                       @Body requestBody: RequestBody):Observable<PasswordGlobalResponse>

    @POST("/api/priory/users/forgot_password.json")
    fun forgetPassword(@Body requestBody: RequestBody):Observable<PasswordGlobalResponse>

    @POST("/api/users/security_check.json")
    fun securityCheck(@Header("AUTH_TOKEN")authtoken: String?,
                      @Body requestBody: RequestBody):Observable<SecurityGlobalResponse>

    @GET("/api/consultations/search_snomed_code.json")
    fun getDiseaseList(@Query("search") search:String?):Observable<MedicalGlobalResponse>

    @POST("/api/medical_histories.json")
    fun addMedicalHistory(@Header("AUTH_TOKEN")authtoken: String?,
                          @Header("clinical_token")clinicalToken:String?,
                          @Body requestBody: RequestBody):Observable<MedicalHistoryGlobalResponse>

    @GET("/api/medical_histories.json")
    fun showMedicalHistory(@Header("AUTH_TOKEN")authtoken: String?,
                           @Header("clinical_token")clinicalToken:String?,
                           @Query("user_id")userId:Int,
                           @Query("type")historyType:String,
                           @Query("status")status:String,
                           @Query("corporate_id")corporateId: Int):Observable<MedicalHistoryGlobalResponse>

    @POST("/api/user_allergies.json")
    fun addAllergyHistory(@Header("AUTH_TOKEN")authtoken: String?,
                          @Header("clinical_token")clinicalToken:String?,
                          @Body requestBody: RequestBody):Observable<AllergyGlobalResponse>

    @GET("/api/user_allergies.json")
    fun showAllergyHistory(@Header("AUTH_TOKEN")authtoken: String?,
                           @Header("clinical_token")clinicalToken:String?,
                           @Query("user_id")userId:Int,
                           @Query("type")historyType:String,
                           @Query("status")status:String,
                           @Query("corporate_id")corporateId: Int):Observable<AllergyHistoryGlobalResponse>

    @POST("/api/investigations.json")
    fun addInvestigation(@Header("AUTH_TOKEN")authtoken: String?,
                         @Header("clinical_token")clinicalToken:String?,
                         @Body requestBody: RequestBody):Observable<InvestigationGlobalResponse>

    @GET("/api/investigations.json")
    fun showInvestigation(@Header("AUTH_TOKEN")authtoken: String?,
                          @Header("clinical_token")clinicalToken:String?,
                          @Query("corporate_id")corporateId: Int):Observable<InvestigationResponse>

    @POST("/api/family_histories.json")
    fun addFamilyHistory(@Header("AUTH_TOKEN")authtoken: String?,
                         @Header("clinical_token")clinicalToken:String?,
                         @Body requestBody: RequestBody):Observable<FamilyHistoryGlobalResponse>

    @GET("/api/family_histories.json")
    fun showFamilyHistory(@Header("AUTH_TOKEN")authtoken: String?,
                          @Header("clinical_token")clinicalToken:String?,
                          @Query("corporate_id")corporateId: Int):Observable<FamilyHistoryResponse>

    @GET("/api/documents/list_for_downloads.json")
    fun getDocuments(@Header("AUTH_TOKEN")authtoken: String?,
                     @Query("document_type")documentType:String):Observable<DocumentGlobalResponse>

    @PUT("/api/users/notification_setting.json")
    fun notificationSetting(@Header("AUTH_TOKEN")authtoken: String?,
                            @Body requestBody: RequestBody):Observable<SettingNotificationGlobalResponse>

    @GET("/api/priory/users/utils/show_notification_setting.json")
    fun showNotification(@Header("AUTH_TOKEN")authtoken: String?):Observable<SettingNotificationResponse>

    @POST("/api/customer_support/memberships.json")
    fun addMembership(@Header("AUTH_TOKEN")authtoken: String?,
                      @Body requestBody: RequestBody):Observable<MembershipGlobalResponse>

    @GET("/api/customer_support/memberships.json")
    fun getMembershipList(@Header("AUTH_TOKEN")authtoken: String?,
                          @Query("corporate_organization_id")corporateId: Int):Observable<MembershipGlobalResponse>

    @POST("/api/priory/users/verify_otp/send_otp.json")
    fun sendOtp(@Query("phone") phoneNumber:String,@Query("role") role :String):Observable<OtpGlobalResponse>

    @POST("/api/priory/users/verify_otp.json")
    fun verifyOtp(@Header("AUTH_TOKEN")authtoken: String?,
                  @Body requestBody: RequestBody):Observable<GlobalResponse>

    @GET("/api/priory/patients/track_profile_progress.json")
    fun trackProfileProgress(@Header("AUTH_TOKEN")authtoken: String?):Observable<ProfileProgressGlobalResponse>

}


