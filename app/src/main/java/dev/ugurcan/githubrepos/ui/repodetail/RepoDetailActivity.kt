package dev.ugurcan.githubrepos.ui.repodetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dev.ugurcan.githubrepos.R
import dev.ugurcan.githubrepos.presentation.repodetail.RepoDetailState
import dev.ugurcan.githubrepos.presentation.repodetail.RepoDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RepoDetailActivity : AppCompatActivity() {

    // Lazy Inject ViewModel
    private val viewModel: RepoDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repodetail)

        viewModel.observableState.observe(this, Observer { state ->
            state?.let { renderState(state) }
        })
    }

    private fun renderState(state: RepoDetailState) {
        when (state.state) {
            /*State.LOADING -> renderLoading()
            State.ERROR -> renderError(state.errorMessage)
            else -> renderData(state.repoList)*/
        }
    }
}
