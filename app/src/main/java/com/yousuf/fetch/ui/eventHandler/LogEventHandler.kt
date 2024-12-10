package com.yousuf.fetch.ui.eventHandler

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.yousuf.fetch.ObserveAsEvents
import com.yousuf.fetch.provider.Level
import com.yousuf.fetch.provider.LocalFetchEventLogger
import kotlinx.coroutines.launch

private const val TAG = "Fetch-Rewards"

@Composable
fun HandleEventLogger() {
    LocalFetchEventLogger.current?.let {
       // val scope = rememberCoroutineScope()
        ObserveAsEvents(flow = it.events) { event ->
            when (event.level) {
                    Level.Verbose -> Log.v(TAG, event.name)
                    Level.Warning -> Log.w(TAG, event.name)
                    Level.Debug -> Log.d(TAG, event.name)
                    Level.Error -> Log.e(TAG, event.name)
                    Level.Info -> Log.i(TAG, event.name)
            }
        }
    }
}