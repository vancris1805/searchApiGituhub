package com.github.stars.repository.remote

import com.github.stars.extensions.fetchResource
import com.github.stars.repository.model.Items
import com.github.stars.repository.model.Resource
import retrofit2.Retrofit

class GitHubRepoRemoteDataSource(retrofit: Retrofit): ISearchStarsDataSource {
    private val searchStarsService: GitHubRepoService =
        retrofit.create(GitHubRepoService::class.java)

    override suspend fun listGitHubRepoForLanguage(
        text: String, sort:String, page: Int
    ): Resource<List<Items>> {
        val response = searchStarsService
            .getSearchStarsAsync(text,sort,page)
            .fetchResource()
        return when (response) {
            is Resource.Success -> Resource.Success(response.data.items)
            is Resource.Error -> Resource.Error(response.errorData)
        }
    }
}