package dev.ugurcan.githubrepos.presentation.repodetail

import com.ww.roxie.BaseState
import dev.ugurcan.githubrepos.data.State

data class RepoDetailState(
    val isBookmarked: Boolean = false,
    val state: State = State.IDLE,
    val errorMessage: String = ""
) : BaseState