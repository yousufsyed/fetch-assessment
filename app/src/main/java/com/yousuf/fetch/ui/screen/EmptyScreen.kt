package com.yousuf.fetch.ui.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.yousuf.fetch.R

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
