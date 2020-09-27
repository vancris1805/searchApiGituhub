package com.github.stars.repository.remote

import com.github.stars.repository.model.SearchStarsResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val SEARCH = "search/repositories"
private const val QUERY_PARAM_Q = "q"
private const val QUERY_PARAM_SORT = "sort"
private const val QUERY_PARAM_PAGE = "page"

interface GitHubRepoService {

    @GET(SEARCH)
        fun getSearchStarsAsync(
            @Query(QUERY_PARAM_Q) text: String,
            @Query(QUERY_PARAM_SORT) sort: String,
            @Query(QUERY_PARAM_PAGE) page: Int,
        ): Deferred<Response<SearchStarsResponse>>
}