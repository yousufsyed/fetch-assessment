package com.yousuf.fetch.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yousuf.fetch.R
import com.yousuf.fetch.data.FetchData.Header
import com.yousuf.fetch.data.FetchData.Item
import com.yousuf.fetch.viewmodel.FetchViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FetchRewardsScreen(
    viewModel: FetchViewModel = hiltViewModel(key = "fetch")
) {
    val list = rememberSaveable { viewModel.fetchResults.value }

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        list.forEach { fetchData ->

            when (fetchData) {
                is Header -> {
                    stickyHeader {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)

                        ) {
                            Text(
                                text = stringResource(R.string.section_header, fetchData.listId),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                is Item -> {
                    item {
                        Text(
                            fetchData.name,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(start = 32.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                        )
                    }
                }
            }

        }
    }
}