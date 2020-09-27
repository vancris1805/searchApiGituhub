package com.github.stars.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.github.stars.presentation.infrastructure.BaseViewModel
import com.github.stars.repository.model.Items
import com.github.stars.repository.model.Resource
import com.github.stars.usecase.GitHubRepoUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListGitHubRepoViewModel(
    context: Application,
    coroutineContext: CoroutineContext,
    private val searchStarsUseCase: GitHubRepoUseCase
): BaseViewModel(context, coroutineContext) {

    private var job: Job? = null
    val searchStarsList:MutableLiveData<List<Items>> = MutableLiveData()
    val requestError: MutableLiveData<Boolean> = MutableLiveData()
    var nextOffset: Int = 1
    var lastOffset: Int = 0
    private val offsetListed = mutableListOf<Int>()


    fun listGitHubRepoForLanguage(text: String, sort:String, page: Int) {
        if (noConnectivity()) return
        clearPagination()
        job?.cancel()
        job = viewModelScope.launch {
            istGitHubRepoForLanguageCallback(searchStarsUseCase.getSearchStarsList(text, sort,page))
        }
    }

    private fun istGitHubRepoForLanguageCallback(resource: Resource<List<Items>>) {
        when (resource) {
            is Resource.Success -> {
                searchStarsList.postValue(resource.data)
                requestError.postValue(false)
            }
            is Resource.Error -> {
                requestError.postValue(true)
            }
        }
    }

    fun loadMoreListRepoForLanguage() {
        if (offsetListed.contains(nextOffset)) return
        job?.cancel()
        job = viewModelScope.launch {
            offsetListed.add(nextOffset)
            nextOffset++
            loadMoreListRepoForLanguageCallback(searchStarsUseCase.getSearchStarsList
                ("language:kotlin", "stars",nextOffset))
        }
    }

    private fun loadMoreListRepoForLanguageCallback(resource: Resource<List<Items>>) {
        when (resource) {
            is Resource.Success -> {
                lastOffset = nextOffset
                searchStarsList.postValue(resource.data)
                requestError.postValue(false)
            }
            is Resource.Error -> {
                offsetListed.remove(nextOffset)
                requestError.postValue(true)
            }
        }
    }

    private fun clearPagination() {
        offsetListed.clear()
        lastOffset = 0
        nextOffset = 1
    }
}