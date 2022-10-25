package com.google.accompanist.swiperefresh.config

data class SwipeUiState<T>(
    var list: List<T>? = null,
    var data: T? = null,
    var isLoading: Boolean = false,
    var refreshSuccess: Boolean? = null,
    var loadMoreSuccess: Boolean? = null,
)