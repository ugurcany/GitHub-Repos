package dev.ugurcan.githubrepos.domain.github

import dev.ugurcan.githubrepos.data.Repo
import io.reactivex.Observable

interface GitHubRepository {
    fun loadRepos(organization: String, pageSize: Int, page: Int): Observable<List<Repo>>
}