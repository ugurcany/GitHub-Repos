package dev.ugurcan.githubrepos.presentation.repodetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import dev.ugurcan.githubrepos.RxTestSchedulerRule
import dev.ugurcan.githubrepos.data.Repo
import dev.ugurcan.githubrepos.data.State
import dev.ugurcan.githubrepos.domain.repos.RepoRepository
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RepoDetailViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testSchedulerRule = RxTestSchedulerRule()

    private lateinit var testSubject: RepoDetailViewModel

    private val loadingState = RepoDetailState(state = State.LOADING)

    private val repoRepository = mock<RepoRepository>()
    private val observer = mock<Observer<RepoDetailState>>()

    @Before
    fun setUp() {
        testSubject = RepoDetailViewModel(repoRepository)
        testSubject.observableState.observeForever(observer)
    }

    @Test
    fun `Given not-bookmarked repo successfully loaded, when action SetRepo is received, then state contains repo`() {
        // GIVEN
        val repo = Repo(0, "title", "description", 123, false)
        val notBookmarkedRepo = repo.copy(isBookmarked = false)
        val successState = RepoDetailState(repo = notBookmarkedRepo, state = State.DATA)

        whenever(
            repoRepository.isRepoBookmarked(repo)
        ).thenReturn(Observable.just(notBookmarkedRepo))

        // WHEN
        testSubject.dispatch(RepoDetailAction.SetRepo(repo))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(successState)
        }
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `Given bookmarked repo successfully loaded, when action SetRepo is received, then state contains repo`() {
        // GIVEN
        val repo = Repo(0, "title", "description", 123, false)
        val bookmarkedRepo = repo.copy(isBookmarked = true)
        val successState = RepoDetailState(repo = bookmarkedRepo, state = State.DATA)

        whenever(
            repoRepository.isRepoBookmarked(repo)
        ).thenReturn(Observable.just(bookmarkedRepo))

        // WHEN
        testSubject.dispatch(RepoDetailAction.SetRepo(repo))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(successState)
        }
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `Given repo failed to load, when action SetRepo is received, then state contains error`() {
        // GIVEN
        val repo = Repo(0, "title", "description", 123, false)
        val errMsg = "Err!"
        val errorState = RepoDetailState(errorMessage = errMsg, state = State.ERROR)

        whenever(
            repoRepository.isRepoBookmarked(anyOrNull())
        ).thenReturn(Observable.error(Throwable(message = errMsg)))

        // WHEN
        testSubject.dispatch(RepoDetailAction.SetRepo(repo))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(errorState)
        }
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `Given repo successfully bookmarked, when action BookmarkRepo is received, then state contains bookmarked repo`() {
        // GIVEN
        val repo = Repo(0, "title", "description", 123, false)
        val bookmarkedRepo = repo.copy(isBookmarked = true)
        val successState = RepoDetailState(repo = bookmarkedRepo, state = State.DATA)

        whenever(
            repoRepository.toggleBookmark(repo)
        ).thenReturn(Observable.just(bookmarkedRepo))

        // WHEN
        testSubject.dispatch(RepoDetailAction.BookmarkRepo(repo))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(successState)
        }
        verifyNoMoreInteractions(observer)
    }


    @Test
    fun `Given repo successfully un-bookmarked, when action BookmarkRepo is received, then state contains un-bookmarked repo`() {
        // GIVEN
        val repo = Repo(0, "title", "description", 123, true)
        val unBookmarkedRepo = repo.copy(isBookmarked = false)
        val successState = RepoDetailState(repo = unBookmarkedRepo, state = State.DATA)

        whenever(
            repoRepository.toggleBookmark(repo)
        ).thenReturn(Observable.just(unBookmarkedRepo))

        // WHEN
        testSubject.dispatch(RepoDetailAction.BookmarkRepo(repo))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(successState)
        }
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `Given repo failed to be bookmarked, when action BookmarkRepo is received, then state contains error`() {
        // GIVEN
        val repo = Repo(0, "title", "description", 123, true)
        val errMsg = "Err!"
        val errorState = RepoDetailState(errorMessage = errMsg, state = State.ERROR)

        whenever(
            repoRepository.toggleBookmark(anyOrNull())
        ).thenReturn(Observable.error(Throwable(message = errMsg)))

        // WHEN
        testSubject.dispatch(RepoDetailAction.BookmarkRepo(repo))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(errorState)
        }
        verifyNoMoreInteractions(observer)
    }

}