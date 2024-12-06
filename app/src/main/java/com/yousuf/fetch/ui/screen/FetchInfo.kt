package com.yousuf.fetch.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yousuf.fetch.R
import com.yousuf.fetch.viewmodel.FetchState
import com.yousuf.fetch.viewmodel.FetchViewModel

@Composable
fun FetchInfo(
    modifier: Modifier = Modifier,
    viewModel: FetchViewModel = hiltViewModel()
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    // upon launch, fetch the data from the network
    LaunchedEffect("fetch") { viewModel.getFetchResults() }

    val savableState = listSaver<FetchState, Any>(
        save = {
            when (it) {
                FetchState.Loading -> listOf("Loading")
                is FetchState.Error -> listOf("Error")
                is FetchState.Empty -> listOf("Empty")
                is FetchState.Success -> listOf("Success")
            }
        },
        restore = {
            when (it[0]) {
                "Error" -> FetchState.Error
                "Success" -> FetchState.Success
                "Empty" -> FetchState.Empty
                else -> FetchState.Loading
            }
        }
    )
    val state by rememberSaveable(
        stateSaver = savableState,
        init = { viewModel.fetchState }
    )

    when (state) {
        FetchState.Loading -> LoadingScreen()
        FetchState.Success -> FetchScreen()
        FetchState.Empty -> EmptyScreen()
        FetchState.Error -> ErrorScreen()
    }

}

/**
 * Basic UI for loading screen, with scope for future scalability.
 */
@Composable
fun LoadingScreen() {
    Text(
        text = stringResource(id = R.string.loading_message),
        fontSize = 16.sp,
        style = MaterialTheme.typography.bodyLarge
    )
}

/**
 * Basic UI for loading screen, with scope for future scalability.
 */
@Composable
fun EmptyScreen() {
    Text(
        text = stringResource(id = R.string.empty_results_message),
        fontSize = 16.sp,
        style = MaterialTheme.typography.bodyLarge
    )
}
