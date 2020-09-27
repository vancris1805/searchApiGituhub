package com.github.stars.repository.infrastruture

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit


private const val CONNECTION_TIMEOUT = 1L
private const val READ_TIMEOUT = 1L
private const val WRITE_TIMEOUT = 15L

interface IRetrofitFactory {
    fun buildRetrofit(
        host: String,
        interceptors: List<Interceptor?> = emptyList(),
    ): Retrofit {

        val okHttpClient = with(OkHttpClient.Builder()) {
            interceptors.filterNotNull().forEach { addInterceptor(it) }
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MINUTES)
            readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            build()
        }

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(host)
            .client(okHttpClient)
            .build()
    }
}