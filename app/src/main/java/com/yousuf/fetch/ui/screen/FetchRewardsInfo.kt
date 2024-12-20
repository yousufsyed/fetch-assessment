package com.yousuf.fetch.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import com.yousuf.fetch.viewmodel.FetchState
import com.yousuf.fetch.viewmodel.FetchViewModel

@Composable
fun FetchRewardsInfo(
    viewModel: FetchViewModel
) {
    LaunchedEffect(viewModel) {
        if(viewModel.fetchState.value == FetchState.Init) {
            viewModel.fetchRewards()
        }
    }

    val state by rememberSaveable { viewModel.fetchState }

    when (state) {
        FetchState.Success -> FetchRewardsScreen(viewModel)
        FetchState.Empty -> EmptyScreen()
        FetchState.Error -> ErrorScreen(viewModel)
        FetchState.Loading -> LoadingScreen()
//        FetchState.PartialError -> PartialErrorScreen(viewModel)
//        FetchState.PartialLoad -> PartialLoadScreen()
        else -> {}
    }
}