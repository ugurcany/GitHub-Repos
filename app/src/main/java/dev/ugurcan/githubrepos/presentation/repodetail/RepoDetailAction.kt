package dev.ugurcan.githubrepos.presentation.repodetail

import com.ww.roxie.BaseAction
import dev.ugurcan.githubrepos.data.Repo

sealed class RepoDetailAction : BaseAction {
    data class SetRepo(val repo: Repo) : RepoDetailAction()
    object BookmarkRepo : RepoDetailAction()
}