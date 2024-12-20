package com.yousuf.fetch.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yousuf.fetch.R
import com.yousuf.fetch.provider.LocalMessageDelegate


/**
 * Basic UI for loading screen, with scope for future scalability.
 */
@Composable
fun LoadingScreen() = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    horizontalAlignment = Alignment.Companion.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    Text(
        text = stringResource(id = R.string.loading_message),
        fontSize = 16.sp,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun PartialLoadScreen() {
    LocalMessageDelegate.current?.showSnackbar(
        message = stringResource(id = R.string.loading_message)
    )
}