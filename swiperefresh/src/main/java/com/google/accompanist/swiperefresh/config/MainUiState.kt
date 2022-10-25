package com.google.accompanist.swiperefresh.config

data class SwipeUiState<T>(
    var List: List<T>? = null,
    var Data: T? = null,
    var isLoading: Boolean = false,
    var refreshSuccess: Boolean? = null,
    var loadMoreSuccess: Boolean? = null,
)