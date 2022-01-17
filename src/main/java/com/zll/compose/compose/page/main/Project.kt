package com.zll.compose.compose.page.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.zll.compose.compose.model.TabBaseModel
import com.zll.compose.compose.page.home.HomeItemView
import com.zll.compose.compose.page.home.getAuthor
import com.zll.compose.compose.ui.NestTopAndContentView
import com.zll.compose.compose.ui.SwipeRefreshContent
import com.zll.compose.compose.ui.TabLayoutCompose
import com.zll.compose.compose.ui.TopBar
import com.zll.compose.compose.util.Nav
import com.zll.compose.compose.vm.ProjectViewModel
import com.zll.compose.util.blackColor
import com.zll.compose.util.blackSecondColor
import com.zll.compose.util.blackThreeColor
import com.zll.compose.util.blueColor
import com.zll.compose.views.ImageCircle
import com.zll.compose.views.ImageWidth
import kotlinx.coroutines.launch

@Composable
fun ProjectViewCompose(modifier: Modifier, navController: NavHostController, drawerClick: () -> Unit = {}) {
    val projectViewModel: ProjectViewModel = viewModel()
    val publicListData = projectViewModel.projectListData.collectAsLazyPagingItems()

    val tabListData = projectViewModel.tabListData.observeAsState().value

    projectViewModel.loadProjectTabList()
    val showButton = remember {
        derivedStateOf {
            projectViewModel.projectLazyListState.firstVisibleItemIndex > 2
        }
    }
    val coroutineScope = rememberCoroutineScope()

    //
    /**
     * key1:
     * TopBar的Index改变
     *
     */
    LaunchedEffect(key1 = Nav.projectIndex.value) {
        if (Nav.projectIndex.value == projectViewModel.saveChangeProjectIndex) return@LaunchedEffect
        projectViewModel.apply {
            saveChangeProjectIndex = Nav.projectIndex.value
            projectLazyListState.scrollToItem(0, 0)
        }
        publicListData.refresh()
    }

    /**
     * ke2:
     * tablayout 后出现 导致id获取不到，列表为空
     */
    LaunchedEffect(projectViewModel.idState.value) {
        if (projectViewModel.idState.value == 0 || publicListData.itemCount > 0) return@LaunchedEffect
        publicListData.refresh()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        NestTopAndContentView(
            modifier = modifier,
            topBarOtherDp = 54.dp,
            topBar = {
                Column() {
                    TopBar(
                        title = "项目",
                        leftIcon = Icons.Default.Menu,
                        menuIcon = Icons.Filled.Search,
                        leftClick = drawerClick
                    )
                    TabLayoutCompose(tabBarIndex = Nav.projectIndex, tabData = tabListData as? List<TabBaseModel>)
                }
            },
            content = { offset, dp ->
                if (tabListData.isNullOrEmpty()) {

                } else {
                    SwipeRefreshContent(
                        viewModel = projectViewModel,
                        lazyPagingListData = publicListData,
                        contentTopPadding = dp,
                        cardHeight = 160.dp,
                        state = projectViewModel.projectLazyListState,
                    ) { index, data ->
                        ProjectView(
                            author =
                            getAuthor(data.author, data.shareUser),
                            data.envelopePic ?: "",
                            data.title ?: "",
                            data.desc ?: "",
                            collect = data.collect,
                            data.niceDate ?: "刚刚"
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
                        projectViewModel.projectLazyListState.animateScrollToItem(0, 0)
                    }
                }) {
                Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color.White)
            }
        }
    }
}


@Composable
fun ProjectView(author: String, pic: String, title: String, desc: String, collect: Boolean, date: String, onClick: () -> Unit = {}) {
    Row(Modifier.padding(horizontal = 16.dp, vertical = 10.dp).fillMaxSize().clickable {
        onClick.invoke()
    }) {
        ImageWidth(pic, 100.dp)
        Column(
            Modifier
                .padding(start = 10.dp)
                .fillMaxHeight()
                .weight(1f, true)
        ) {
            Text(
                text = title, maxLines = 1, overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp, fontWeight = FontWeight.Bold, color = blackColor
            )
            Box(
                Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .weight(1f,true)
            ) {
                Text(
                    text = title, overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp, color = blackThreeColor
                )
            }
            Row(Modifier.padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = author, fontSize = 14.sp, color = blackSecondColor)
                Spacer(
                    modifier = Modifier
                        .width(10.dp)
                        .height(0.dp)
                )
                Text(text = date, fontSize = 14.sp, color = blackSecondColor)
                Box(
                    Modifier
                        .weight(1f, true), contentAlignment = Alignment.CenterEnd) {
                    //收藏
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = if (collect) Color.Red else Color.LightGray
                    )
                }
            }
        }
    }

}