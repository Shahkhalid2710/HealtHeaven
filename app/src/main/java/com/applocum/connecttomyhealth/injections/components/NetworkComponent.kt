package com.applocum.connecttomyhealth.injections.components

import com.applocum.connecttomyhealth.injections.modules.UserHolderModule
import com.applocum.connecttomyhealth.ui.allergyhistory.ActiveAllergyFragment
import com.applocum.connecttomyhealth.ui.allergyhistory.AddAllergyActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.AllergyHistoryActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.PastAllergyFragment
import com.applocum.connecttomyhealth.ui.appointment.AppointmentFragment
import com.applocum.connecttomyhealth.ui.clinicalrecords.ClinicalRecordsActivity
import com.applocum.connecttomyhealth.ui.familyhistory.FamilyHistoryActivity
import com.applocum.connecttomyhealth.ui.forgotpassword.ForgotPasswordActivity
import com.applocum.connecttomyhealth.ui.home.HomeFragment
import com.applocum.connecttomyhealth.ui.investigation.AddInvestigationActivity
import com.applocum.connecttomyhealth.ui.investigation.InvestigationActivity
import com.applocum.connecttomyhealth.ui.login.LoginActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.AddMedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.mydownloads.MyDownloadsActivity
import com.applocum.connecttomyhealth.ui.notification.NotificationFragment
import com.applocum.connecttomyhealth.ui.personaldetails.PersonalDetailsActivity
import com.applocum.connecttomyhealth.ui.profile.ProfileFragment
import com.applocum.connecttomyhealth.ui.profiledetails.ProfileDetailsActivity
import com.applocum.connecttomyhealth.ui.settings.SettingActivity
import com.applocum.connecttomyhealth.ui.signup.SignupActivity
import com.applocum.connecttomyhealth.ui.verification.VerificationActivity
import com.connectmyhealth.patient.injections.modules.RetrofitModule
import com.connectmyhealth.patient.shareddata.interceptors.GlobalResponseInterceptor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(RetrofitModule::class),(UserHolderModule::class)])
interface NetworkComponent {
    fun inject(activity:LoginActivity)

    fun inject(activity:SignupActivity)

    fun inject(activity:VerificationActivity)

    fun inject(activity:ForgotPasswordActivity)

    fun inject(activity:MyDownloadsActivity)

    fun inject(activity:PersonalDetailsActivity)

    fun inject(activity:ClinicalRecordsActivity)

    fun inject(activity:SettingActivity)

    fun inject(activity:FamilyHistoryActivity)

    fun inject(activity:AllergyHistoryActivity)

    fun inject(activity:AddAllergyActivity)

    fun inject(activity:AddMedicalHistoryActivity)

    fun inject(activity:InvestigationActivity)

    fun inject(activity:AddInvestigationActivity)

    fun inject(activity:ProfileDetailsActivity)

    fun inject(fragment:HomeFragment)

    fun inject(fragment:ProfileFragment)

    fun inject(fragment:NotificationFragment)

    fun inject(fragment:AppointmentFragment)

    fun inject(fragment:ActiveAllergyFragment)

    fun inject(fragment:PastAllergyFragment)

    fun inject(intercepter:GlobalResponseInterceptor)
}