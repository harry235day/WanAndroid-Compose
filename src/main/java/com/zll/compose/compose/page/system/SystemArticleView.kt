package com.zll.compose.compose.page.system

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.zll.compose.compose.page.home.HomeItemView
import com.zll.compose.compose.page.home.getAuthor
import com.zll.compose.compose.page.main.nav
import com.zll.compose.compose.ui.*
import com.zll.compose.compose.vm.SystemViewModel
import com.zll.compose.util.blueColor
import kotlinx.coroutines.launch


@Composable
fun SystemArticleView(navHostController: NavHostController, id: Int, title: String?, leftClick: () -> Unit = {}) {
    val systemViewModel: SystemViewModel = viewModel()
    systemViewModel.id = id
    val articleListData = systemViewModel.articleListData.collectAsLazyPagingItems()

    val showButton = remember {
        derivedStateOf {
            systemViewModel.systemArticleState.firstVisibleItemIndex > 2
        }
    }

    BaseView {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Column {
                TopBar(
                    title = "${title ?: "玩Android"}",
                    leftIcon = Icons.Default.ArrowBack,
                    menuIcon = Icons.Filled.Search,
                    leftClick = leftClick
                )
                SwipeRefreshContent(
                    viewModel = systemViewModel,
                    lazyPagingListData = articleListData,
                    state = systemViewModel.systemArticleState,
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
            AnimatedVisibility(visible = showButton.value) {
                FloatingActionButton(
                    modifier = Modifier.padding(end = 16.dp, bottom = 30.dp),
                    contentColor = MaterialTheme.colors.primary,
                    onClick = {
                        systemViewModel.apply {
                            viewModelScope.launch {
                                systemArticleState.scrollToItem(0, 0)
                            }
                        }
                    }) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}