package com.yousuf.fetch.network

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
                        // for now just throwing an exception if the api response is failure,
                        // this can be refined to handle different type of errors
                        throw FetchDataException()
                    }
                }
            } catch (e: FetchDataException) {
                throw e
            } catch (e: FetchParseException) {
                throw e
            } catch (e: Exception) {
                throw FetchNetworkException()
            }
        }
    }
}

class FetchDataException() : RuntimeException("Failed to fetch results.")
class FetchNetworkException() :
    RuntimeException("Network error occurred while trying to fetch results.")