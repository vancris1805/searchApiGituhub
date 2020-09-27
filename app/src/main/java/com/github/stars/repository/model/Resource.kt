package com.github.stars.repository.model

import com.github.stars.R
import java.net.HttpURLConnection

sealed class Resource<out T : Any> {
    data class Success<T : Any>(val data: T) : Resource<T>()
    data class Error(val errorData: ErrorMessage) : Resource<Nothing>()
    companion object {
        fun genericError(): Error {
            val message = ErrorMessage.withResource(HttpURLConnection.HTTP_INTERNAL_ERROR, R.string.generic_error_message)
            return Error(message)
        }
        fun noContent(): Error {
            val message = ErrorMessage.withResource(HttpURLConnection.HTTP_NO_CONTENT, R.string.no_content)
            return Error(message)
        }
    }
}