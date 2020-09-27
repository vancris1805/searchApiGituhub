package com.github.stars.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.stars.R
import com.github.stars.application.GlideApp
import com.github.stars.presentation.adapter.ListGitHubRepoAdapter
import com.github.stars.presentation.viewmodel.ListGitHubRepoViewModel
import com.github.stars.repository.model.Items
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.viewModel

private const val REMAIN_LOAD_MORE = 5

class GitHubRepoActivity : AppCompatActivity() {

    val searchViewModel: ListGitHubRepoViewModel by viewModel()
    private lateinit var adapter: ListGitHubRepoAdapter
    private lateinit var loadMoreListener: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initListeners()
    }

    private fun initData() {
        with(searchViewModel) {
            searchStarsList.observe(this@GitHubRepoActivity, Observer(::bindList))
            requestError.observe(this@GitHubRepoActivity, Observer(::bindError))
        }

        searchViewModel.listGitHubRepoForLanguage("language:kotlin", "stars", 0)
    }

    private fun initListeners() {
        loadMoreListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0) return
                val manager = (recyclerView.layoutManager as? LinearLayoutManager) ?: return
                val visibleItemCount = manager.childCount
                val totalItemCount = manager.itemCount
                val pastVisibleItems = manager.findFirstVisibleItemPosition()
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount - REMAIN_LOAD_MORE) {
                    searchViewModel.loadMoreListRepoForLanguage()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        }
    }

    private fun bindList(searchList: List<Items>) {
        setupSearchList(searchList)
        CoroutineScope(Dispatchers.Default).launch {
            val allItems = mutableSetOf<Items>()
            allItems.addAll(adapter.items)
            allItems.addAll(searchList)
            withContext(Dispatchers.Main) {
                adapter.items = allItems.toList()
            }
        }
    }

    private fun bindError(error: Boolean){
        if(error) Snackbar.make(searchRootView,R.string.generic_error_message, Snackbar.LENGTH_LONG).show()
    }

    private fun setupSearchList(list: List<Items>) {
        adapter = ListGitHubRepoAdapter(
            GlideApp.with(this),
            ::onClickedSearchContent
        )

        searchListView.addOnScrollListener(loadMoreListener)
        searchListView.adapter = adapter
        adapter.items = list
    }

    private fun onClickedSearchContent(items: Items) {
        if (searchViewModel.noConnectivity()) return
    }
}