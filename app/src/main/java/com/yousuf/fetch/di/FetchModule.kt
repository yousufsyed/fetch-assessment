package com.yousuf.fetch.di

import com.yousuf.fetch.network.DefaultFetchClient
import com.yousuf.fetch.network.FetchClient
import com.yousuf.fetch.network.FetchService
import com.yousuf.fetch.provider.DefaultDispatcherProvider
import com.yousuf.fetch.provider.DefaultFetchProvider
import com.yousuf.fetch.provider.DispatcherProvider
import com.yousuf.fetch.provider.FetchProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class FetchModule {

    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://fetch-hiring.s3.amazonaws.com")
        .client(OkHttpClient.Builder().build())
        .build()

    @Provides
    fun provideFetchService(retrofit: Retrofit): FetchService {
        return retrofit.create(FetchService::class.java)
    }

    @Provides
    fun provideFetchClient(impl: DefaultFetchClient): FetchClient = impl

    @Provides
    fun providesFetchProvider(impl: DefaultFetchProvider): FetchProvider = impl

    @Provides
    fun providesDispatchers(impl: DefaultDispatcherProvider): DispatcherProvider = impl

}