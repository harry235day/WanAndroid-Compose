package com.zll.compose.compose.page.main

import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.flowlayout.FlowRow
import com.zll.compose.compose.MainDestinations
import com.zll.compose.compose.model.*
import com.zll.compose.compose.ui.NestTopAndContentView
import com.zll.compose.compose.ui.SwipeRefreshContent
import com.zll.compose.compose.ui.TabLayoutCompose
import com.zll.compose.compose.ui.TopBar
import com.zll.compose.compose.util.Constant
import com.zll.compose.compose.util.Nav
import com.zll.compose.compose.vm.SystemViewModel
import com.zll.compose.util.*
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun SystemViewCompose(modifier: Modifier, navController: NavHostController, drawerClick: () -> Unit = {}) {
    val systemViewModel: SystemViewModel = viewModel()
    val systemListData = systemViewModel.systemListData.observeAsState()
    val navListData = systemViewModel.navListData.observeAsState()

    val tabListData = systemViewModel.tabList

    systemViewModel.loadSystemList()
    systemViewModel.loadNavList()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        NestTopAndContentView(
            modifier = modifier,
            topBarOtherDp = 54.dp,
            topBar = {
                Column() {
                    TopBar(
                        title = "体系",
                        leftIcon = Icons.Default.Menu,
                        menuIcon = Icons.Filled.Search,
                        leftClick = drawerClick
                    )
                    TabLayoutCompose(tabBarIndex = Nav.systemIndex, tabData = tabListData)
                }
            },
            content = { offset, dp ->
                when (Nav.systemIndex.value) {
                    0 -> {
                        SwipeRefreshContent(
                            viewModel = systemViewModel,
                            listData = systemListData.value,
                            state = systemViewModel.systemIndexState,
                            contentTopPadding = dp,
                            noData = {
                                systemViewModel.loadSystemList()
                            }) {
                            SystemItemView(navController, it.name ?: "", it.children)
                        }
                    }
                    else -> {
                        SwipeRefreshContent(
                            viewModel = systemViewModel,
                            list = navListData.value,
                            contentTopPadding = dp,
                            noData = {
                                systemViewModel.loadNavList()
                            }) {
                            NavView(navController, navListData.value)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun NavView(navController: NavHostController, list: List<NavModel>?) {
    val index = remember {
        mutableStateOf(0)
    }
    var contentIndex = 0
    val contentState = rememberLazyListState()
    val menuState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val state = remember {
        derivedStateOf {
            contentState.firstVisibleItemIndex
        }
    }

    LaunchedEffect(state.value) {
        if (state.value == index.value || contentIndex == index.value) return@LaunchedEffect
        scope.launch {
            index.value = state.value
            contentState.scrollToItem(state.value, 0)
        }
    }

    list?.takeIf { it.isNotEmpty() }?.let {
        Row {
            LazyColumn(
                Modifier
                    .width(100.dp)
                    .fillMaxHeight(),
                state = menuState
            ) {
                items(it.size) { i ->
                    val data = it[i]
                    NavMenuView(data.name ?: "", index.value == i) {
                        index.value = i
                        contentIndex = i
                        scope.launch {
                            contentState.scrollToItem(i, 0)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            LazyColumn(
                Modifier
                    .weight(1f, true)
                    .fillMaxHeight(),
                state = contentState,
            ) {
                items(it.size) { i ->
                    val data = it[i]
                    NavContentView(navController, data.name ?: "", data.articles) {

                    }
                }
            }
        }
    }
}

@Composable
fun NavMenuView(title: String, isSelect: Boolean, onClick: () -> Unit = {}) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .clickable {
            onClick.invoke()
        }
        .background(if (isSelect) white else white5), contentAlignment = Alignment.Center) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = if (isSelect) MaterialTheme.colors.primary else blackThreeColor,
        )
    }
}


@Composable
fun NavContentView(navController: NavHostController, title: String, list: List<ProjectListData>?, onClick: () -> Unit = {}) {
    Text(
        title,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        color = blackColor,
        modifier = Modifier.padding(top = 10.dp)
    )
    list?.let {
        FlowRow(mainAxisSpacing = 10.dp, crossAxisSpacing = 10.dp, modifier = Modifier.padding(start = 5.dp, top = 10.dp)) {
            it.forEachIndexed { index, item ->
                val random = Random.nextInt(Constant.colors.size)
                NavTagView(navController, Constant.colors[random], item.title ?: "",item.link)
            }
        }
    }
}

@Composable
fun NavTagView(navController: NavHostController, color: Color, name: String,link:String?) {
    Text(
        text = name, fontSize = 14.sp, color = color,
        modifier = Modifier
            .clip(RoundedCornerShape(2.dp))
            .clickable {
                nav(navController,link)
            }
            .background(white5)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}


@Composable
fun SystemItemView(navController: NavHostController, title: String, list: List<ProjectTreeChildrenData>?) {
    Column(Modifier.padding(10.dp)) {
        Text(
            title,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = blackColor
        )
        list?.let {
            FlowRow(mainAxisSpacing = 10.dp, crossAxisSpacing = 5.dp, modifier = Modifier.padding(top = 10.dp)) {
                for (item in it) {
                    TagView(navController, item.id, item.name ?: "")
                }
            }
        }
    }
}

@Composable
private fun TagView(navController: NavHostController, id: Int, name: String) {
    Text(
        text = name, fontSize = 14.sp, color = white,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                navController.navigate("${MainDestinations.SYSTEM}?id=$id&title=$name")
            }
            .background(MaterialTheme.colors.primary)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )

}
