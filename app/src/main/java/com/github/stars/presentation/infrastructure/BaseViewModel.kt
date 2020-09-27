package com.github.stars.presentation.infrastructure

import com.github.stars.R
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(private val context: Application, coroutineContext: CoroutineContext) :
    AndroidViewModel(context) {

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob by lazy { Job() }
    /**
     * This is the scope for all coroutines launched by the ViewModel.
     *
     * Since we pass [viewModelJob], you can cancel all coroutines launched by [viewModelScope] by calling
     * viewModelJob.cancel().  This is called in [onCleared].
     */
    protected val viewModelScope by lazy { CoroutineScope(coroutineContext + viewModelJob) }

    var loading: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Checks for current internet connectivity and send a br.com.net.netott.action.NO_CONNECTIVITY
     * Broadcas if not connected.
     *
     * @return true if has no connectivity, false otherwise.
     */
    fun noConnectivity(): Boolean {
        return if (!NetworkStateChecker.isConnected(context)) {
            sendNoConnectivityBroadcast()
            true
        } else {
            false
        }
    }

    private fun sendNoConnectivityBroadcast() {
        val broadcast = Intent()
        broadcast.action =
            context.getString(R.string.no_connectivity)
        broadcast.addCategory(Intent.CATEGORY_DEFAULT)
       // LocalBroadcastManager.getInstance(context).sendBroadcast(broadcast)
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        viewModelJob.cancel()
        viewModelScope.coroutineContext.cancel()
        super.onCleared()
    }
}
