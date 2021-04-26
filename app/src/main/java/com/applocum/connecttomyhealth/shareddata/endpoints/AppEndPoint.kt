package com.applocum.connecttomyhealth.shareddata.endpoints

import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppEndPoint {

    @POST("/api/users/sign_up.json")
    fun signUp(@Body requestBody: RequestBody): Observable<com.applocum.connecttomyhealth.ui.signup.models.GlobalResponse>

    @POST("/api/users/sign_in.json")
    fun signin(@Body requestBody: RequestBody): Observable<com.applocum.connecttomyhealth.ui.signup.models.GlobalResponse>

    @GET("/api/profiles/search.json")
    fun getdoctors():Observable<com.applocum.connecttomyhealth.ui.specialists.models.GlobalResponse>

}