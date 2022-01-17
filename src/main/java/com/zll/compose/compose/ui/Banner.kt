package com.zll.compose.compose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.zll.compose.compose.model.BannerModel
import com.zll.compose.compose.theme.DefaultBlackSecondary
import com.zll.compose.util.blackSecondColor
import com.zll.compose.util.blackThreeColor
import kotlinx.coroutines.delay

@ExperimentalPagerApi
@Composable
fun BannerViewCompose(
    banners: List<BannerModel>?,
    height: Dp = 200.dp,
    timeMillis: Long = 3000L,
    onClick: (link: String?) -> Unit = {}
) {
    AnimatedVisibility(visible = !banners.isNullOrEmpty()) {
        val pagerState = rememberPagerState(
            //初始页面
            initialPage = 0,
        )
        val executeChangePage = remember { mutableStateOf(false) }
        var currentPageIndex = 0
        LaunchedEffect(pagerState.currentPage, executeChangePage) {
            if (pagerState.pageCount > 0) {
                delay(timeMillis)
                //这里直接+1就可以循环，前提是infiniteLoop == true
                val page = if (pagerState.currentPage + 1 >= banners!!.size) 0 else pagerState.currentPage + 1
                pagerState.animateScrollToPage(page)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalPager(
                count = banners!!.size, state = pagerState,
                modifier = Modifier
                    .pointerInput(pagerState.currentPage) {
                        awaitPointerEventScope {
                            while (true) {
                                //PointerEventPass.Initial - 本控件优先处理手势，处理后再交给子组件
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                //获取到第一根按下的手指
                                val dragEvent = event.changes.firstOrNull()
                                when {
                                    //当前移动手势是否已被消费
                                    dragEvent!!.positionChangeConsumed() -> {
                                        return@awaitPointerEventScope
                                    }
                                    //是否已经按下(忽略按下手势已消费标记)
                                    dragEvent.changedToDownIgnoreConsumed() -> {
                                        //记录下当前的页面索引值
                                        currentPageIndex = pagerState.currentPage
                                    }
                                    //是否已经抬起(忽略按下手势已消费标记)
                                    dragEvent.changedToUpIgnoreConsumed() -> {
                                        //当页面没有任何滚动/动画的时候pagerState.targetPage为null，这个时候是单击事件
                                        if (pagerState.targetPage == null) return@awaitPointerEventScope
                                        //当pageCount大于1，且手指抬起时如果页面没有改变，就手动触发动画
                                        if (currentPageIndex == pagerState.currentPage && pagerState.pageCount > 1) {
                                            executeChangePage.value = !executeChangePage.value
                                        }
                                    }
                                }
                            }
                        }
                    }
                    .clickable(onClick = { onClick(banners[pagerState.currentPage].url) })
                    .fillMaxSize(),
            ) { index ->
                Image(
                    painter = rememberImagePainter(banners[index].imagePath),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            val bg = if(isSystemInDarkTheme()){
                Color.Black
            }else{
                Color(0x99CCCCCC)
            }
            val textColor = if(isSystemInDarkTheme()){
                Color.White
            }else{
                DefaultBlackSecondary
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(bg)
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    banners[pagerState.currentPage].title ?: "",
                    fontSize = 14.sp, color = textColor,
                    modifier = Modifier.weight(1f, true)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    for (i in banners.indices) {
                        //大小
                        val size = remember { mutableStateOf(5.dp) }
                        size.value = if (pagerState.currentPage == i) 7.dp else 5.dp

                        //颜色
                        val color = if (pagerState.currentPage == i) Color.White else Color.Gray
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color)
                                //当size改变的时候以动画的形式改变
                                .animateContentSize()
                                .size(size.value)
                        )
                        //指示点间的间隔
                        if (i != banners.lastIndex) Spacer(
                            modifier = Modifier
                                .height(0.dp)
                                .width(4.dp)
                        )
                    }
                }
            }


        }
    }
}


