package com.yousuf.fetch.di

import com.yousuf.fetch.network.DefaultFetchRewardsClient
import com.yousuf.fetch.network.FetchRewardsClient
import com.yousuf.fetch.network.FetchRewardsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://fetch-hiring.s3.amazonaws.com")
        .client(OkHttpClient.Builder().build())
        .build()

    @Provides
    fun provideFetchService(retrofit: Retrofit): FetchRewardsService {
        return retrofit.create(FetchRewardsService::class.java)
    }

    @Provides
    fun provideFetchClient(impl: DefaultFetchRewardsClient): FetchRewardsClient = impl
}