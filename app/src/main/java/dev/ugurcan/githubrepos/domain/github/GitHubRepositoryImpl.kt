package dev.ugurcan.githubrepos.domain.github

import dev.ugurcan.githubrepos.Config
import dev.ugurcan.githubrepos.data.Repo
import io.reactivex.Observable

class GitHubRepositoryImpl(private val gitHubApi: GitHubApi) : GitHubRepository {
    override fun loadRepos(organization: String, pageSize: Int, page: Int): Observable<List<Repo>> {
        return gitHubApi.getRepos(
            organization = organization,
            pageSize = pageSize,
            page = page,
            clientId = Config.GITHUB_CLIENT_ID,
            clientSecret = Config.GITHUB_CLIENT_SECRET
        )
    }
}