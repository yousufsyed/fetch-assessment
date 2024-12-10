package com.yousuf.fetch.ui.eventHandler

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.yousuf.fetch.ObserveAsEvents
import com.yousuf.fetch.provider.LocalMessageDelegate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HandleSnackbar(snackbarHostState: SnackbarHostState) {

    LocalMessageDelegate.current?.let {
        val scope = rememberCoroutineScope()
        ObserveAsEvents(flow = it.events, snackbarHostState) { event ->
            scope.launch(Dispatchers.Main.immediate) {
                snackbarHostState.currentSnackbarData?.dismiss()
                val result = snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action?.name,
                    duration = SnackbarDuration.Short
                )

                when (result) {
                    SnackbarResult.ActionPerformed -> event.action?.action?.invoke()
                    SnackbarResult.Dismissed -> {}
                }
            }
        }
    }
}