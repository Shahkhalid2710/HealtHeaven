package com.applocum.connecttomyhealth.injections.modules

import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.commons.globals.PrefHelper
import com.applocum.connecttomyhealth.extensions.getPrefInstance
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserHolderModule {

    @Provides
    @Singleton
    fun provideTokenHolder(): UserHolder =
        UserHolder(MyApplication.instance.getPrefInstance(PrefHelper.prefBasic))

}