package com.google.accompanist.swiperefresh

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import com.google.accompanist.swiperefresh.footer.BjxRefreshFooter
import com.google.accompanist.swiperefresh.header.BjxRefreshHeader
import com.google.accompanist.swiperefresh.header.MyRefreshHeader
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@Composable
fun SmartSwipeRefresh(
    modifier: Modifier = Modifier,
    onRefresh: (suspend () -> Unit)? = null,
    onLoadMore: (suspend () -> Unit)? = null,
    state: SmartSwipeRefreshState,
    isNeedRefresh: Boolean = true,
    isNeedLoadMore: Boolean = true,
    headerThreshold: Dp? = null,
    footerThreshold: Dp? = null,
    headerIndicator: @Composable () -> Unit = { BjxRefreshHeader(state.refreshFlag) },
    footerIndicator: @Composable () -> Unit = { BjxRefreshFooter(state.loadMoreFlag) },
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    LaunchedEffect(Unit) {
        state.indicatorOffsetFlow.collect {
            val currentOffset = with(density) { state.indicatorOffset + it.toDp() }
            // 重点：解决触摸滑动展示header||footer的时候反向滑动另一边的布局被展示出一个白边出来
            // 当footer显示的情况下，希望父布局的偏移量最大只有0dp，防止尾布局会被偏移几个像素
            // 当header显示的情况下，希望父布局的偏移量最小只有0dp，防止头布局会被偏移几个像素
            // 其余的情况下，直接偏移
            state.snapToOffset(when {
                state.footerIsShow -> currentOffset.coerceAtMost(0.dp).coerceAtLeast(-(footerThreshold ?: Dp.Infinity))
                state.headerIsShow -> currentOffset.coerceAtLeast(0.dp).coerceAtMost(headerThreshold ?: Dp.Infinity)
                else -> currentOffset
            })
        }
    }
    LaunchedEffect(state.refreshFlag) {
        when (state.refreshFlag) {
            SmartSwipeStateFlag.REFRESHING -> {
                onRefresh?.invoke()
                state.smartSwipeRefreshAnimateFinishing = state.smartSwipeRefreshAnimateFinishing.copy(isFinishing = false, isRefresh = true)
            }
            SmartSwipeStateFlag.SUCCESS, SmartSwipeStateFlag.ERROR -> {
                delay(50)
                state.animateToOffset(0.dp)
            }
            else -> {}
        }
    }
    LaunchedEffect(state.loadMoreFlag) {
        when (state.loadMoreFlag) {
            SmartSwipeStateFlag.REFRESHING -> {
                onLoadMore?.invoke()
                state.smartSwipeRefreshAnimateFinishing = state.smartSwipeRefreshAnimateFinishing.copy(isFinishing = false, isRefresh = false)
            }
            SmartSwipeStateFlag.SUCCESS, SmartSwipeStateFlag.ERROR -> {
                delay(50)
                state.animateToOffset(0.dp)
            }
            else -> {}
        }
    }
    Box(modifier = modifier.zIndex(-1f)) {
        SubComposeSmartSwipeRefresh(headerIndicator = headerIndicator,
            footerIndicator = footerIndicator,
            isNeedRefresh,
            isNeedLoadMore) { header, footer ->
            Log.v("Loren", "header = $header  footer = $footer")
            val smartSwipeRefreshNestedScrollConnection = remember(state, header, footer) {
                SmartSwipeRefreshNestedScrollConnection(state, header, footer)
            }
            Box(modifier.nestedScroll(smartSwipeRefreshNestedScrollConnection), contentAlignment = Alignment.TopCenter) {
                if (isNeedRefresh) {
                    Box(Modifier.offset(y = -header + state.indicatorOffset)) {
                        headerIndicator()
                    }
                }
                // 因为无法测量出content的高度 所以footer偏移到content布局之下
                Box(modifier = Modifier.offset(y = state.indicatorOffset)) {
                    content()
                    if (isNeedLoadMore) {
                        Box(modifier = Modifier.align(Alignment.BottomCenter).offset(y = footer)) {
                            footerIndicator()
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun SubComposeSmartSwipeRefresh(
    headerIndicator: @Composable () -> Unit,
    footerIndicator: @Composable () -> Unit,
    isNeedRefresh: Boolean,
    isNeedLoadMore: Boolean,
    content: @Composable (header: Dp, footer: Dp) -> Unit,
) {
    SubcomposeLayout { constraints: Constraints ->
        val headerIndicatorPlaceable = subcompose("headerIndicator", headerIndicator).first().measure(constraints)
        val footerIndicatorPlaceable = subcompose("footerIndicator", footerIndicator).first().measure(constraints)
        val contentPlaceable = subcompose("content") {
            content(if (isNeedRefresh) headerIndicatorPlaceable.height.toDp() else 0.dp,
                if (isNeedLoadMore) footerIndicatorPlaceable.height.toDp() else 0.dp)
        }.map {
            it.measure(constraints)
        }.first()
        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.placeRelative(0, 0)
        }
    }
}