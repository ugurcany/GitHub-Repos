package dev.ugurcan.githubrepos.presentation.repodetail

import com.ww.roxie.BaseAction

sealed class RepoDetailAction : BaseAction {
    object IsRepoBookmarked : RepoDetailAction()
    data class BookmarkRepo(val repoId: String, val shouldBookmark: Boolean) : RepoDetailAction()
}