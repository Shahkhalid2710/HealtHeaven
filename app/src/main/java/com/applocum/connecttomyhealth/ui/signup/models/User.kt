package com.applocum.connecttomyhealth.ui.signup.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id : Int,
    @SerializedName("email") val email : String,
    @SerializedName("first_name") val firstName : String,
    @SerializedName("last_name") val lastName : String,
    @SerializedName("phone") val phone : Int,
    @SerializedName("user_confirmed") val userConfirmed : Boolean,
    @SerializedName("is_phone_verified") val isPhoneVerified : Boolean,
    @SerializedName("role") val role : String,
    @SerializedName("auth_token") val authToken : String,
    @SerializedName("address") val address : String,
    @SerializedName("address_detail") val addressDetail : String,
    @SerializedName("status") val status : String,
    @SerializedName("image") val image : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("gmc") val gmc : String,
    @SerializedName("country_code") val countryCode : Int,
    @SerializedName("phone_number") val phoneNumber : Int,
    @SerializedName("profile") val profile : Profile,
    @SerializedName("organization") val organization : Organization,
    @SerializedName("created_at") val createdAt : String,
    @SerializedName("security_question") val securityQuestion : String,
    @SerializedName("security_answer") val securityAnswer : String,
    @SerializedName("employee_code") val employeeCode : String,
    @SerializedName("documents") val documents : String,
    @SerializedName("consulting_now") val consultingNow : String,
    @SerializedName("incomplete_inform_gp_count") val incompleteInformGpCount : String,
    @SerializedName("surgery") val surgery : String,
    @SerializedName("is_verify_docs_exist") val isVerifyDocsExist : Boolean,
    @SerializedName("qr_code") val qrCode : String,
    @SerializedName("email_verified") val emailVerified : Boolean,
    @SerializedName("is_corporate_employee") val isCorporateEmployee : Boolean,
    @SerializedName("corporate_id") val corporateId : Int,
    @SerializedName("corporate_name") val corporateName : String,
    @SerializedName("is_card_present") val is_cardPresent : Boolean,
    @SerializedName("country") val country : String,
    @SerializedName("nmc") val nmc : String,
    @SerializedName("days_left_for_current_password") val daysLeftForCurrentPassword : Int,
    @SerializedName("password_expired") val passwordExpired : Boolean,
    @SerializedName("password_expiry_due") val passwordExpiryDue : Boolean,
    @SerializedName("is_account_locked") val isAccountLocked : Boolean,
    @SerializedName("is_reset_password_locked") val isResetPasswordLocked : Boolean,
    @SerializedName("all_roles") val allRoles : List<String>,
    @SerializedName("child") val child : List<String>,
    @SerializedName("cart_items") val cartItems : List<String>
)