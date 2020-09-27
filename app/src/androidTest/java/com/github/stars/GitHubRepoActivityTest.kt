package com.github.stars

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.github.stars.presentation.GitHubRepoActivity
import com.github.stars.presentation.viewmodel.ListGitHubRepoViewModel
import com.github.stars.repository.model.Items
import com.github.stars.repository.model.SearchStarsResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import java.io.BufferedReader
import java.io.InputStreamReader

@RunWith(AndroidJUnit4::class)
class GitHubRepoActivityTest : KoinTest{

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(GitHubRepoActivity::class.java, true, true)

    lateinit var viewModel: ListGitHubRepoViewModel

    val SEARCH_STARS by lazy {
        SearchStarsResponse(
            items = getSearchResultContentFromFile("mockito-extensions/search.json")
        )
    }

    private fun getSearchResultContentFromFile(filename: String): List<Items> {
        return Gson().fromJson(fromJSONFile(filename), object : TypeToken<List<Items>>() {}.type)
    }

    private fun fromJSONFile(filename: String): BufferedReader {
        val mediaResponseRaw = javaClass.classLoader?.getResourceAsStream(filename)
        return BufferedReader(InputStreamReader(mediaResponseRaw))
    }

//    @Before
//    fun setUp() {
//        viewModel = mock(ListGitHubRepoViewModel::class.java)
//
//        loadKoinModules(module {
//            viewModel {
//                viewModel
//            }
//        })
//    }
//
//    @After
//    fun cleanUp() {
//        stopKoin()
//    }


    @Test
    fun setListIsDisplayed() {
        activityRule.activity.searchViewModel.searchStarsList.postValue(SEARCH_STARS.items)
        onView(withId(R.id.searchListView))
            .check(matches(isDisplayed()))

    }

}