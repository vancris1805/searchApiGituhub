package com.github.stars.repository.remote

import com.github.stars.repository.model.Items
import com.github.stars.repository.model.Resource

interface ISearchStarsDataSource {
    suspend fun listGitHubRepoForLanguage(text: String, sort:String, page: Int): Resource<List<Items>>
}