package com.applocum.connecttomyhealth.injections.components

import com.applocum.connecttomyhealth.injections.modules.UserHolderModule
import com.applocum.connecttomyhealth.ui.addcard.activities.AddCardActivity
import com.applocum.connecttomyhealth.ui.addsymptoms.activities.AddSymptomActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.fragments.ActiveAllergyFragment
import com.applocum.connecttomyhealth.ui.allergyhistory.activities.AddAllergyActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.activities.AllergyHistoryActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.fragments.PastAllergyFragment
import com.applocum.connecttomyhealth.ui.appointment.fragments.AppointmentFragment
import com.applocum.connecttomyhealth.ui.appointment.fragments.PastSessionAppointmentFragment
import com.applocum.connecttomyhealth.ui.appointment.fragments.UpcomingSessionApointmentFragment
import com.applocum.connecttomyhealth.ui.booksession.fragments.AboutSpecialistFragment
import com.applocum.connecttomyhealth.ui.booksession.fragments.AvailabilityFragment
import com.applocum.connecttomyhealth.ui.booksession.activities.BookSessionActivity
import com.applocum.connecttomyhealth.ui.booksession.activities.SessionBookActivity
import com.applocum.connecttomyhealth.ui.changepassword.activities.ChangePasswordActivity
import com.applocum.connecttomyhealth.ui.clinicalrecords.activities.ClinicalRecordsActivity
import com.applocum.connecttomyhealth.ui.confirmbooking.activities.ConfirmBookingActivity
import com.applocum.connecttomyhealth.ui.familyhistory.activities.AddFamilyHistoryActivity
import com.applocum.connecttomyhealth.ui.familyhistory.activities.FamilyHistoryActivity
import com.applocum.connecttomyhealth.ui.fitnote.activities.FitNoteActivity
import com.applocum.connecttomyhealth.ui.forgotpassword.activities.ForgotPasswordActivity
import com.applocum.connecttomyhealth.ui.home.fragments.HomeFragment
import com.applocum.connecttomyhealth.ui.investigation.activities.AddInvestigationActivity
import com.applocum.connecttomyhealth.ui.investigation.activities.InvestigationActivity
import com.applocum.connecttomyhealth.ui.login.activities.LoginActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.fragments.ActiveMedicalHistoryFragment
import com.applocum.connecttomyhealth.ui.medicalhistory.activities.AddMedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.activities.MedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.fragments.PastMedicalHistoryFragment
import com.applocum.connecttomyhealth.ui.mydownloads.activities.MyDownloadsActivity
import com.applocum.connecttomyhealth.ui.mygp.activities.AddGPServiceActivity
import com.applocum.connecttomyhealth.ui.mygp.activities.GpServiceActivity
import com.applocum.connecttomyhealth.ui.notification.fragments.NotificationFragment
import com.applocum.connecttomyhealth.ui.othernotes.activities.OtherNoteActivity
import com.applocum.connecttomyhealth.ui.payment.activities.*
import com.applocum.connecttomyhealth.ui.personaldetails.activities.PersonalDetailsActivity
import com.applocum.connecttomyhealth.ui.photoid.activities.PhotoIdActivity
import com.applocum.connecttomyhealth.ui.prescription.activities.PrescriptionActivity
import com.applocum.connecttomyhealth.ui.profile.fragments.ProfileFragment
import com.applocum.connecttomyhealth.ui.profiledetails.activities.ProfileDetailsActivity
import com.applocum.connecttomyhealth.ui.referral.activities.ReferralActivity
import com.applocum.connecttomyhealth.ui.securitycheck.activities.SecurityActivity
import com.applocum.connecttomyhealth.ui.settings.activities.SettingActivity
import com.applocum.connecttomyhealth.ui.signup.activities.SignupActivity
import com.applocum.connecttomyhealth.ui.specialists.activities.SpecialistsActivity
import com.applocum.connecttomyhealth.ui.splashscreen.activities.SplashScreenActivity
import com.applocum.connecttomyhealth.ui.verification.activities.VerificationActivity
import com.applocum.connecttomyhealth.ui.verificationdocument.activities.*
import com.connectmyhealth.patient.injections.modules.RetrofitModule
import com.connectmyhealth.patient.shareddata.interceptors.GlobalResponseInterceptor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(RetrofitModule::class),(UserHolderModule::class)])
interface NetworkComponent {
    fun inject(activity: LoginActivity)

    fun inject(activity: SignupActivity)

    fun inject(activity: VerificationActivity)

    fun inject(activity: ForgotPasswordActivity)

    fun inject(activity: MyDownloadsActivity)

    fun inject(activity: MemberShipActivity)

    fun inject(activity: PhotoIdActivity)

    fun inject(activity: PrescriptionActivity)

    fun inject(activity: FitNoteActivity)

    fun inject(activity: ReferralActivity)

    fun inject(activity: OtherNoteActivity)

    fun inject(activity: PersonalDetailsActivity)

    fun inject(activity: ClinicalRecordsActivity)

    fun inject(activity: SettingActivity)


    fun inject(activity: FamilyHistoryActivity)

    fun inject(activity: AllergyHistoryActivity)

    fun inject(activity: AddAllergyActivity)

    fun inject(activity: AddMedicalHistoryActivity)

    fun inject(activity: InvestigationActivity)

    fun inject(activity: AddInvestigationActivity)

    fun inject(activity: ProfileDetailsActivity)

    fun inject(activity: SpecialistsActivity)

    fun inject(activity: BookSessionActivity)

    fun inject(activity: SessionBookActivity)

    fun inject(activity: AddCardActivity)

    fun inject(activity: AddFamilyHistoryActivity)

    fun inject(activity: ConfirmBookingActivity)

    fun inject(activity: PaymentActivity)

    fun inject(activity:VerifyIdentityActivity)

    fun inject(activity:ValidPassportActivity)

    fun inject(activity:ValidationValidPassportActivity)

    fun inject(activity:VerifiedActivity)

    fun inject(activity: AddCodeActivity)

    fun inject(activity: AddSymptomActivity)

    fun inject(activity: PaymentShowActivity)

    fun inject(activity: SplashScreenActivity)

    fun inject(activity: GpServiceActivity)

    fun inject(activity: AddGPServiceActivity)

    fun inject(activity: ChangePasswordActivity)

    fun inject(activity: PaymentMethodActivity)

    fun inject(activity: SecurityActivity)

    fun inject(activity: MedicalHistoryActivity)

    fun inject(fragment: HomeFragment)

    fun inject(fragment: ProfileFragment)

    fun inject(fragment: NotificationFragment)

    fun inject(fragment: AppointmentFragment)

    fun inject(fragment: AboutSpecialistFragment)

    fun inject(fragment: AvailabilityFragment)

    fun inject(fragment: ActiveAllergyFragment)

    fun inject(fragment: PastAllergyFragment)

    fun inject(fragment: ActiveMedicalHistoryFragment)

    fun inject(fragment: PastMedicalHistoryFragment)

    fun inject(fragment: UpcomingSessionApointmentFragment)

    fun inject(fragment: PastSessionAppointmentFragment)

    fun inject(intercepter:GlobalResponseInterceptor)

}