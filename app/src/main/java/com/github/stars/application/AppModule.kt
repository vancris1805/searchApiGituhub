package com.github.stars.application

import com.github.stars.repository.infrastruture.remote.RetrofitFactory
import com.github.stars.presentation.viewmodel.ListGitHubRepoViewModel
import com.github.stars.repository.remote.GitHubRepoRemoteDataSource
import com.github.stars.repository.services.GitHubRepoRepository
import com.github.stars.usecase.GitHubRepoUseCase
import kotlinx.coroutines.Dispatchers

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


private const val RETROFIT = "RETROFIT"

object AppModule {
    private val mainCoroutineDispatcher by lazy { Dispatchers.IO }

    val servicesModule = module {
        single { AppContext }
        single(name = RETROFIT){
            RetrofitFactory.getRetrofit()
        }
    }

    val searchModule = module {
        single { GitHubRepoRemoteDataSource(get(name = RETROFIT)) }
        single { GitHubRepoRepository(get()) }
        single { GitHubRepoUseCase(get()) }
        viewModel { ListGitHubRepoViewModel(get(), mainCoroutineDispatcher, get()) }
    }

}