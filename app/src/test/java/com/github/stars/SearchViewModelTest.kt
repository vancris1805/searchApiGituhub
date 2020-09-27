package com.github.stars

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.stars.application.AppContext
import com.github.stars.presentation.viewmodel.ListGitHubRepoViewModel
import com.github.stars.repository.model.ErrorMessage
import com.github.stars.repository.model.Items
import com.github.stars.repository.model.Resource
import com.github.stars.repository.model.SearchStarsResponse
import com.github.stars.repository.services.GitHubRepoRepository
import com.github.stars.usecase.GitHubRepoUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var context: AppContext
    @Mock
    lateinit var mockedRepository: GitHubRepoRepository
    @Mock
    lateinit var searchUseCase: GitHubRepoUseCase
    @Mock
    lateinit var searchStarsListTest: Observer<List<Items>>
    @Mock
    lateinit var requestErrorTest: Observer<Boolean>
    @Mock
    private lateinit var connManager: ConnectivityManager
    @Mock
    private lateinit var networkInfo: NetworkInfo
    @Mock
    private lateinit var packageManager: PackageManager

    lateinit var viewModel: ListGitHubRepoViewModel

    val SEARCH_STARS by lazy {
        SearchStarsResponse(
            items = getSearchResultContentFromFile("mockito-extensions/search.json")
        )
    }

    val SEARCH_STARS_EMPTY by lazy {
        SearchStarsResponse(
            items = emptyList()
        )
    }

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        setupContext()
        setupViewModelMocks()
        setupConnectivityManager()

        mockedRepository = Mockito.mock(GitHubRepoRepository::class.java)
        searchUseCase = Mockito.spy(GitHubRepoUseCase(mockedRepository))

        viewModel = ListGitHubRepoViewModel(context, Dispatchers.Unconfined, searchUseCase)

        with(viewModel) {
          searchStarsList.observeForever(searchStarsListTest)
          requestError.observeForever(requestErrorTest)
        }
    }

    private fun setupContext() {
        context = Mockito.mock(AppContext::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    private fun setupViewModelMocks() {
        searchStarsListTest = Mockito.mock(Observer::class.java) as Observer<List<Items>>
    }

    private fun setupConnectivityManager() {
        connManager = Mockito.mock(ConnectivityManager::class.java)
        networkInfo = Mockito.mock(NetworkInfo::class.java)
        packageManager = Mockito.mock(PackageManager::class.java)
        Mockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connManager)
        Mockito.`when`(connManager.activeNetworkInfo).thenReturn(networkInfo)
        Mockito.`when`(networkInfo.isConnected).thenReturn(true)
    }

    private fun getSearchResultContentFromFile(filename: String): List<Items> {
        return Gson().fromJson(fromJSONFile(filename), object : TypeToken<List<Items>>() {}.type)
    }

    private fun fromJSONFile(filename: String): BufferedReader {
        val mediaResponseRaw = javaClass.classLoader?.getResourceAsStream(filename)
        return BufferedReader(InputStreamReader(mediaResponseRaw))
    }

    @Test
    fun getSearch_SUCCESS() = runBlocking {
        Mockito.`when`(
            mockedRepository.listGitHubRepoForLanguage("language:kotlin", "stars", 1)
        ).thenReturn(
            Resource.Success(SEARCH_STARS.items)
        )

        viewModel.listGitHubRepoForLanguage("language:kotlin", "stars", 1)
        Mockito.verify(searchUseCase).getSearchStarsList("language:kotlin", "stars", 1)
        Mockito.verify(searchStarsListTest, Mockito.atLeastOnce()).onChanged(SEARCH_STARS.items)
        Mockito.verify(requestErrorTest, Mockito.atLeastOnce()).onChanged(false)
    }

    @Test
    fun getSearchLoadMore_SUCCESS() = runBlocking {
            Mockito.`when`(
                mockedRepository.listGitHubRepoForLanguage("language:kotlin", "stars", 2)
            ).thenReturn(
                Resource.Success(SEARCH_STARS.items)
            )

            viewModel.loadMoreListRepoForLanguage()
            Mockito.verify(searchUseCase).getSearchStarsList("language:kotlin", "stars", 2)
            Mockito.verify(searchStarsListTest, Mockito.atLeastOnce()).onChanged(SEARCH_STARS.items)
            Mockito.verify(requestErrorTest, Mockito.atLeastOnce()).onChanged(false)
    }

}