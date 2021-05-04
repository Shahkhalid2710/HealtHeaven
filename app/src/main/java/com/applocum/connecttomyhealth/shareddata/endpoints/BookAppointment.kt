package com.applocum.connecttomyhealth.shareddata.endpoints

class BookAppointment {
    var appointmentGp: String = ""
    var appointmentDate: String = ""
    var appointmentTime: String = ""
    var appointmentSlot: String = ""
    var appointmentDateTime: String = ""
    var pickedFilePath: String = ""
    var appointmentReason: String = ""
    var appointmentStartTime: String = ""
    var appointmentEndTime: String = ""
    var doctorId: String? = ""
    var offlineAppointment: Boolean = false
    var appointmentType: String = ""
    var therapistId: Int = -1
    var therapistName: String = ""
    var therapistAddress: String? = ""
    var corporateId: Int = -1
    var isRecurring: Boolean = false
    var recurringType: String? = null
    var recurringSessionCount: String? = null
    var recurringMonthDate: String? = null
    var recurringWeekDays: String? = null
    var discountCode: String? = null
    var memberShipCode: String? = null
}