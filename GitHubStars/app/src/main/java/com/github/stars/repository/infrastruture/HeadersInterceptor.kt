package com.github.stars.repository.infrastruture

import okhttp3.Interceptor
import okhttp3.Response

private const val HEADER_KEY_CONTENT_TYPE = "content-Type"
private const val HEADER_VALUE_CONTENT_TYPE = "application/json; charset=utf-8"

class HeadersInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        requestBuilder.addHeader(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_CONTENT_TYPE)
        return chain.proceed(requestBuilder.build())
    }
}