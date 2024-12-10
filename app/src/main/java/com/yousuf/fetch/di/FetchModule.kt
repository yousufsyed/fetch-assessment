package com.yousuf.fetch.di

import com.yousuf.fetch.network.DefaultFetchRewardsClient
import com.yousuf.fetch.network.FetchRewardsClient
import com.yousuf.fetch.network.FetchRewardsService
import com.yousuf.fetch.provider.DefaultDispatcherProvider
import com.yousuf.fetch.provider.DefaultFetchEventLogger
import com.yousuf.fetch.provider.DefaultFetchProvider
import com.yousuf.fetch.provider.DefaultSnackbarDelegate
import com.yousuf.fetch.provider.DispatcherProvider
import com.yousuf.fetch.provider.FetchEventLogger
import com.yousuf.fetch.provider.FetchProvider
import com.yousuf.fetch.provider.LogEvent
import com.yousuf.fetch.provider.SnackbarDelegate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.Channel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module (includes = [EventsModule::class, NetworkModule::class])
@InstallIn(SingletonComponent::class)
class FetchModule {

    @Provides
    @Singleton
    fun providesFetchProvider(impl: DefaultFetchProvider): FetchProvider = impl

    @Provides
    @Singleton
    fun providesDispatchers(impl: DefaultDispatcherProvider): DispatcherProvider = impl

}