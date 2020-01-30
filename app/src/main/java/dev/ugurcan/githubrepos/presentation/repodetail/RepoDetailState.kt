package dev.ugurcan.githubrepos.presentation.repodetail

import com.ww.roxie.BaseState
import dev.ugurcan.githubrepos.data.Repo
import dev.ugurcan.githubrepos.data.State

data class RepoDetailState(
    val repo: Repo? = null,
    val state: State = State.IDLE,
    val errorMessage: String = ""
) : BaseState