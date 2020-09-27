package com.github.stars.repository.services

import com.github.stars.repository.model.Items
import com.github.stars.repository.model.Resource
import com.github.stars.repository.remote.GitHubRepoRemoteDataSource

class GitHubRepoRepository(private val remoteDataSource: GitHubRepoRemoteDataSource,) {
    suspend fun listGitHubRepoForLanguage(text: String, sort:String, page: Int): Resource<List<Items>> =
        remoteDataSource.listGitHubRepoForLanguage(text, sort, page)

}