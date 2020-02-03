package dev.ugurcan.githubrepos.presentation.repolist

import dev.ugurcan.githubrepos.data.Repo

sealed class RepoListChange {
    object Loading : RepoListChange()
    data class Data(val repoList: List<Repo>) : RepoListChange()
    data class Error(val throwable: Throwable?) : RepoListChange()
}