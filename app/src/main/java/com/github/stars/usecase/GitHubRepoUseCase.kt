package com.github.stars.usecase

import com.github.stars.repository.model.Items
import com.github.stars.repository.model.Resource
import com.github.stars.repository.services.GitHubRepoRepository

class GitHubRepoUseCase(private val searchStarsRepository: GitHubRepoRepository){

    suspend fun getSearchStarsList(text: String, sort: String, page:Int): Resource<List<Items>> {
        var response = searchStarsRepository.listGitHubRepoForLanguage(text, sort, page)
        if (response is Resource.Success && response.data.isNullOrEmpty()) {
            response = Resource.noContent()
        }
        return response
    }
}