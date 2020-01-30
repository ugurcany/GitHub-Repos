package dev.ugurcan.githubrepos.presentation.repodetail

import dev.ugurcan.githubrepos.data.Repo

sealed class RepoDetailChange {
    object Loading : RepoDetailChange()
    data class Data(val repo: Repo) : RepoDetailChange()
    data class Error(val throwable: Throwable?) : RepoDetailChange()
}