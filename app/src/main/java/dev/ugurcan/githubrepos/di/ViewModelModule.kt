package dev.ugurcan.githubrepos.di

import dev.ugurcan.githubrepos.presentation.repodetail.RepoDetailViewModel
import dev.ugurcan.githubrepos.presentation.repolist.RepoListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel { RepoListViewModel(get()) }
    viewModel { RepoDetailViewModel(get()) }
}
