package com.applocum.connecttomyhealth

import android.app.Application
import com.applocum.connecttomyhealth.injections.components.DaggerNetworkComponent
import com.applocum.connecttomyhealth.injections.components.NetworkComponent
import com.applocum.connecttomyhealth.injections.modules.UserHolderModule
import com.connectmyhealth.patient.injections.modules.RetrofitModule

class MyApplication:Application() {

    lateinit var component:NetworkComponent
    companion object {
        lateinit var instance: MyApplication
        fun getAppContext(): MyApplication {
            return instance
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerNetworkComponent.builder()
            .retrofitModule(RetrofitModule())
            .userHolderModule(UserHolderModule())
            .build()
    }
}