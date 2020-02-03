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
                state = State.LOADING
            )
            is RepoDetailChange.Data -> state.copy(
                state = State.DATA,
                repo = change.repo
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
        val setRepoChange = actions.ofType<RepoDetailAction.SetRepo>()
            .switchMap {
                repoRepository.isRepoBookmarked(repo = it.repo)
                    .subscribeOn(Schedulers.io())
                    .map<RepoDetailChange> { repo -> RepoDetailChange.Data(repo) }
                    .onErrorReturn { throwable -> RepoDetailChange.Error(throwable) }
                    .startWith(RepoDetailChange.Loading)
            }

        val bookmarkRepoChange = actions.ofType<RepoDetailAction.BookmarkRepo>()
            .switchMap {
                repoRepository.toggleBookmark(repo = state.value?.repo ?: it.repo)
                    .subscribeOn(Schedulers.io())
                    .map<RepoDetailChange> { repo -> RepoDetailChange.Data(repo) }
                    .onErrorReturn { throwable -> RepoDetailChange.Error(throwable) }
                    .startWith(RepoDetailChange.Loading)
            }

        val allChanges = Observable.merge(setRepoChange, bookmarkRepoChange)

        disposables += allChanges
            .scan(initialState, reducer)
            .filter { it.state != State.IDLE }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(state::setValue, Timber::e)
    }
}