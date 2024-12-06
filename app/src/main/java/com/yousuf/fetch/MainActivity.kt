package com.yousuf.fetch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.yousuf.fetch.ui.screen.FetchInfo
import com.yousuf.fetch.ui.theme.FetchAssesmentTheme
import com.yousuf.fetch.viewmodel.FetchViewModel
import com.yousuf.weatherapp.provider.MessageDelegate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchAssesmentTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                HandleSnackbar(snackbarHostState)
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background),
                    topBar = { Appbar() },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    FetchInfo(
                        modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HandleSnackbar(snackbarHostState: SnackbarHostState) {

    val scope = rememberCoroutineScope()

    ObserveAsEvents( flow = MessageDelegate.events, snackbarHostState) { event ->
        scope.launch {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Appbar(viewModel: FetchViewModel = hiltViewModel()) {
    TopAppBar(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary),
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(
                onClick = { /* todo: handle home click*/ },
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home"
                )
            }
        },
        actions = {
            IconButton(
                onClick = { viewModel.refresh() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh"
                )
            }
        }
    )
}