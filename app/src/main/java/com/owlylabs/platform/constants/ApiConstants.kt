package com.owlylabs.platform.constants

import androidx.annotation.Keep
import com.owlylabs.playerlibrary.FlavorConfigConstants

class ApiConstants {

    @Keep
    companion object{

        const val API_BASE_URL = "http://app.owlylabs.com/"  // link to base url of api
        private val API_VERSION_V1 by lazy { "v1" }

        /** Get structure */
        const val API_GET_PLATFORM_STRUCTURE_JSON = "api4/GetStructureJson"
        /** Get data */
        const val API_GET_PLATFORM_DATA_JSON = "api4/GetContent"
        /**
         * Get udid.
         * If not work try add to end of query?UDID=
         */
        const val API_GET_UDID = "api4/checkUDID"
        /** Register temp user */
        const val API_REGISTER_TEMP_USER = "api4/registertemp"
        /** Get server's time */
        const val API_GET_TIME_URL =  "api4/time"
        /** Download epub */
        const val API_GET_BOOK = "api4/epub"
        /** Get recommended apps */
        const val API_GET_RECOMMENDED_APPS =  "short2/getrecomendation"

        /** Get recommended apps */
        const val API_VALIDATION_GET_LIST_OF_AVAILABLE_SKUS = "android/GetInAppProducts"
        const val API_VALIDATION_GET_SUBSCRIPTION_STATE = "android/GetPurchasesSubscriptions"
        const val API_VALIDATION_ACKNOWLEDGE_SUBSRIPTION = "android/SubscriptionValidation"
        const val API_VALIDATION_CANCEL_SUBSRIPTION = "android/SubscriptionCancel"
        const val API_VALIDATION_SUBSCRIPTION_DATE = "android/SubscriptionValidation"

        const val API_PARAM_CONST_APP_ID = FlavorConfigConstants.API_PARAM_CONST_APP_ID
        val API_PARAM_CONST_VERSION by lazy { API_VERSION_V1 }
        val API_PARAM_CONST_DEVICE by lazy {1}
        val API_PARAM_CONST_DATE by lazy {0L}

        const val API_PARAM_VERSION = "ver"
        const val API_PARAM_APP_ID = "app_id"
        const val API_PARAM_DEVICE = "device"
        const val API_PARAM_DATE = "date"
        const val API_PARAM_UDID = "udid"
        const val API_PARAM_BOOK_ID = "id_book"
        const val API_PARAM_PACKAGE_NAME = "package_name"
        const val API_PARAM_PURCHASE_TOKEN = "purchase_token"
        const val API_PARAM_PRODUCT_ID = "product_id"
        const val API_PARAM_SUBSCRIPTION_ID = "subscription_id"

        const val TAB_TYPE_NEWS = "news"
        const val TAB_TYPE_BOOKS = "books"
        const val TAB_TYPE_AUDIOS = "audios"
        const val TAB_TYPE_VIDEOS = "videos"
        const val TAB_TYPE_ACCOUNT = "account"
    }
}