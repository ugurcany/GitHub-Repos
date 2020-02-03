package dev.ugurcan.githubrepos.presentation.repolist

import com.ww.roxie.BaseAction

sealed class RepoListAction : BaseAction {
    data class LoadRepos(val organization: String) : RepoListAction()
}