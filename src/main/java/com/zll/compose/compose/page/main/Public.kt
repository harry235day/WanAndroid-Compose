package com.zll.compose.compose.page.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.zll.compose.compose.model.ProjectListData
import com.zll.compose.compose.model.TabBaseModel
import com.zll.compose.compose.navSearch
import com.zll.compose.compose.page.home.HomeItemView
import com.zll.compose.compose.page.home.getAuthor
import com.zll.compose.compose.ui.NestTopAndContentView
import com.zll.compose.compose.ui.SwipeRefreshContent
import com.zll.compose.compose.ui.TabLayoutCompose
import com.zll.compose.compose.ui.TopBar
import com.zll.compose.compose.util.Nav
import com.zll.compose.compose.vm.PublicViewModel
import com.zll.compose.util.blueColor
import kotlinx.coroutines.launch

@Composable
fun PublicViewCompose(modifier: Modifier, navController: NavHostController, drawerClick: () -> Unit = {}) {

    val publicViewModel: PublicViewModel = viewModel()
    val publicListData = publicViewModel.publicListData.collectAsLazyPagingItems()

    val tabListData = publicViewModel.tabListData.observeAsState().value

    publicViewModel.loadPublicTabList()
    val showButton = remember {
        derivedStateOf {
            publicViewModel.publicLazyListState.firstVisibleItemIndex > 2
        }
    }
    val coroutineScope = rememberCoroutineScope()

    //
    /**
     * key1:
     * TopBar的Index改变
     *
     */
    LaunchedEffect(key1 = Nav.publicIndex.value) {
        if (Nav.publicIndex.value == publicViewModel.saveChangeProjectIndex ) return@LaunchedEffect
        publicViewModel.apply {
            saveChangeProjectIndex = Nav.publicIndex.value
            publicLazyListState.scrollToItem(0, 0)
        }
        publicListData.refresh()
    }

    /**
     * ke2:
     * tablayout 后出现 导致id获取不到，列表为空
     */
    LaunchedEffect(publicViewModel.idState.value){
        if(publicViewModel.idState.value==0 || publicListData.itemCount>0) return@LaunchedEffect
        publicListData.refresh()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        NestTopAndContentView(
            modifier = modifier,
            topBarOtherDp = 54.dp,
            topBar = {
                Column() {
                    TopBar(
                        title = "公众号",
                        leftIcon = Icons.Default.Menu,
                        menuIcon = Icons.Filled.Search,
                        leftClick = drawerClick,
                        menuClick = {
                            navSearch(navController)
                        }
                    )
                    TabLayoutCompose(tabBarIndex = Nav.publicIndex, tabData = tabListData as? List<TabBaseModel>)
                }
            },
            content = { offset, dp ->
                if (tabListData.isNullOrEmpty()) {

                } else {
                    SwipeRefreshContent(
                        viewModel = publicViewModel,
                        lazyPagingListData = publicListData,
                        contentTopPadding = dp,
                        stateRefresh = {

                        },
                        state = publicViewModel.publicLazyListState,
                    ) { index, data ->
                        HomeItemView(
                            author =
                            getAuthor(data.author, data.shareUser), fresh = data.fresh, niceDate = data.niceDate ?: "刚刚",
                            title = data.title ?: "", superChapterName = data.superChapterName ?: "未知",
                            collect = data.collect
                        ) {
                            nav(navController, data.link)
                        }
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
                        publicViewModel.publicLazyListState.animateScrollToItem(0, 0)
                    }
                }) {
                Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color.White)
            }
        }
    }
}

