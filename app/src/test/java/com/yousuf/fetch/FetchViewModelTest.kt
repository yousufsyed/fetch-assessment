package com.yousuf.fetch

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.yousuf.fetch.network.data.FetchResult
import com.yousuf.fetch.provider.FetchEventLogger
import com.yousuf.fetch.provider.FetchProvider
import com.yousuf.fetch.viewmodel.FetchState
import com.yousuf.fetch.viewmodel.FetchViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FetchViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fetchProvider: FetchProvider
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: FetchViewModel

    private val fetchEventLogger = mockk<FetchEventLogger>()

    @Before
    fun setup() {
        savedStateHandle = SavedStateHandle()
        fetchProvider = mockk<FetchProvider>()
        viewModel = FetchViewModel(fetchProvider, fetchEventLogger, savedStateHandle)
    }

    @Test
    fun testFetchResultsStateInitialStates() {
        Assert.assertEquals(viewModel.fetchResults.value, emptyList<FetchResult>())
        Assert.assertEquals(viewModel.fetchState.value, FetchState.Init)
        Assert.assertEquals(viewModel.errorMessage.value, "")
        Assert.assertEquals(viewModel.canRetry.value, true)
    }

    @Test
    fun testFetchResultsWithSuccess() {
        coEvery { fetchProvider.getRewards() } returns fetchRewards

        runTest {
            viewModel.fetchRewards()

            advanceUntilIdle()

            Assert.assertEquals(viewModel.fetchResults.value, fetchRewardsData)
            Assert.assertEquals(viewModel.fetchState.value, FetchState.Success)
            Assert.assertEquals(viewModel.errorMessage.value, "")
            Assert.assertEquals(viewModel.canRetry.value, true)

            coVerify(exactly = 1) { fetchProvider.getRewards() }
        }
    }


    @Test
    fun testFetchResultsWithFailure() {
        coEvery { fetchProvider.getRewards() } throws RuntimeException("This should fail")

        runTest {
            viewModel.fetchRewards()

            advanceUntilIdle()

            Assert.assertEquals(viewModel.fetchResults.value, emptyList<FetchResult>())
            Assert.assertEquals(viewModel.fetchState.value, FetchState.Error)
            Assert.assertEquals(viewModel.errorMessage.value, "This should fail")
            Assert.assertEquals(viewModel.canRetry.value, true)

            coVerify(exactly = 1) { fetchProvider.getRewards() }
        }
    }
}

