package com.bjx.swiperefresh

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.accompanist.swiperefresh.config.SwipeUiState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {


    var allList = arrayListOf<String>()
    val mainListDta = MutableLiveData<SwipeUiState<String>>()
    var isFirst = true
    fun getData() {
        MainScope().launch {
            mainListDta.value = SwipeUiState(allList, isLoading = true)
            pageIndex = 1
            delay(1500)
            if (isFirst) {
                allList = arrayListOf(
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
                isFirst = false
            } else {
                allList.clear()
            }
            mainListDta.value = SwipeUiState(allList, refreshSuccess = allList.isNotEmpty())
        }
    }

    var pageIndex = 1
    fun loadData() {
        MainScope().launch {
            mainListDta.value = mainListDta.value?.apply {
                isLoading = true
            }
            delay(1500)
            pageIndex++
            val isHaveData = pageIndex <= 3//模拟只有3页数据
            if (isHaveData) allList.addAll(allList)
            mainListDta.value = SwipeUiState(allList, loadMoreSuccess = isHaveData)
        }
    }

}