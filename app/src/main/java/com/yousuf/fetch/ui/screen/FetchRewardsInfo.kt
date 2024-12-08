package com.yousuf.fetch.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
fun FetchRewardsInfo(
    modifier: Modifier = Modifier,
    viewModel: FetchViewModel = hiltViewModel(key = "fetch")
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    LaunchedEffect(viewModel) {
        if (viewModel.fetchState.value == FetchState.Init) {
            viewModel.fetchRewards()
        }
    }

    val state by rememberSaveable { viewModel.fetchState }

    when (state) {
        FetchState.Success -> FetchRewardsScreen()
        FetchState.Empty -> EmptyScreen()
        FetchState.Error -> ErrorScreen()
        else -> LoadingScreen()
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
