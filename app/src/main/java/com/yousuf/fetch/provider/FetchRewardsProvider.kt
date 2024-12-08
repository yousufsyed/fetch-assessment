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
    private val fetchRewardsClient: FetchRewardsClient
) : FetchProvider {

    private var fetchResultCache: List<FetchResult>? = null

    override suspend fun getRewards(): List<FetchResult> {
        return getResultsFromCache() ?: getRewardsFromNetwork()
    }

    override suspend fun getRewardsFromNetwork(): List<FetchResult> {
        return fetchRewardsClient.fetchRewards().apply {
            saveResults(this)
        }
    }

    private fun getResultsFromCache(): List<FetchResult>? {
        return fetchResultCache
    }

    private fun saveResults(results: List<FetchResult>) {
        fetchResultCache = results
        // Todo save results to db
    }

}