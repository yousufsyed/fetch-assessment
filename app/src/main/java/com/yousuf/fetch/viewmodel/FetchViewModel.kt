package com.yousuf.fetch.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yousuf.fetch.data.FetchData
import com.yousuf.fetch.data.toFetchData
import com.yousuf.fetch.provider.FetchProvider
import com.yousuf.fetch.viewmodel.FetchState.Empty
import com.yousuf.fetch.viewmodel.FetchState.Loading
import com.yousuf.fetch.viewmodel.FetchState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FetchViewModel @Inject constructor(
    private val fetchProvider: FetchProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var fetchResults = getSavedFetchResults()
        private set

    var errorMessage = getSavedErrorCode()
        private set

    var canRetry = mutableStateOf<Boolean>(true)
        private set

    var fetchState = mutableStateOf<FetchState>(Loading)
        private set

    /**
     * Fetch results from provider,
     * process them to include headers so they can be displayed as a group with sticky headers
     * update the states accordingly
     */
    fun getFetchResults() {
        viewModelScope.launch {
            initLoadingState()

            try {
                val results = fetchProvider.getFetchResults()
                fetchResults.value = results.toFetchData()
                fetchState.value = if (results.isEmpty()) Empty else Success
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Unknown Error"
                fetchState.value = FetchState.Error
            }

            // book keeping after the fetch is done.
            saveResults()
            enableRetryState()
        }
    }

    fun refresh() = getFetchResults()

    fun retry() = getFetchResults()


    private fun enableRetryState() {
        canRetry.value = true
    }

    private fun initLoadingState() {
        fetchState.value = Loading
        canRetry.value = false
    }

    private fun getSavedErrorCode() = mutableStateOf(
        savedStateHandle.get<String>("errorCode") ?: ""
    )

    private fun getSavedFetchResults() = mutableStateOf(
        savedStateHandle.get<List<FetchData>>("fetchResults") ?: mutableListOf<FetchData>()
    )

    private fun saveResults() {
        savedStateHandle["errorCode"] = errorMessage.value
        savedStateHandle["fetchResults"] = fetchResults.value
    }
}

/**
 * Ui states for the fetch screen
 */
sealed class FetchState {
    object Loading : FetchState()
    object Success : FetchState()
    object Empty : FetchState()
    object Error : FetchState()
}