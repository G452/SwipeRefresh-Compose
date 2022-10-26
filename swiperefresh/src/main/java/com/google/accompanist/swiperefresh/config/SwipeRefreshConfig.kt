package com.google.accompanist.swiperefresh.config

import com.google.accompanist.swiperefresh.R

/**
 * 可全局修改一些默认值
 * 需在Application修改
 * */
class SwipeRefreshConfig {
    companion object {
        var isBjxMedia = false
        val defaultEmptyImage get() = if (isBjxMedia) R.drawable.search_nodata else R.drawable.resume_empty
        val defaultRefreshImages get() = if (isBjxMedia) AnimImage.loadingMediaList else AnimImage.loadingList
        var defaultEmptyTitle = "暂无数据"
        //还需要什么继续加...
    }
}