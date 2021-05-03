package com.applocum.connecttomyhealth.shareddata.endpoints

import com.applocum.connecttomyhealth.ui.addcard.models.CardGlobalResponse
import com.applocum.connecttomyhealth.ui.addcard.models.CardResponse
import com.applocum.connecttomyhealth.ui.booksession.models.TimeResponse
import com.applocum.connecttomyhealth.ui.exemptions.models.ExemptionGlobalResponse
import com.applocum.connecttomyhealth.ui.mygp.models.GpServiceGlobalResponse
import com.applocum.connecttomyhealth.ui.signup.models.GlobalResponse
import com.applocum.connecttomyhealth.ui.specialists.models.DoctorResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface AppEndPoint {

    @POST("/api/priory/users/sign_up.json")
    fun signUp(@Body requestBody: RequestBody): Observable<GlobalResponse>

    @POST("/api/priory/users/sign_in.json")
    fun signin(@Body requestBody: RequestBody): Observable<GlobalResponse>

    @GET("/api/profiles/search.json")
    fun getdoctors(@Header("AUTH_TOKEN") authtoken:String?,@Query("corporate_organization_id") corporateId:Int):Observable<DoctorResponse>

    @GET("/api/patients/time_slots.json")
    fun getTimeSlots(@Header("AUTH_TOKEN") authtoken: String?,@Query("date") date:String,@Query("doctor_id") doctorid:Int,@Query("session_type") sessiontype:String,@Query("session_slot") sessionslot:String):Observable<TimeResponse>

    @POST("/api/priory/cards.json")
    fun addCard(@Header("AUTH_TOKEN") authtoken: String?,@Body requestBody: RequestBody):Observable<CardGlobalResponse>

    @GET("/api/cards.json")
    fun showCard(@Header("AUTH_TOKEN") authtoken: String?):Observable<CardResponse>

    @GET("/api/priory/patients/{id}.json")
    fun showProfile(@Header("AUTH_TOKEN") authtoken: String?, @Path("id") userid: String?):Observable<GlobalResponse>

    @PATCH("/api/priory/patients/{id}.json")
    fun updateProfile(@Header("AUTH_TOKEN") authtoken: String?, @Path("id") userid: String?, @Body body:RequestBody?):Observable<GlobalResponse>

    @GET(" /api/priory/surgeries/search.json")
    fun getGpList(@Query("search")search:String?):Observable<GpServiceGlobalResponse>



}
