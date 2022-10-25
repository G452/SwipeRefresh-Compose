package com.google.accompanist.swiperefresh.config

import com.google.accompanist.swiperefresh.R

/**
 * 可全局修改一些默认值
 * 需在Application修改
* */
object Config {
    var isBjxMedia = false
    var defaultEmptyImage = if (isBjxMedia) R.drawable.search_nodata else R.drawable.resume_empty
    var defaultEmptyTitle = "暂无数据"
    //还需要什么继续加...
}