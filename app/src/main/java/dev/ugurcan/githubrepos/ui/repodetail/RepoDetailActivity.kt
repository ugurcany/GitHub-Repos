package dev.ugurcan.githubrepos.ui.repodetail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import dev.ugurcan.githubrepos.R
import dev.ugurcan.githubrepos.data.Repo
import dev.ugurcan.githubrepos.data.State
import dev.ugurcan.githubrepos.presentation.repodetail.RepoDetailAction
import dev.ugurcan.githubrepos.presentation.repodetail.RepoDetailState
import dev.ugurcan.githubrepos.presentation.repodetail.RepoDetailViewModel
import kotlinx.android.synthetic.main.activity_repodetail.*
import org.koin.android.viewmodel.ext.android.viewModel

class RepoDetailActivity : AppCompatActivity() {

    // Lazy Inject ViewModel
    private val viewModel: RepoDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repodetail)
        buttonRepoBookmark.setOnClickListener {
            viewModel.dispatch(RepoDetailAction.BookmarkRepo)
        }

        viewModel.observableState.observe(this, Observer { state ->
            state?.let { renderState(state) }
        })

        val repo = intent.getSerializableExtra(KEY_REPO) as Repo
        viewModel.dispatch(RepoDetailAction.SetRepo(repo))
    }

    private fun renderState(state: RepoDetailState) {
        when (state.state) {
            State.LOADING -> {
                buttonRepoBookmark.isEnabled = false
            }
            State.DATA -> {
                buttonRepoBookmark.isEnabled = true
                setRepoUI(state.repo)
            }
            State.ERROR -> {
                Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
                buttonRepoBookmark.isEnabled = true
            }
            else -> {
            }
        }
    }

    private fun setRepoUI(repo: Repo?) {
        if (repo == null)
            return

        textViewRepoDetailName.text = repo.name
        textViewRepoDetailDescription.text = repo.description
        textViewRepoDetailStarCount.text = "${repo.starCount}"
        imageViewRepoDetailBookmarked.isVisible = repo.isBookmarked
        buttonRepoBookmark.text =
            getString(if (repo.isBookmarked) R.string.remove_bookmark else R.string.bookmark)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        setResult(
            ACTIVITY_RESULT_CODE,
            Intent().putExtra(KEY_REPO, viewModel.observableState.value?.repo)
        )
        super.onBackPressed()
    }

    companion object {
        const val KEY_REPO = "RepoDetailActivity.Repo"
        const val ACTIVITY_REQUEST_CODE = 301
        const val ACTIVITY_RESULT_CODE = 302
    }
}
