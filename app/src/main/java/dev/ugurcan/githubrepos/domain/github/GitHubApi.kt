package dev.ugurcan.githubrepos.domain.github

import dev.ugurcan.githubrepos.data.Repo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("orgs/{org}/repos")
    fun getRepos(
        @Path("org") organization: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int
    ): Observable<List<Repo>>

}