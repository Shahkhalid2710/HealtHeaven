package com.connectmyhealth.patient.shareddata.interceptors

import android.annotation.SuppressLint
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.commons.globals.ErrorCodes.Companion.SessionInvalid
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.signup.models.GlobalResponse
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject


@SuppressLint("Registered")
/**
 *  Logout Interceptor
 */
class GlobalResponseInterceptor : BaseActivity(), Interceptor {

    @Inject
    lateinit var userHolder: UserHolder

    init {
        MyApplication.instance.component.inject(this)
    }

    /**
     * no need to implement
     */

    /**
     * no need to implement
     */

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        try {
            val response: Response = chain.proceed(request)
            val globJson = response.body!!.string()
            val globalResponse: GlobalResponse? =
                Gson().fromJson(globJson, GlobalResponse::class.java)


            when (globalResponse?.status) {
                SessionInvalid -> {
                    runOnUiThread {
                        val email = userHolder.userEmail
                        userHolder.clearUserData(
                            "", "", "", "", "",
                            ""
                        ,"")
                    }
                }
            }

            // Re-create the response before returning it because body can be read only once
            return response.newBuilder()
                .body(ResponseBody.create(response.body!!.contentType(), globJson)).build()


        } catch (e: Exception) {
            e.printStackTrace()
            if ((e is IOException && e.localizedMessage == "Canceled") || (e is UnknownHostException)) {
            } else {
            }
        }

        return chain.proceed(request)!!
    }

    override fun getLayoutResourceId(): Int = 0
}