package com.yousuf.fetch.network.data

import com.yousuf.fetch.network.FetchError.ParseError
import okhttp3.ResponseBody
import org.json.JSONArray
import kotlin.Int

/**
 * Convert the response body to a list of fetch results
 * sorted by listId and then by name
 */
fun ResponseBody.toFetchResults(): List<FetchResult> {
    try {
        val body = string()
        val fetchResponse = JSONArray(body)

        val results = mutableListOf<FetchResult>()

        for (i in 0 until fetchResponse.length()) {
            fetchResponse.optJSONObject(i)?.let {
                val name = it.optString("name")
                if (!name.isNullOrBlank() && name.trim() != "null") {
                    results.add(
                        FetchResult(
                            id = it.optInt("id"),
                            listId = it.optInt("listId"),
                            name,
                            nameKey = runCatching {
                                name.substringAfter("Item ").toInt()
                            }.getOrDefault(Int.MAX_VALUE)
                        )
                    )
                }
            }
        }
        // sort results first by listId and then by name
        return results
            .sortedWith(
                compareBy<FetchResult>(
                    { it.listId },
                    { it.nameKey }
                )
            )
    } catch (e: Exception) {
        throw ParseError()
    }

}

class FetchParseException : RuntimeException("Failed to parse fetch response.")

data class FetchResult(
    val id: Int, val listId: Int, val name: String, val nameKey: Int
)