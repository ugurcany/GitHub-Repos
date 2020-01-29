package dev.ugurcan.githubrepos.presentation.repolist

import com.ww.roxie.BaseState
import dev.ugurcan.githubrepos.data.Repo
import dev.ugurcan.githubrepos.data.State

data class RepoListState(
    val repoList: List<Repo> = emptyList(),
    val state: State = State.IDLE,
    val errorMessage: String = ""
) : BaseState