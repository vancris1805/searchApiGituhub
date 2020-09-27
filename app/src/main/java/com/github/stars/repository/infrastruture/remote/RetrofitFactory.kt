package com.github.stars.repository.infrastruture.remote

import com.github.stars.BuildConfig
import com.github.stars.repository.infrastruture.HeadersInterceptor
import com.github.stars.repository.infrastruture.IRetrofitFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitFactory : IRetrofitFactory {

    fun getRetrofit(): Retrofit {

        return buildRetrofit(
            "https://api.github.com/",
        interceptors = listOf(
            HeadersInterceptor(),
            if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) else null
        ))
        //HEADERS)
    }
}
