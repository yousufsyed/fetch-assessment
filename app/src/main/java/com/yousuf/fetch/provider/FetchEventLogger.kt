package com.yousuf.fetch.provider

import androidx.compose.runtime.compositionLocalOf
import com.yousuf.fetch.di.LogEventChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

interface FetchEventLogger {
    fun log(message: String)
    fun logInfo(message: String)
    fun logWarning(message: String)
    fun logError(message: String)
    fun logDebug(message: String)
    val events: Flow<LogEvent>
}

/**
 * Apps Events logger..
 */
class DefaultFetchEventLogger @Inject constructor(
    @LogEventChannel private val _events: Channel<LogEvent>
) : FetchEventLogger {

    override val events = _events.receiveAsFlow()

    override fun logWarning(message: String) {
        _events.trySend(LogEvent(message, Level.Warning))
    }

    override fun logDebug(message: String) {
        _events.trySend(LogEvent(message, Level.Debug))
    }

    override fun log(message: String) {
        _events.trySend(LogEvent(message, Level.Verbose))
    }

    override fun logError(message: String) {
        _events.trySend(LogEvent(message, Level.Error))
    }

    override fun logInfo(message: String) {
        _events.trySend(LogEvent(message, Level.Info))
    }
}


data class LogEvent(val name: String, val level: Level)

enum class Level {
    Warning,
    Debug,
    Verbose,
    Error,
    Info
}

val LocalFetchEventLogger = compositionLocalOf<FetchEventLogger?> { null }