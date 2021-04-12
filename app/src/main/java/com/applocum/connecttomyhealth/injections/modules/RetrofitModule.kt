package com.connectmyhealth.patient.injections.modules


import com.applocum.connecttomyhealth.BuildConfig
import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Retrofit Module is single instance of all the Retrofit init functions and endpoints
 */
@Module
class RetrofitModule {

    /**
     * Initialize the logging interceptors to set in Retrofit instance
     * TODO develop another component for default read Timeout
     */
    @Provides
    @Singleton
    fun provideHttpLogging(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return OkHttpClient.Builder()
                .addNetworkInterceptor(logging)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
    }

    /**
     * Initialize the Retrofit instance
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(okHttpClient)
            .build()

    /**
     * Initialize the single instance of App URL's Endpoint
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): AppEndPoint = retrofit.create(
        AppEndPoint::class.java)


    /**
     * Below methods are defined for Without Logout Interceptor. And its used for Downloading file
     * from URL. logout Interceptor parsing model create issue while downloading file from URL.
     */

}
