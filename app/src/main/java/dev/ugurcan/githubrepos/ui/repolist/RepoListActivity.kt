package dev.ugurcan.githubrepos.ui.repolist

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ugurcan.githubrepos.R
import dev.ugurcan.githubrepos.data.Repo
import dev.ugurcan.githubrepos.data.State
import dev.ugurcan.githubrepos.presentation.repolist.RepoListAction
import dev.ugurcan.githubrepos.presentation.repolist.RepoListState
import dev.ugurcan.githubrepos.presentation.repolist.RepoListViewModel
import dev.ugurcan.githubrepos.ui.repodetail.RepoDetailActivity
import kotlinx.android.synthetic.main.activity_repolist.*
import org.koin.android.viewmodel.ext.android.viewModel

class RepoListActivity : AppCompatActivity() {

    // Lazy Inject ViewModel
    private val viewModel: RepoListViewModel by viewModel()

    private val adapter = RepoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repolist)
        setupRecyclerView()
        setupSwipeRefreshLayout()

        viewModel.observableState.observe(this, Observer { state ->
            state?.let { renderState(state) }
        })

        loadData(false)
    }

    private fun loadData(requestNextPage: Boolean) {
        if (requestNextPage) viewModel.page++
        else viewModel.page = 1
        viewModel.dispatch(RepoListAction.LoadRepos("square"))
    }

    private fun setupRecyclerView() {
        adapter.loadMoreModule?.let {
            it.setOnLoadMoreListener {
                loadData(true)
            }
            it.isAutoLoadMore = true
        }

        adapter.setOnItemClickListener { adapter, _, position ->
            val intent = Intent(this, RepoDetailActivity::class.java)
            intent.putExtra(RepoDetailActivity.KEY_REPO, adapter.getItem(position) as Repo)
            startActivity(intent)
        }

        recyclerView.layoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager(this)
            else -> GridLayoutManager(this, 2)
        }
        recyclerView.adapter = adapter
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            loadData(false)
        }
    }

    private fun renderState(state: RepoListState) {
        when (state.state) {
            State.LOADING -> renderLoading()
            State.ERROR -> renderError(state.errorMessage)
            else -> renderData(state.repoList)
        }
    }

    private fun renderLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun renderError(errorMessage: String) {
        adapter.loadMoreModule?.loadMoreComplete()
        swipeRefreshLayout.isRefreshing = false
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun renderData(repoList: List<Repo>) {
        swipeRefreshLayout.isRefreshing = false

        //NEW DATA OR ADDITION?
        if (viewModel.page == 1)
            adapter.setNewData(repoList.toMutableList())
        else {
            adapter.addData(repoList.toMutableList())
            //DECIDE IF MORE PAGES EXIST OR NOT
            if (repoList.size < viewModel.pageSize)
                adapter.loadMoreModule?.loadMoreEnd()
            else
                adapter.loadMoreModule?.loadMoreComplete()
        }
    }
}
