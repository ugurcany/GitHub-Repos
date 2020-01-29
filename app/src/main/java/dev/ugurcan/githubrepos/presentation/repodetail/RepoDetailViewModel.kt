package dev.ugurcan.githubrepos.presentation.repodetail

import com.ww.roxie.BaseViewModel
import com.ww.roxie.Reducer
import dev.ugurcan.githubrepos.data.State
import dev.ugurcan.githubrepos.domain.repos.RepoRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RepoDetailViewModel(private val repoRepository: RepoRepository) :
    BaseViewModel<RepoDetailAction, RepoDetailState>() {

    override val initialState = RepoDetailState(state = State.IDLE)

    private val reducer: Reducer<RepoDetailState, RepoDetailChange> = { state, change ->
        when (change) {
            is RepoDetailChange.Loading -> state.copy(
                state = State.LOADING,
                errorMessage = ""
            )
            is RepoDetailChange.IsRepoBookmarked -> state.copy(
                state = State.DATA,
                isBookmarked = change.isBookmarked,
                errorMessage = ""
            )
            is RepoDetailChange.Error -> state.copy(
                state = State.ERROR,
                errorMessage = change.throwable?.message ?: "Something went wrong!"
            )
        }
    }

    init {
        bindActions()
    }

    private fun bindActions() {
        val isRepoBookmarkChange = actions.ofType<RepoDetailAction.IsRepoBookmarked>()
            .switchMap {
                repoRepository.isRepoBookmarked(repo = it.repo)
                    .subscribeOn(Schedulers.io())
                    .map<RepoDetailChange> { RepoDetailChange.IsRepoBookmarked(true) }
                    .onErrorReturn { RepoDetailChange.IsRepoBookmarked(false) }
                    .startWith(RepoDetailChange.Loading)
            }

        val bookmarkRepoChange = actions.ofType<RepoDetailAction.BookmarkRepo>()
            .switchMap {
                repoRepository.bookmarkRepo(
                    repo = it.repo,
                    shouldBookmark = state.value?.isBookmarked?.not() ?: true
                )
                    .subscribeOn(Schedulers.io())
                    .map<RepoDetailChange> { bookmarked ->
                        RepoDetailChange.IsRepoBookmarked(
                            bookmarked
                        )
                    }
                    .onErrorReturn { throwable -> RepoDetailChange.Error(throwable) }
                    .startWith(RepoDetailChange.Loading)
            }

        val allChanges = Observable.merge(isRepoBookmarkChange, bookmarkRepoChange)

        disposables += allChanges
            .scan(initialState, reducer)
            .filter { it.state != State.IDLE }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(state::setValue, Timber::e)
    }
}