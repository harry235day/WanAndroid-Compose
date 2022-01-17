package com.zll.compose.compose.page.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.zll.compose.compose.MainDestinations
import com.zll.compose.compose.page.home.HomeItemView
import com.zll.compose.compose.page.home.getAuthor
import com.zll.compose.compose.ui.BaseView
import com.zll.compose.compose.ui.NestTopAndContentView
import com.zll.compose.compose.ui.SwipeRefreshContent
import com.zll.compose.compose.ui.TopBar
import com.zll.compose.compose.vm.SquareViewModel
import com.zll.compose.util.blueColor
import kotlinx.coroutines.launch


@Composable
fun SquareViewCompose(modifier: Modifier = Modifier, navController: NavHostController, drawerClick: () -> Unit = {}) {

    val homeViewModel: SquareViewModel = viewModel()
    val squareListData = homeViewModel.squareListData.collectAsLazyPagingItems()
    val showButton = remember {
        derivedStateOf {
            homeViewModel.squareLazyListState.firstVisibleItemIndex > 2
        }
    }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        NestTopAndContentView(
            modifier = modifier,
            topBar = {
                TopBar(
                    title = "广场",
                    leftIcon = Icons.Default.Menu,
                    menuIcon = Icons.Filled.Add,
                    leftClick = drawerClick
                )
            },
            content = { offset, dp ->
                SwipeRefreshContent(
                    viewModel = homeViewModel,
                    lazyPagingListData = squareListData,
                    contentTopPadding = dp,
                    stateRefresh = {

                    },
                    state = homeViewModel.squareLazyListState,
                ) { index, data ->
                    HomeItemView(
                        author =
                        getAuthor(data.author, data.shareUser), fresh = data.fresh, niceDate = data.niceDate ?: "刚刚",
                        title = data.title ?: "", superChapterName = data.superChapterName ?: "未知",
                        collect = data.collect
                    ) {
                        nav(navController,data.link)
                    }
                }
            }
        )
        AnimatedVisibility(visible = showButton.value) {
            FloatingActionButton(
                modifier = modifier.padding(end = 16.dp, bottom = 30.dp),
                contentColor = MaterialTheme.colors.primary,
                onClick = {
                    coroutineScope.launch {
                        homeViewModel.squareLazyListState.animateScrollToItem(0, 0)
                    }
                }) {
                Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color.White)
            }
        }
    }

}

