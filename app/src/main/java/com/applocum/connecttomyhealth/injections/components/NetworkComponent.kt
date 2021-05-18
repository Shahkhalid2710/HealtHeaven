package com.applocum.connecttomyhealth.injections.components

import com.applocum.connecttomyhealth.injections.modules.UserHolderModule
import com.applocum.connecttomyhealth.ui.addcard.AddCardActivity
import com.applocum.connecttomyhealth.ui.addsymptoms.AddSymptomActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.ActiveAllergyFragment
import com.applocum.connecttomyhealth.ui.allergyhistory.AddAllergyActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.AllergyHistoryActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.PastAllergyFragment
import com.applocum.connecttomyhealth.ui.appointment.AppointmentFragment
import com.applocum.connecttomyhealth.ui.appointment.PastSessionAppointmentFragment
import com.applocum.connecttomyhealth.ui.appointment.UpcomingSessionApointmentFragment
import com.applocum.connecttomyhealth.ui.booksession.AboutSpecialistFragment
import com.applocum.connecttomyhealth.ui.booksession.AvailabilityFragment
import com.applocum.connecttomyhealth.ui.booksession.BookSessionActivity
import com.applocum.connecttomyhealth.ui.booksession.SessionBookActivity
import com.applocum.connecttomyhealth.ui.changepassword.ChangePasswordActivity
import com.applocum.connecttomyhealth.ui.clinicalrecords.ClinicalRecordsActivity
import com.applocum.connecttomyhealth.ui.confirmbooking.ConfirmBookingActivity
import com.applocum.connecttomyhealth.ui.familyhistory.FamilyHistoryActivity
import com.applocum.connecttomyhealth.ui.forgotpassword.ForgotPasswordActivity
import com.applocum.connecttomyhealth.ui.home.HomeFragment
import com.applocum.connecttomyhealth.ui.investigation.AddInvestigationActivity
import com.applocum.connecttomyhealth.ui.investigation.InvestigationActivity
import com.applocum.connecttomyhealth.ui.login.LoginActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.ActiveMedicalHistoryFragment
import com.applocum.connecttomyhealth.ui.medicalhistory.AddMedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.MedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.PastMedicalHistoryFragment
import com.applocum.connecttomyhealth.ui.mydownloads.MyDownloadsActivity
import com.applocum.connecttomyhealth.ui.mygp.AddGPServiceActivity
import com.applocum.connecttomyhealth.ui.mygp.GpServiceActivity
import com.applocum.connecttomyhealth.ui.mygp.MyGPActivity
import com.applocum.connecttomyhealth.ui.notification.NotificationFragment
import com.applocum.connecttomyhealth.ui.payment.AddCodeActivity
import com.applocum.connecttomyhealth.ui.payment.PaymentActivity
import com.applocum.connecttomyhealth.ui.payment.PaymentMethodActivity
import com.applocum.connecttomyhealth.ui.payment.PaymentShowActivity
import com.applocum.connecttomyhealth.ui.personaldetails.PersonalDetailsActivity
import com.applocum.connecttomyhealth.ui.profile.ProfileFragment
import com.applocum.connecttomyhealth.ui.profiledetails.ProfileDetailsActivity
import com.applocum.connecttomyhealth.ui.securitycheck.SecurityActivity
import com.applocum.connecttomyhealth.ui.settings.SettingActivity
import com.applocum.connecttomyhealth.ui.signup.SignupActivity
import com.applocum.connecttomyhealth.ui.specialists.SpecialistsActivity
import com.applocum.connecttomyhealth.ui.splashscreen.SplashScreenActivity
import com.applocum.connecttomyhealth.ui.verification.VerificationActivity
import com.applocum.connecttomyhealth.ui.verificationdocument.activities.*
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

    fun inject(activity:SpecialistsActivity)

    fun inject(activity:BookSessionActivity)

    fun inject(activity:SessionBookActivity)

    fun inject(activity:AddCardActivity)

    fun inject(activity:ConfirmBookingActivity)

    fun inject(activity:PaymentActivity)

    fun inject(activity:VerifyIdentityActivity)

    fun inject(activity:ValidPassportActivity)

    fun inject(activity:ValidationValidPassportActivity)

    fun inject(activity:UkDrivingLicenseActivity)

    fun inject(activity:ValidationValidUKDrivingLicenseActivity)

    fun inject(activity:VerifiedActivity)

    fun inject(activity:AddCodeActivity)

    fun inject(activity:AddSymptomActivity)

    fun inject(activity:PaymentShowActivity)

    fun inject(activity:SplashScreenActivity)

    fun inject(activity:GpServiceActivity)

    fun inject(activity:AddGPServiceActivity)

    fun inject(activity:MyGPActivity)

    fun inject(activity:ChangePasswordActivity)

    fun inject(activity:PaymentMethodActivity)

    fun inject(activity:SecurityActivity)

    fun inject(activity:MedicalHistoryActivity)

    fun inject(fragment:HomeFragment)

    fun inject(fragment:ProfileFragment)

    fun inject(fragment:NotificationFragment)

    fun inject(fragment:AppointmentFragment)

    fun inject(fragment:AboutSpecialistFragment)

    fun inject(fragment:AvailabilityFragment)

    fun inject(fragment:ActiveAllergyFragment)

    fun inject(fragment:PastAllergyFragment)

    fun inject(fragment:ActiveMedicalHistoryFragment)

    fun inject(fragment:PastMedicalHistoryFragment)

    fun inject(fragment:UpcomingSessionApointmentFragment)

    fun inject(fragment:PastSessionAppointmentFragment)

    fun inject(intercepter:GlobalResponseInterceptor)

}