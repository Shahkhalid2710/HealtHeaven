package com.applocum.connecttomyhealth.ui.settings.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SettingNotification (

	@SerializedName("is_notify_by_sms") val is_notify_by_sms : Boolean,
	@SerializedName("is_notify_by_email") val is_notify_by_email : Boolean,
	@SerializedName("is_notify_by_phone") val is_notify_by_phone : Boolean,
	@SerializedName("is_notify_by_push_notification") val is_notify_by_push_notification : Boolean,
	@SerializedName("is_notify_by_medication_reminder") val is_notify_by_medication_reminder : Boolean,
	@SerializedName("is_notify_by_postbox") val is_notify_by_postbox : Boolean,
	@SerializedName("is_notify_by_third_party") val is_notify_by_third_party : Boolean
):Serializable