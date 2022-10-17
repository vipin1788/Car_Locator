package com.karsync.karsyncservices.utils

/**
 * Purpose - Utility class for Constant values.
 *
 * @author Jyoti Verma
 */
object KarSyncConstants {

    const val STATUS_API = "/getSubAccountUser"
    const val UPDATE_STATUS_API = "/updatePending"
    const val UNAUTHORISE_API = "/api/v1/public/**"
    const val PUBLIC_SECURE_URL = "/api/v1/public/*.*"
    const val PUBLIC_URL = "/api/v1/public"
    const val AUTHORISE_CONTROLLER = "/api/v1/authorise"
    const val CLIENT_URL = "/api/v1/client"
    const val DEALER_URL = "/api/v1/dealer"
    const val MASTER_URL = "/api/v1/master"
    const val ADMIN_URL = "/api/v1/admin"
    val SWAGGER_URL = arrayOf(UNAUTHORISE_API, "/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**", "/tw", "/images/**")


}
