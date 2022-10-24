package com.bjx.swiperefresh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bjx.swiperefresh.ui.theme.SwipeRefreshComposeTheme
import com.google.accompanist.swiperefresh.SmartSwipeRefresh
import com.google.accompanist.swiperefresh.SmartSwipeStateFlag
import com.google.accompanist.swiperefresh.footer.BjxRefreshFooter
import com.google.accompanist.swiperefresh.header.BjxMedaiRefreshHeader
import com.google.accompanist.swiperefresh.rememberSmartSwipeRefreshState

class MainActivity : ComponentActivity() {
    private val viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeRefreshComposeTheme {
                NewsList(viewModel)
            }
            viewModel.getData()
        }
    }

    @Composable
    fun NewsList(viewModel: MainViewModel) {
        val scrollState = rememberLazyListState()
        val mainUiState = viewModel.allList.observeAsState()
        val refreshState = rememberSmartSwipeRefreshState()
        SmartSwipeRefresh(state = refreshState, onRefresh = {
            viewModel.getData()
        }, onLoadMore = {
            viewModel.loadData()
        }, headerIndicator = {//可不传，有默认header
            BjxMedaiRefreshHeader(refreshState.refreshFlag)
        }, footerIndicator = {//可不传，有默认footer
            BjxRefreshFooter(refreshState.loadMoreFlag)
        }) {
            LaunchedEffect(refreshState.smartSwipeRefreshAnimateFinishing) {
                if (refreshState.smartSwipeRefreshAnimateFinishing.isFinishing && !refreshState.smartSwipeRefreshAnimateFinishing.isRefresh) {
                    scrollState.animateScrollToItem(scrollState.firstVisibleItemIndex + 1)
                }
            }
            LaunchedEffect(mainUiState.value) {
                mainUiState.value?.let {
                    if (!it.isLoading) {
                        refreshState.refreshFlag = when (it.refreshSuccess) {
                            true -> SmartSwipeStateFlag.SUCCESS
                            false -> SmartSwipeStateFlag.ERROR
                            else -> SmartSwipeStateFlag.IDLE
                        }
                        refreshState.loadMoreFlag = when (it.loadMoreSuccess) {
                            true -> SmartSwipeStateFlag.SUCCESS
                            false -> SmartSwipeStateFlag.ERROR
                            else -> SmartSwipeStateFlag.IDLE
                        }
                    }
                }
            }
            mainUiState.value?.data?.let { list ->
                LazyColumn(state = scrollState) {
                    items(list.size) { index ->
                        Row(Modifier.padding(16.dp)) {
                            Image(
                                painter = painterResource(R.drawable.app_icon),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(text = list[index],
                                style = MaterialTheme.typography.subtitle2,
                                modifier = Modifier.weight(1f).align(Alignment.CenterVertically))
                        }
                    }
                }
            }
        }
    }

}
