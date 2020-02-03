package dev.ugurcan.githubrepos.presentation.repolist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import dev.ugurcan.githubrepos.RxTestSchedulerRule
import dev.ugurcan.githubrepos.data.Repo
import dev.ugurcan.githubrepos.data.State
import dev.ugurcan.githubrepos.domain.repos.RepoRepository
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

class RepoListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testSchedulerRule = RxTestSchedulerRule()

    private lateinit var testSubject: RepoListViewModel

    private val loadingState = RepoListState(state = State.LOADING)

    private val repoRepository = mock<RepoRepository>()
    private val observer = mock<Observer<RepoListState>>()

    @Before
    fun setUp() {
        testSubject = RepoListViewModel(repoRepository)
        testSubject.observableState.observeForever(observer)
    }

    @Test
    fun `Given repo list successfully loaded, when action LoadRepos is received, then state contains repo list`() {
        // GIVEN
        val repoList = listOf(Repo(0, "title", "description", 123, false))
        val successState = RepoListState(repoList = repoList, state = State.DATA)

        whenever(
            repoRepository.loadRepos(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(Observable.just(repoList))

        // WHEN
        testSubject.dispatch(RepoListAction.LoadRepos("some_org"))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(successState)
        }
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `Given repo list failed to load, when action LoadRepos is received, then state contains error`() {
        // GIVEN
        val errMsg = "Err!"
        val errorState = RepoListState(errorMessage = errMsg, state = State.ERROR)

        whenever(
            repoRepository.loadRepos(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(Observable.error(Throwable(message = errMsg)))

        // WHEN
        testSubject.dispatch(RepoListAction.LoadRepos("some_org"))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(errorState)
        }
        verifyNoMoreInteractions(observer)
    }
}