package com.yousuf.fetch.provider

import androidx.compose.runtime.compositionLocalOf
import com.yousuf.fetch.di.SnackbarEventChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

interface SnackbarDelegate {
    val events : Flow<SnackbarEvent>
    fun showSnackbar(message: String, action: SnackbarAction? = null)
}

class DefaultSnackbarDelegate @Inject constructor(
    @SnackbarEventChannel private val _events: Channel<SnackbarEvent>
) : SnackbarDelegate {

    override val events = _events.receiveAsFlow()

    override fun showSnackbar(message: String, action: SnackbarAction?) {
        _events.trySend(SnackbarEvent(message, action))
    }
}


data class SnackbarAction(val name: String, val action: () -> Unit)

data class SnackbarEvent(val message: String, val action: SnackbarAction? = null)

val LocalMessageDelegate = compositionLocalOf<SnackbarDelegate?> { null }