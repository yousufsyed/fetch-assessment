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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.yousuf.fetch.provider.FetchEventLogger
import com.yousuf.fetch.provider.LocalFetchEventLogger
import com.yousuf.fetch.provider.LocalMessageDelegate
import com.yousuf.fetch.provider.SnackbarDelegate
import com.yousuf.fetch.ui.eventHandler.HandleEventLogger
import com.yousuf.fetch.ui.eventHandler.HandleSnackbar
import com.yousuf.fetch.ui.nav.FetchNavHost
import com.yousuf.fetch.ui.theme.FetchAssesmentTheme
import com.yousuf.fetch.viewmodel.FetchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var eventLogger: FetchEventLogger
    @Inject
    lateinit var snackbarEventDelegate: SnackbarDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchAssesmentTheme {
                RegisterProviders(eventLogger, snackbarEventDelegate) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    HandleSnackbar(snackbarHostState)
                    HandleEventLogger()
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background),
                        topBar = { Appbar() },
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                    ) { innerPadding ->
                        FetchNavHost(
                            modifier =  Modifier.fillMaxSize().padding(innerPadding),
                            navController = rememberNavController()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Appbar(
    viewModel: FetchViewModel = hiltViewModel(key="fetch")
) {
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

/**
 * Register providers for the app that can be used by composable via LocalProviders
 */
@Composable
fun RegisterProviders(
    eventLogger: FetchEventLogger,
    snackbarEventDelegate: SnackbarDelegate,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalFetchEventLogger provides eventLogger,
        LocalMessageDelegate provides snackbarEventDelegate,
        content = content
    )
}