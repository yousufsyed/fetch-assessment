package com.yousuf.fetch.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface FetchRewardsService {

    @GET("/hiring.json")
    suspend fun getRewards(): Response<ResponseBody>

}