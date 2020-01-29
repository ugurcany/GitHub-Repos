package dev.ugurcan.githubrepos.ui.repodetail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    private lateinit var repo: Repo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repodetail)

        repo = intent.getSerializableExtra(KEY_REPO) as Repo
        initUI()

        viewModel.observableState.observe(this, Observer { state ->
            state?.let { renderState(state) }
        })

        viewModel.dispatch(RepoDetailAction.IsRepoBookmarked(repo))
    }

    private fun initUI() {
        textViewRepoDetailName.text = repo.name
        textViewRepoDetailDescription.text = repo.description
        textViewRepoDetailStarCount.text = "${repo.starCount}"
        buttonRepoBookmark.setOnClickListener {
            viewModel.dispatch(RepoDetailAction.BookmarkRepo(repo))
        }
    }

    private fun renderState(state: RepoDetailState) {
        when (state.state) {
            State.LOADING -> {
                buttonRepoBookmark.isEnabled = false
            }
            State.DATA -> {
                buttonRepoBookmark.text =
                    getString(if (state.isBookmarked) R.string.bookmarked else R.string.bookmark)
                buttonRepoBookmark.isEnabled = true
            }
            State.ERROR -> {
                Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
                buttonRepoBookmark.isEnabled = true
            }
            else -> {
            }
        }
    }

    companion object {
        const val KEY_REPO = "RepoDetailActivity.Repo"
    }
}
