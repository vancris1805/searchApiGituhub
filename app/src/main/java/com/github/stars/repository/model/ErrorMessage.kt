package com.github.stars.repository.model

import androidx.annotation.StringRes

data class ErrorMessage(val code: Int, val message: String?, @StringRes val messageResource: Int?) {
    companion object {
        const val SOCKET_TIMEOUT_CODE = 11

        fun withResource(code: Int, @StringRes resource: Int) = ErrorMessage(code, null, resource)
    }
}