package com.yousuf.fetch

import com.yousuf.fetch.data.FetchData.Header
import com.yousuf.fetch.data.FetchData.Item
import com.yousuf.fetch.network.data.FetchResult

val fetchRewardsResponse = """ 
[
    {
        "id": 1,
        "listId": 2,
        "name": null
    },
    {"id": 2,
        "listId": 2,
        "name": ""
    },
    {
        "id": 3,
        "listId": 1,
        "name": "Item 1"
    },
    {
        "id": 4,
        "listId": 2,
        "name": "Item 30"
    },
    {
        "id": 5,
        "listId": 1,
        "name": "Item 15"
    },
    {
        "id": 6,
        "listId": 2,
        "name": "Item 10"
    }
]
""".trimIndent()

val fetchRewards = listOf(
    FetchResult(3, 1, "Item 1", 1),
    FetchResult(5, 1, "Item 15", 15),
    FetchResult(6, 2, "Item 10", 10),
    FetchResult(4, 2, "Item 30", 30)
)

val fetchRewardsData = listOf(
    Header(1),
    Item(1, "Item 1"),
    Item(1, "Item 15"),
    Header(2),
    Item(2, "Item 10"),
    Item(2, "Item 30")
)