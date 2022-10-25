package com.google.accompanist.swiperefresh.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.swiperefresh.config.Config.defaultEmptyImage
import com.google.accompanist.swiperefresh.config.Config.defaultEmptyTitle
import com.google.accompanist.swiperefresh.ui.footer.toColor

@Composable
fun EmptyView(img: Int = defaultEmptyImage, title: String = defaultEmptyTitle, onClick: () -> Unit = {}) {
    ConstraintLayout(modifier = Modifier.width(500.dp).height(300.dp).padding(0.dp)
        .clickable(onClick = onClick, indication = null, interactionSource = remember { MutableInteractionSource() })) {
        val (imgView, textView) = createRefs()
        Image(painter = painterResource(img), contentDescription = null, modifier = Modifier.wrapContentSize().constrainAs(imgView) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        })
        Text(text = title, fontSize = 16.sp, color = "333333".toColor(), textAlign = TextAlign.Center, modifier = Modifier.constrainAs(textView) {
            start.linkTo(imgView.start)
            end.linkTo(imgView.end)
            top.linkTo(imgView.bottom, 16.dp)
            width = Dimension.fillToConstraints
        })
    }
}