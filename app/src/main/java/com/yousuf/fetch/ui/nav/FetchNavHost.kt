package com.yousuf.fetch.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yousuf.fetch.ui.screen.FetchRewardsInfo
import com.yousuf.fetch.viewmodel.FetchViewModel

enum class Screen {
    Home
}

sealed class Destination(val route: String) {
    object Home : Destination(Screen.Home.name)
}

@Composable
fun FetchNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FetchViewModel = hiltViewModel(key="fetch"),
    startDestination: Destination = Destination.Home
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Destination.Home.route) {
            FetchRewardsInfo(viewModel)
        }
        //add new routes as needed
    }
}