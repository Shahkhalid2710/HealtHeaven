package com.applocum.connecttomyhealth.commons.globals

class ErrorCodes {
    companion object
    {
        const val Success = 200
        const val SessionInvalid = 950
        const val InvalidCredentials = 808
        const val InternalServer = 500
        const val AlreadyExist=805
        const val NotFound=404
        const val MissingParameter=801
        const val InvalidOtp=803
        const val OtpTryAgain=813
        const val UnsuccessfulAttemptt=403
    }
}