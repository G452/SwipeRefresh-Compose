package com.bjx.swiperefresh

import android.graphics.drawable.PaintDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bjx.swiperefresh.ui.theme.SwipeRefreshComposeTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeRefreshComposeTheme {
                Sample(list)
            }
        }
    }
    val list = arrayListOf(
        "Jetpack Compose过于早期，只有基础控件，甚至周边设施都是之后才会有的。\n",
        "在去年立项的时候Jetpack Compose刚刚alpha02，什么",
        "周边设施都没有，比如Navigation一开始是借用",
        "的fragment去做的，后来Navigation Co",
        "Jetpack Compose过于早期，只有基础控件，甚至周边设施都是之后才会有的。\n",
        "在去年立项的时候Jetpack Compose刚刚alpha02，什么",
        "周边设施都没有，比如Navigation一开始是借用",
        "的fragment去做的，后来Navigation Co",
        "Jetpack Compose过于早期，只有基础控件，甚至周边设施都是之后才会有的。\n",
        "在去年立项的时候Jetpack Compose刚刚alpha02，什么",
        "周边设施都没有，比如Navigation一开始是借用",
        "的fragment去做的，后来Navigation Co",
    )
}

@Composable
private fun Sample(list: ArrayList<String> = arrayListOf()) {
    ConstraintLayout {
        val (swip, moreView) = createRefs()
        val listState = rememberLazyListState()
        var refreshing by remember { mutableStateOf(false) }
        LaunchedEffect(refreshing) {
            if (refreshing) {
                delay(2000)
                refreshing = false
            }
        }
        var loadMoreing by remember { mutableStateOf(false) }
        LaunchedEffect(loadMoreing) {
            if (loadMoreing) {
                delay(2000)
                loadMoreing = false
            }
        }
        SwipeRefresh(
            state = rememberSwipeRefreshState(refreshing, loadMoreing),
            onRefresh = {
                refreshing = true
            },
            onLoadMore = {
                loadMoreing = true
            },
            modifier = Modifier.constrainAs(swip) {
                top.linkTo(parent.top)
                bottom.linkTo(moreView.top)
                height = Dimension.fillToConstraints
            }
        ) {
            LazyColumn(state = listState) {
                item {
                    AnimatedVisibility(refreshing) {
                        Text(
                            text = if (refreshing) "下拉刷新中..." else "加载完毕",
                            fontSize = 20.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                items(list.size) { index ->
                    Row(Modifier.padding(16.dp)) {
                        Image(
                            painter = painterResource(R.drawable.app_icon),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = list[index],
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
        AnimatedVisibility(loadMoreing) {
            Text(
                text = if (loadMoreing) "上拉加载中" else "加载完毕",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().constrainAs(moreView) {
                    top.linkTo(swip.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
            )
        }
    }
}

