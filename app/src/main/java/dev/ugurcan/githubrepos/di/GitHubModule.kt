package dev.ugurcan.githubrepos.di

import com.google.gson.GsonBuilder
import dev.ugurcan.githubrepos.domain.github.GitHubApi
import dev.ugurcan.githubrepos.domain.github.GitHubRepository
import dev.ugurcan.githubrepos.domain.github.GitHubRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val gitHubModule = module {
    // single instance of GitHubApi
    single<GitHubApi> {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        retrofit.create(GitHubApi::class.java)
    }

    // single instance of GitHubRepository
    single<GitHubRepository> { GitHubRepositoryImpl(get()) }
}