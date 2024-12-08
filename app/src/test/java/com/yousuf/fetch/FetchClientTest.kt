package com.yousuf.fetch

import com.yousuf.fetch.network.DefaultFetchRewardsClient
import com.yousuf.fetch.network.FetchDataException
import com.yousuf.fetch.network.FetchRewardsClient
import com.yousuf.fetch.network.FetchRewardsService
import com.yousuf.fetch.provider.DispatcherProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class FetchClientTest {

    private val mockWebServer = MockWebServer()
    private lateinit var fetchRewardsService: FetchRewardsService
    private lateinit var fetchRewardsClient: FetchRewardsClient

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcherProvider = mockk<DispatcherProvider> {
        every { io } returns UnconfinedTestDispatcher()
    }

    @Before
    fun setup() {
        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

        mockWebServer.start()
        fetchRewardsService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .build()
            .create(FetchRewardsService::class.java)

        fetchRewardsClient = DefaultFetchRewardsClient(dispatcherProvider, fetchRewardsService)
    }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test(expected = FetchDataException::class)
    fun testFetchResultFailure() {
        runBlocking {
            MockResponse().setResponseCode(404).also {
                mockWebServer.enqueue(it)
            }
            fetchRewardsClient.fetchRewards()
        }
    }

    @Test
    fun testFetchResultSuccess() {
        runBlocking {
            val response = MockResponse()
                .setResponseCode(200)
                .setBody(fetchRewardsResponse.trimIndent())

            mockWebServer.enqueue(response)

            val result = fetchRewardsClient.fetchRewards()
            Assert.assertEquals(result, fetchRewards)
        }
    }
}

