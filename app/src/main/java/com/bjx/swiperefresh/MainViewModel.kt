package com.bjx.swiperefresh

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.accompanist.swiperefresh.config.SwipeUiState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val allList = MutableLiveData<SwipeUiState<String>>()
    var list = arrayListOf<String>()

    fun getData() {
        MainScope().launch {
            allList.value = SwipeUiState(list, isLoading = true)
            delay(1500)
            list = arrayListOf(
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
            allList.value = SwipeUiState(list, refreshSuccess = true)
        }
    }

    fun loadData() {
        MainScope().launch {
            allList.value = SwipeUiState(list, isLoading = true)
            delay(1500)
            list.addAll(list)
            allList.value = SwipeUiState(list, loadMoreSuccess = true)
        }
    }

}