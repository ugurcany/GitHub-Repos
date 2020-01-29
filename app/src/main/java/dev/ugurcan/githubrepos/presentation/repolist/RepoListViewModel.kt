package dev.ugurcan.githubrepos.presentation.repolist

import com.ww.roxie.BaseViewModel
import com.ww.roxie.Reducer
import dev.ugurcan.githubrepos.data.State
import dev.ugurcan.githubrepos.domain.github.GitHubRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RepoListViewModel(private val gitHubRepository: GitHubRepository) :
    BaseViewModel<RepoListAction, RepoListState>() {

    override val initialState = RepoListState(state = State.IDLE)

    val pageSize: Int = 20
    var page: Int = 1

    private val reducer: Reducer<RepoListState, RepoListChange> = { state, change ->
        when (change) {
            is RepoListChange.Loading -> state.copy(
                state = State.LOADING,
                repoList = emptyList(),
                errorMessage = ""
            )
            is RepoListChange.Data -> state.copy(
                state = State.DATA,
                repoList = change.repoList,
                errorMessage = ""
            )
            is RepoListChange.Error -> state.copy(
                state = State.ERROR,
                repoList = emptyList(),
                errorMessage = change.throwable?.message ?: "Something went wrong!"
            )
        }
    }

    init {
        bindActions()
    }

    private fun bindActions() {
        val loadReposChange = actions.ofType<RepoListAction.LoadRepos>()
            .switchMap {
                gitHubRepository.loadRepos(organization = it.organization, pageSize = pageSize, page = page)
                    .subscribeOn(Schedulers.io())
                    .map<RepoListChange> { data -> RepoListChange.Data(data) }
                    .defaultIfEmpty(RepoListChange.Data(emptyList()))
                    .onErrorReturn { throwable -> RepoListChange.Error(throwable) }
                    .startWith(RepoListChange.Loading)
            }

        disposables += loadReposChange
            .scan(initialState, reducer)
            .filter { it.state != State.IDLE }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(state::setValue, Timber::e)
    }
}