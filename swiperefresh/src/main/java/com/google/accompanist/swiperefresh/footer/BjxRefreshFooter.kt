package com.google.accompanist.swiperefresh.footer

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.swiperefresh.AnimImage
import com.google.accompanist.swiperefresh.R
import com.google.accompanist.swiperefresh.SmartSwipeStateFlag
import kotlinx.coroutines.delay

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BjxRefreshFooter(flag: SmartSwipeStateFlag) {
    val loadImg = remember { mutableStateOf(R.drawable.loading_00) }
    LaunchedEffect(flag) {
        var index = 0
        var isLoad = true
        while (isLoad) {
            if (flag == SmartSwipeStateFlag.REFRESHING) {
                loadImg.value = AnimImage.loadMoreList[index]
                if (index == AnimImage.loadMoreList.lastIndex) index = 0 else index++
                delay(40)
            }
            else {
                loadImg.value = AnimImage.loadMoreList[0]
                isLoad = false
            }
        }
    }
    ConstraintLayout(modifier = Modifier.fillMaxWidth().height(80.dp).background(Color.White)) {
        val (img) = createRefs()
        Image(painter = painterResource(loadImg.value), contentDescription = null, modifier = Modifier.constrainAs(img) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        })
    }
}
