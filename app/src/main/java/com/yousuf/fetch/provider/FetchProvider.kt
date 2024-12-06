package com.yousuf.fetch.provider

import com.yousuf.fetch.network.FetchClient
import com.yousuf.fetch.network.data.FetchResult
import javax.inject.Inject

interface FetchProvider {
    suspend fun getFetchResults(): List<FetchResult>
    suspend fun getResultsFromNetwork(): List<FetchResult>
}

class DefaultFetchProvider @Inject constructor(
    private val fetchClient: FetchClient
) : FetchProvider {

    private val fetchResultCache: List<FetchResult>? = null

    override suspend fun getFetchResults(): List<FetchResult> {
        return getResultsFromCache() ?: getResultsFromNetwork()
    }

    override suspend fun getResultsFromNetwork(): List<FetchResult> {
        return fetchClient.fetchRewards().apply {
            saveResultsToDb(this)
        }
    }

    private fun getResultsFromCache() : List<FetchResult>? {
        return fetchResultCache
    }

    private fun saveResultsToDb(results: List<FetchResult>) {
        // Todo save results to db
    }

}