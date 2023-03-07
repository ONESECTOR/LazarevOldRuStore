package com.owlylabs.platform.di.java.modules

import com.owlylabs.platform.di.java.qualifiers.ApiBaseUrl
import com.owlylabs.platform.model.repository.remote.ServerAPI
import dagger.Module
import dagger.Provides
import okhttp3.Headers
import okhttp3.Headers.Companion.toHeaders
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(@ApiBaseUrl baseUrl: String, httpClient: OkHttpClient): ServerAPI {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(ServerAPI::class.java)
    }

    @Provides
    fun getOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    @Provides
    fun getInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val requestBuilder = chain.request().newBuilder()
            val headMap: MutableMap<String, String> =
                HashMap()
            //headMap["Connection"] = "close"
            headMap["Content-Type"] = "application/json"
            //headMap["Accept-Encoding"] = "identity"
            val headers = headMap.toHeaders()
            requestBuilder.headers(headers)
            chain.proceed(requestBuilder.build())
        }
    }
}