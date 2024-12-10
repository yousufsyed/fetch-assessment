package com.yousuf.fetch.di

import androidx.room.Query
import com.yousuf.fetch.provider.DefaultFetchEventLogger
import com.yousuf.fetch.provider.DefaultSnackbarDelegate
import com.yousuf.fetch.provider.FetchEventLogger
import com.yousuf.fetch.provider.LogEvent
import com.yousuf.fetch.provider.SnackbarDelegate
import com.yousuf.fetch.provider.SnackbarEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.Channel
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EventsModule {

    @Provides
    fun providesEventLogger(impl: DefaultFetchEventLogger): FetchEventLogger = impl

    @Provides
    fun providesSnackbarDelegate(impl: DefaultSnackbarDelegate): SnackbarDelegate = impl

    @Singleton @LogEventChannel
    @Provides
    fun providesEventLoggerChannel(): Channel<LogEvent> = Channel<LogEvent>()

    @Singleton @SnackbarEventChannel
    @Provides
    fun providesSnackbarEventChannel(): Channel<SnackbarEvent> = Channel<SnackbarEvent>()
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LogEventChannel

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SnackbarEventChannel