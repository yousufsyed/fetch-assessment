package com.yousuf.fetch.provider

import com.yousuf.fetch.network.FetchRewardsClient
import com.yousuf.fetch.network.data.FetchResult
import javax.inject.Inject

interface FetchProvider {
    /**
     * Fetch results from cache if available,
     * otherwise fetch from network.
     */
    suspend fun getRewards(): List<FetchResult>

    /**
     * Fetch results from network and save them to cache.
     */
    suspend fun getRewardsFromNetwork(): List<FetchResult>
}

class DefaultFetchProvider @Inject constructor(
    private val fetchRewardsClient: FetchRewardsClient,
    private val fetchEventLogger: FetchEventLogger
) : FetchProvider {

    private var fetchResultCache: List<FetchResult>? = null

    override suspend fun getRewards(): List<FetchResult> {
        return getResultsFromCache() ?: getRewardsFromNetwork()
    }

    override suspend fun getRewardsFromNetwork(): List<FetchResult> {
        fetchEventLogger.logDebug("fetching results from network")
        return fetchRewardsClient.fetchRewards().apply {
            fetchEventLogger.logDebug("received parsed results from client")
            saveResults(this)
        }
    }

    private fun getResultsFromCache(): List<FetchResult>? {
        return fetchResultCache?.also {
            fetchEventLogger.logDebug("returning results from cache")
        }
    }

    private fun saveResults(results: List<FetchResult>) {
        fetchEventLogger.logDebug("saving results to cache")
        fetchResultCache = results

        // Todo save results to db
        // eventLogger.sendLogDebug("saving results to db")
    }

}