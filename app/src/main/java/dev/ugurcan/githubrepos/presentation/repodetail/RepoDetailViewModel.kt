package dev.ugurcan.githubrepos.presentation.repodetail

import com.ww.roxie.BaseViewModel
import com.ww.roxie.Reducer
import dev.ugurcan.githubrepos.data.State
import dev.ugurcan.githubrepos.presentation.repolist.RepoListChange
import dev.ugurcan.githubrepos.presentation.repolist.RepoListState

class RepoDetailViewModel() :
    BaseViewModel<RepoDetailAction, RepoDetailState>() {

    override val initialState = RepoDetailState(state = State.IDLE)

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
        /*val loadReposChange = actions.ofType<RepoListAction.LoadRepos>()
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
            .subscribe(state::setValue, Timber::e)*/
    }
}