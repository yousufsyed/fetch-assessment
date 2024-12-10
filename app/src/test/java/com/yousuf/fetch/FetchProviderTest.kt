package com.yousuf.fetch

import com.yousuf.fetch.network.FetchRewardsClient
import com.yousuf.fetch.provider.DefaultFetchProvider
import com.yousuf.fetch.provider.FetchEventLogger
import com.yousuf.fetch.provider.FetchProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class FetchProviderTest {

    private lateinit var fetchProvider: FetchProvider
    private lateinit var fetchRewardsClient: FetchRewardsClient
    private val fetchEventLogger = mockk<FetchEventLogger>()

    @Before
    fun setup() {
        fetchRewardsClient = mockk()
        fetchProvider = DefaultFetchProvider(fetchRewardsClient, fetchEventLogger)
    }

    @Test
    fun testFetchResults() {
        coEvery { fetchRewardsClient.fetchRewards() } returns fetchRewards

        runBlocking {
            val results = fetchProvider.getRewards()
            Assert.assertEquals(results, fetchRewards)
            coVerify(exactly = 1) { fetchRewardsClient.fetchRewards() }
        }
    }

    @Test
    fun testMultipleFetchResultsInvocations() {
        coEvery { fetchRewardsClient.fetchRewards() } returns fetchRewards

        runBlocking {
            // invocation #1
            fetchProvider.getRewards()

            // invocation #2
            fetchProvider.getRewards()

            coVerify(exactly = 1) { fetchRewardsClient.fetchRewards() }
        }
    }

    @Test
    fun testFetchResultsFromNetwork() {
        coEvery { fetchRewardsClient.fetchRewards() } returns fetchRewards

        runBlocking {
            val results = fetchProvider.getRewardsFromNetwork()
            Assert.assertEquals(results, fetchRewards)
            coVerify(exactly = 1) { fetchRewardsClient.fetchRewards() }
        }
    }

    @Test
    fun testMultipleFetchResultsFromNetworkInvocations() {
        coEvery { fetchRewardsClient.fetchRewards() } returns fetchRewards

        runBlocking {
            // invocation #1
            fetchProvider.getRewardsFromNetwork()

            // invocation #2
            fetchProvider.getRewardsFromNetwork()

            coVerify(exactly = 2) { fetchRewardsClient.fetchRewards() }
        }
    }

}