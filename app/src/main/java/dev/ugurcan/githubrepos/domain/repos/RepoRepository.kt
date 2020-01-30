package dev.ugurcan.githubrepos.domain.repos

import dev.ugurcan.githubrepos.data.Repo
import io.reactivex.Observable

interface RepoRepository {
    fun loadRepos(organization: String, pageSize: Int, page: Int): Observable<List<Repo>>
    fun isRepoBookmarked(repo: Repo): Observable<Repo>
    fun bookmarkRepo(repo: Repo, shouldBookmark: Boolean): Observable<Repo>
}