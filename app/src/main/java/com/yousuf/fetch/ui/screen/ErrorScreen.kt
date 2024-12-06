package com.yousuf.fetch.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yousuf.fetch.R
import com.yousuf.fetch.viewmodel.FetchViewModel

@Composable
fun ErrorScreen(viewModel: FetchViewModel = hiltViewModel()) {

    val errorMessage = rememberSaveable("error") { viewModel.errorMessage }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )

            Button(
                modifier = Modifier.padding(top = 16.dp),
                enabled = remember { viewModel.canRetry.value },
                onClick = { viewModel.retry() }
            ) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}