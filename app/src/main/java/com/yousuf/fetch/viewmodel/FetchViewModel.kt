package com.yousuf.fetch.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yousuf.fetch.data.FetchData
import com.yousuf.fetch.data.toFetchData
import com.yousuf.fetch.network.FetchError
import com.yousuf.fetch.network.data.FetchResult
import com.yousuf.fetch.provider.FetchEventLogger
import com.yousuf.fetch.provider.FetchProvider
import com.yousuf.fetch.viewmodel.FetchState.Empty
import com.yousuf.fetch.viewmodel.FetchState.Loading
import com.yousuf.fetch.viewmodel.FetchState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import kotlin.time.TimeSource
import kotlin.time.TimeSource.Monotonic.ValueTimeMark

typealias RewardsAction = suspend () -> List<FetchResult>

@HiltViewModel
class FetchViewModel @Inject constructor(
    private val fetchProvider: FetchProvider,
    private val fetchEventLogger: FetchEventLogger,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var fetchResults = getSavedFetchResults()
        private set

    var errorMessage = getSavedErrorCode()
        private set

    var canRetry = mutableStateOf<Boolean>(true)
        private set

    var fetchState = mutableStateOf<FetchState>(FetchState.Init)
        private set

    fun fetchRewards() {
        fetchEventLogger.logInfo("fetching rewards...")
        getRewards { fetchProvider.getRewards() }
    }

    fun refresh() {
        fetchEventLogger.logInfo("refresh...")
        getRewards { fetchProvider.getRewardsFromNetwork() }
    }

    fun retry() {
        fetchEventLogger.logInfo("retrying...")
        getRewards { fetchProvider.getRewardsFromNetwork() }
    }

    /**
     * Fetch results from provider,
     *  - if success, process results to include headers
     *  - update the states accordingly
     */
    private fun getRewards(rewardsAction: RewardsAction) {
        viewModelScope.launch {
            initLoadingState() //show loading state
            fetchAndProcessResults(rewardsAction)
            saveResults()
            enableRetryState()
        }
    }

    private suspend fun fetchAndProcessResults(rewardsAction: RewardsAction) {
        val startTime = TimeSource.Monotonic.markNow() // track start of the fetch call

        val state = try {
            rewardsAction.invoke()
                .apply {
                    fetchResults.value = this.toFetchData()
                }.let {
                    if (it.isEmpty()) {
                        fetchEventLogger.logInfo("received empty results...")
                        fetchEventLogger.logInfo("transitioning to Empty state...")
                        Empty
                    } else {
                        fetchEventLogger.logInfo("received results...")
                        fetchEventLogger.logInfo("transitioning to Success state...")
                        Success
                    }
                }
        } catch (e: FetchError) {
            fetchEventLogger.logError("received error results..., ${e.message.orEmpty()}")
            errorMessage.intValue = e.code

            fetchEventLogger.logInfo("transitioning to Error state...")
            FetchState.Error
        } catch (e: Exception) {
            fetchEventLogger.logError("received error results..., ${e.message.orEmpty()}")
            errorMessage.intValue = 100

            fetchEventLogger.logInfo("transitioning to Error state...")
            FetchState.Error
        }

        // update state and apply delay if needed.
        updateState(state, startTime)
    }

    // check if we should delay the state load to avoid jitter
    private suspend fun updateState(state: FetchState, startTime: ValueTimeMark) {
        val endTime = TimeSource.Monotonic.markNow() // track start of the fetch call
        val duration = endTime - startTime // calculate the duration of the fetch call
        if (duration.inWholeMilliseconds < 1000) {
            // upon retry, the transition is too fast, producing a flickering effect.
            // so introducing a delay to ensure the transition is smooth.
            delay(1000)
            fetchEventLogger.logInfo("mimic delay for smoother state transition...")
        }
        fetchState.value = state
    }

    private fun enableRetryState() {
        canRetry.value = true
    }

    private fun initLoadingState() {
        fetchEventLogger.logInfo("transitioning to loading state...")
        fetchState.value = Loading
        errorMessage.intValue = 0
        canRetry.value = false
    }

    private fun getSavedErrorCode() = mutableIntStateOf(
        savedStateHandle.get<Int>("errorCode") ?: 0
    )

    private fun getSavedFetchResults() = mutableStateOf(
        savedStateHandle.get<List<FetchData>>("fetchResults") ?: mutableListOf<FetchData>()
    )

    private fun saveResults() {
        fetchEventLogger.logInfo("saving results to savedStateHandler")
        savedStateHandle["errorCode"] = errorMessage.intValue
        savedStateHandle["fetchResults"] = fetchResults.value
    }
}

/**
 * Ui states for the fetch screen
 */
@Parcelize
sealed class FetchState : Parcelable {
    object Init : FetchState()
    object Loading : FetchState()
    object Success : FetchState()
    object Empty : FetchState()
    object Error : FetchState()
}