package com.zll.compose.compose.page.search

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
import com.zll.compose.compose.vm.SettingViewModel
import com.zll.compose.compose.vm.SystemViewModel
import com.zll.compose.util.blueColor
import kotlinx.coroutines.launch


@Composable
fun SearchResultViewCompose(navHostController: NavHostController,  key: String, leftClick: () -> Unit = {}) {
    val settingViewModel: SettingViewModel = viewModel()
    settingViewModel.searchKey = key
    val searchResultListData = settingViewModel.searchResultListData.collectAsLazyPagingItems()

    val showButton = remember {
        derivedStateOf {
            settingViewModel.searchResultLazyListState.firstVisibleItemIndex > 2
        }
    }
    BaseView {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Column {
                TopBar(
                    title = key,
                    leftIcon = Icons.Default.ArrowBack,
                    leftClick = {
                        navHostController.popBackStack()
                    }
                )
                SwipeRefreshContent(
                    viewModel = settingViewModel,
                    lazyPagingListData = searchResultListData,
                    state = settingViewModel.searchResultLazyListState,
                    stateRefresh = {
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
            AnimatedVisibility(visible = showButton.value) {
                FloatingActionButton(
                    modifier = Modifier.padding(end = 16.dp, bottom = 30.dp),
                    contentColor = MaterialTheme.colors.primary,
                    onClick = {
                        settingViewModel.apply {
                            viewModelScope.launch {
                                searchResultLazyListState.scrollToItem(0, 0)
                            }
                        }
                    }) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}