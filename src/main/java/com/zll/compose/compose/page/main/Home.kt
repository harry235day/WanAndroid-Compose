package com.zll.compose.compose.page.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.zll.compose.compose.MainDestinations
import com.zll.compose.compose.page.home.HomeItemView
import com.zll.compose.compose.page.home.getAuthor
import com.zll.compose.compose.ui.*
import com.zll.compose.compose.vm.HomeViewModel
import com.zll.compose.util.blueColor
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun HomeViewCompose(modifier: Modifier = Modifier, navHostController: NavHostController, drawerClick: () -> Unit = {}) {
    val homeViewModel: HomeViewModel = viewModel()
    val homeListData = homeViewModel.homeListData.collectAsLazyPagingItems()
    val state = remember {
        mutableStateOf(true)
    }
    val showButton = remember {
        derivedStateOf {
            homeViewModel.homeLazyListState.firstVisibleItemIndex > 2
        }
    }
    val coroutineScope = rememberCoroutineScope()

    val bannerList = homeViewModel.bannerList.observeAsState()
    //加载banner
    homeViewModel.loadBanner()

    //置顶
    val topArticleList = homeViewModel.topArticleList.observeAsState()
    homeViewModel.loadTopArticleList()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        NestTopAndContentView(
            modifier = modifier,
            state = state,
            topBar = {
                TopBar(
                    title = "玩Android",
                    leftIcon = Icons.Default.Menu,
                    menuIcon = Icons.Filled.Search,
                    leftClick = drawerClick
                )
            },
            content = { offset, dp ->
                SwipeRefreshContent(
                    viewModel = homeViewModel,
                    lazyPagingListData = homeListData,
                    contentTopPadding = dp,
                    stateRefresh = {
                        state.value = !it
                    },
                    state = homeViewModel.homeLazyListState,
                    itemContent = {
                        item {
                            if (bannerList.value?.isSuccess == true) {
                                BannerViewCompose(banners = bannerList.value?.getOrNull()) {
                                    nav(navHostController, it)
                                }
                            } else {
                                Spacer(Modifier)
                            }
                        }
                        topArticleList.value?.getOrNull()?.takeIf { it.isNotEmpty() }?.let {
                            items(it.size) { index ->
                                SimpleCard(cardHeight = 120.dp) {
                                    it[index].apply {
                                        HomeItemView(
                                            getAuthor(author, shareUser),
                                            fresh,
                                            true,
                                            niceDate ?: "刚刚",
                                            title ?: "xxx",
                                            superChapterName ?: "未知",
                                            collect
                                        ) {
                                            nav(navHostController, link)
                                        }
                                    }
                                }
                            }
                        }
                    }
                ) { index, data ->
                    HomeItemView(
                        author =
                        getAuthor(data.author, data.shareUser), fresh = data.fresh, niceDate = data.niceDate ?: "刚刚",
                        title = data.title ?: "", superChapterName = data.superChapterName ?: "未知",
                        collect = data.collect
                    ) {
                        nav(navHostController, data.link)
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
                        homeViewModel.homeLazyListState.animateScrollToItem(0, 0)
                    }
                }) {
                Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color.White)
            }
        }
    }

}

fun nav(navHostController: NavHostController, link: String?) {
    navHostController.navigate("${MainDestinations.WEBVIEW}?url=$link")
}
