package com.yousuf.fetch.network

import com.yousuf.fetch.network.FetchError.NetworkError
import com.yousuf.fetch.network.data.FetchParseException
import com.yousuf.fetch.network.data.FetchResult
import com.yousuf.fetch.network.data.toFetchResults
import com.yousuf.fetch.provider.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface FetchRewardsClient {
    suspend fun fetchRewards(): List<FetchResult>
}

/***
 * Network client to fetch weather data from api
 */
class DefaultFetchRewardsClient @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val fetchRewardsService: FetchRewardsService
) : FetchRewardsClient {

    // fetch data from api
    override suspend fun fetchRewards(): List<FetchResult> {
        return withContext(dispatcherProvider.io) {
            try {
                fetchRewardsService.getRewards().let { response ->
                    if (response.isSuccessful && response.body() != null) {
                        response.body()!!.toFetchResults()
                    } else {
                        throw NetworkError()
                    }
                }
            } catch (fpe: FetchParseException) {
                throw fpe
            } catch (io: Exception) {
                throw NetworkError()
            }
        }
    }
}
sealed class FetchError(val code: Int) : RuntimeException() {
    class ParseError() : FetchError(300) //"Failed to parse response")
    class NetworkError() : FetchError(400) //"Failed to fetch results from network"
    class UnknownError() : FetchError(100) //"An unknown error occurred"
}