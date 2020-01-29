package dev.ugurcan.githubrepos.presentation.repodetail

sealed class RepoDetailChange {
    object Loading : RepoDetailChange()
    data class IsRepoBookmarked(val isBookmarked: Boolean) : RepoDetailChange()
    data class Error(val throwable: Throwable?) : RepoDetailChange()
}