package dev.ugurcan.githubrepos.domain.repos

import dev.ugurcan.githubrepos.Config
import dev.ugurcan.githubrepos.data.Repo
import dev.ugurcan.githubrepos.domain.repos.api.GitHubApi
import dev.ugurcan.githubrepos.domain.repos.db.RepoDb
import io.reactivex.Observable

class RepoRepositoryImpl(private val gitHubApi: GitHubApi, private val repoDb: RepoDb) :
    RepoRepository {
    override fun loadRepos(organization: String, pageSize: Int, page: Int): Observable<List<Repo>> {
        return gitHubApi.getRepos(
            organization = organization,
            pageSize = pageSize,
            page = page,
            clientId = Config.GITHUB_CLIENT_ID,
            clientSecret = Config.GITHUB_CLIENT_SECRET
        )
            .flatMapIterable { list -> list }
            .flatMap { repo ->
                isRepoBookmarked(repo)
            }
            .toList()
            .toObservable()
    }

    override fun isRepoBookmarked(repo: Repo): Observable<Repo> {
        return repoDb.repoDao().getById(repo.id)
            .onErrorReturn { repo }
            .toObservable()
    }

    override fun bookmarkRepo(repo: Repo, shouldBookmark: Boolean): Observable<Repo> {
        repo.isBookmarked = shouldBookmark

        return if (shouldBookmark) {
            repoDb.repoDao().insert(repo)
                .map { repo }
                .toObservable()
        } else {
            repoDb.repoDao().delete(repo)
                .map { repo }
                .toObservable()
        }
    }
}