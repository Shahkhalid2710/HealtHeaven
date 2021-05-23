package com.applocum.connecttomyhealth.ui.payment.models
import com.google.gson.annotations.SerializedName

data class MembershipResponse (
	@SerializedName("id") val id : Int,
	@SerializedName("status") val status : String,
	@SerializedName("user_id") val user_id : Int,
	@SerializedName("membership_plan_id") val membership_plan_id : Int,
	@SerializedName("start_date") val start_date : String,
	@SerializedName("end_date") val end_date : String,
	@SerializedName("membership_code") val membership_code : String,
	@SerializedName("cancelled_at") val cancelled_at : String,
	@SerializedName("max_usage_count") val max_usage_count : Int,
	@SerializedName("consultations_usage_count") val consultations_usage_count : Int,
	@SerializedName("fair_usage_exceeded") val fair_usage_exceeded : Boolean,
	@SerializedName("will_fair_usage_exceed") val will_fair_usage_exceed : Boolean,
	@SerializedName("installments_count") val installments_count : Int,
	@SerializedName("last_installment_paid_at") val last_installment_paid_at : String,
	@SerializedName("current_period_start_date") val current_period_start_date : String,
	@SerializedName("current_period_end_date") val current_period_end_date : String,
	@SerializedName("auto_renew") val auto_renew : Boolean,
	@SerializedName("exceeded_fair_usage_price") val exceeded_fair_usage_price : Double,
	@SerializedName("corporate_id") val corporate_id : Int,
	@SerializedName("corporate_name") val corporate_name : String,
	@SerializedName("is_default") val is_default : Boolean,
	@SerializedName("is_first_membeship_appointment") val is_first_membeship_appointment : Boolean,
	@SerializedName("price") val price : Double,
	@SerializedName("membership_plan") val membership_plan : MembershipPlan
)