package com.owlylabs.platform.model.repository.remote

import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.model.repository.remote.callbacks.content.ContentCallback
import com.owlylabs.platform.model.repository.remote.callbacks.hash.HashCallback
import com.owlylabs.platform.model.repository.remote.callbacks.structure.StructureCallback
import com.owlylabs.platform.model.repository.remote.callbacks.timestamp.ServerTimstampCallback
import com.owlylabs.platform.model.repository.remote.callbacks.udid.UdidCallback
import com.owlylabs.platform.model.repository.remote.callbacks.recommended.RecommendedAppsResponse
import com.owlylabs.platform.model.repository.remote.callbacks.billing.get_skus.GetSkusCallback
import com.owlylabs.platform.model.repository.remote.callbacks.billing.subscription_state.SubStateCallback
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerAPI {
    @GET(ApiConstants.API_GET_PLATFORM_STRUCTURE_JSON)
    fun downloadStructureBlocking(
        @Query(ApiConstants.API_PARAM_VERSION) apiVersion: String
    ): Call<StructureCallback>

    @GET(ApiConstants.API_GET_PLATFORM_DATA_JSON)
    fun downloadContentBlocking(
        @Query(ApiConstants.API_PARAM_APP_ID) appId: Int,
        @Query(ApiConstants.API_PARAM_DATE) date: Long
    ): Call<ContentCallback>

    @GET(ApiConstants.API_GET_UDID)
    fun getUdidBlocking(): Call<UdidCallback>

    @GET(ApiConstants.API_REGISTER_TEMP_USER)
    fun registerUserBlocking(
        @Query(ApiConstants.API_PARAM_APP_ID) appId: Int,
        @Query(ApiConstants.API_PARAM_UDID) udid: String
    ): Call<HashCallback>

    @GET(ApiConstants.API_GET_BOOK)
    /*    @Headers(
    "Content-Type: application/json",
    "charset: utf-8"
     )*/
    fun downloadBookBlocking(
        @Query(value = ApiConstants.API_PARAM_BOOK_ID, encoded = true ) bookId: String
    ): Call<ResponseBody>

    @GET(ApiConstants.API_GET_TIME_URL)
    fun getServerTimestampBlocking(): Call<ServerTimstampCallback>

    @GET(ApiConstants.API_GET_TIME_URL)
    fun getServerTimestampObservable(): Call<ServerTimstampCallback>

    @GET(ApiConstants.API_GET_RECOMMENDED_APPS)
    fun getRecommendedAppsBlocking(
        @Query(ApiConstants.API_PARAM_DATE) date: Long,
        @Query(ApiConstants.API_PARAM_DEVICE) device: Int,
        @Query(ApiConstants.API_PARAM_APP_ID) appId: Int
    ): Call<RecommendedAppsResponse>

    @GET(ApiConstants.API_VALIDATION_GET_LIST_OF_AVAILABLE_SKUS)
    fun getSkusAvailableForPurchaseSingle(
        @Query(ApiConstants.API_PARAM_PACKAGE_NAME) date: String
    ): Single<GetSkusCallback>

    @GET(ApiConstants.API_VALIDATION_GET_SUBSCRIPTION_STATE)
    fun getSubscriptionInfo(
        @Query(ApiConstants.API_PARAM_PACKAGE_NAME) packageName: String,
        @Query(ApiConstants.API_PARAM_PRODUCT_ID) productId: String,
        @Query(ApiConstants.API_PARAM_PURCHASE_TOKEN) token: String
    ): Single<SubStateCallback>

    @GET(ApiConstants.API_VALIDATION_ACKNOWLEDGE_SUBSRIPTION)
    fun acknowledgeSubscription(
        @Query(ApiConstants.API_PARAM_PACKAGE_NAME) date: String,
        @Query(ApiConstants.API_PARAM_SUBSCRIPTION_ID) productId: String,
        @Query(ApiConstants.API_PARAM_PURCHASE_TOKEN) token: String
    ): Single<Response<ResponseBody>>

    @GET(ApiConstants.API_VALIDATION_CANCEL_SUBSRIPTION)
    fun cancelSubscription(
        @Query(ApiConstants.API_PARAM_PACKAGE_NAME) date: String,
        @Query(ApiConstants.API_PARAM_SUBSCRIPTION_ID) productId: String,
        @Query(ApiConstants.API_PARAM_PURCHASE_TOKEN) token: String
    ): Single<Response<ResponseBody>>
}