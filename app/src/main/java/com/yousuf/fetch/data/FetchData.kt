package com.yousuf.fetch.data

import android.os.Parcelable
import com.yousuf.fetch.data.FetchData.Header
import com.yousuf.fetch.data.FetchData.Item
import com.yousuf.fetch.network.data.FetchResult
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class FetchData : Parcelable {
    data class Header(val listId: Int) : FetchData()

    data class Item(val listId: Int, val name: String) : FetchData()
}

/**
 * Convert the sorted list of results into a new list that includes headers for each listId
 */
fun List<FetchResult>.toFetchData(): List<FetchData> {
    val results = mutableListOf<FetchData>()
    this.groupBy { it.listId }.forEach {
        results.add(Header(it.key))
        results.addAll(it.value.map { Item(it.listId, it.name) })
    }
    return results
}
