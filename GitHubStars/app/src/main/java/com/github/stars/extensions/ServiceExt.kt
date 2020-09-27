package com.github.stars.extensions

import com.github.stars.R
import com.github.stars.repository.model.ErrorMessage.Companion.SOCKET_TIMEOUT_CODE
import com.github.stars.repository.model.ErrorMessage.Companion.withResource
import com.github.stars.repository.model.Resource
import com.github.stars.repository.model.Resource.Error
import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException


suspend fun <T : Any> Deferred<Response<T>>.fetchResource(): Resource<T> {
    return try {
        createResource(await())
    } catch (e: UnknownHostException) {
        val message = withResource(HttpURLConnection.HTTP_NOT_FOUND, R.string.unknow_host_message)
        Error(message)
    } catch (e: SocketTimeoutException) {
        val message = withResource(SOCKET_TIMEOUT_CODE, R.string.timeout_message)
        Error(message)
    } catch (e: Exception) {
        Resource.genericError()
    }
}

fun <T : Any> createResource(
    response: Response<T>
): Resource<T> {
    if (response.isSuccessful) {
        response.body()?.run {
            return Resource.Success(this)
        }
    }
    return Resource.genericError()
}

