package com.zll.compose.compose.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.zll.compose.compose.util.ErrorMoreRetryItem
import com.zll.compose.compose.util.LoadingItem
import com.zll.compose.compose.util.PagingStateUtil
import com.zll.compose.ext.sleepTime

@Composable
fun <T : Any> SwipeRefreshContent(
    viewModel: ViewModel,
    lazyPagingListData: LazyPagingItems<T>,
    cardHeight: Dp = 120.dp,
    contentTopPadding: Dp = 0.dp,
    modifier: Modifier = Modifier,
    stateRefresh: (state: Boolean) -> Unit = {},
    state: LazyListState = rememberLazyListState(),
    itemContent: LazyListScope.() -> Unit = {},
    content: @Composable (index: Int, data: T) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = contentTopPadding)
    ) {
        val refreshState = rememberSwipeRefreshState(false)
        SwipeRefresh(
            state = refreshState,
            onRefresh = {
                //显示刷新头
                refreshState.isRefreshing = true
                //刷新数据
                lazyPagingListData.refresh()
                viewModel.sleepTime(1000) {
                    refreshState.isRefreshing = false
                }
            }
        ) {
            //列表数据
            PagingStateUtil().pagingState(lazyPagingListData, refreshState, viewModel) {
                LazyColumn(modifier = Modifier.fillMaxSize(), state = state) {
                    itemContent()
                    itemsIndexed(lazyPagingListData) { index, data ->
                        SimpleCard(cardHeight = cardHeight) {
                            content(index, data!!)
                        }
                    }
                    //如果在加载途中遇到错误的话，pagingData的状态为append
                    when (lazyPagingListData.loadState.append) {
                        //加载失败
                        is LoadState.Error -> {
                            item {
                                ErrorMoreRetryItem{
                                    lazyPagingListData.retry()
                                }
                            }
                        }
                        //加载中
                        LoadState.Loading -> {
                            item {
                                LoadingItem()
                            }
                        }
                    }
                }
            }
        }

    }
}


/**
 * 带刷新头的Card布局
 * LazyPagingItems<T>
 * Card高度自适应
 */
@Composable
fun <T : Any> SwipeRefreshContent(
    viewModel: ViewModel,
    listData: List<T>?,
    contentTopPadding: Dp = 0.dp,
    state: LazyListState = rememberLazyListState(),
    noData: () -> Unit,
    content: @Composable (data: T) -> Unit
) {

    if (listData == null) return

    if (listData.isEmpty()) {
        ErrorComposable("暂无数据，请点击重试") {
            noData()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = contentTopPadding)) {

        val refreshState = rememberSwipeRefreshState(false)

        SwipeRefresh(
            state = refreshState,
            onRefresh = {
                //显示刷新头
                refreshState.isRefreshing = true
                //刷新数据
                noData()
                viewModel.sleepTime(3000) {
                    refreshState.isRefreshing = false
                }
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize(), state = state) {
                itemsIndexed(listData) { index, data ->
                    SimpleCard {
                        content(data)
                    }
                }
            }
        }

    }
}


@Composable
fun <T : Any> SwipeRefreshContent(
    viewModel: ViewModel,
    list: List<T>?,
    contentTopPadding: Dp = 0.dp,
    noData: () -> Unit,
    content: @Composable () -> Unit
) {

    if (list == null) return

    if (list.isEmpty()) {
        ErrorComposable("暂无数据，请点击重试") {
            noData()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = contentTopPadding)) {

        val refreshState = rememberSwipeRefreshState(false)

        SwipeRefresh(
            state = refreshState,
            onRefresh = {
                //显示刷新头
                refreshState.isRefreshing = true
                //刷新数据
                noData()
                viewModel.sleepTime(3000) {
                    refreshState.isRefreshing = false
                }
            }
        ) {
            content()
        }

    }
}

@Composable
fun <T : Any> SwipeRefreshContent(
    viewModel: ViewModel,
    lazyPagingListData: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    itemContent: LazyListScope.() -> Unit = {},
    content: @Composable (index: Int, data: T) -> Unit
) {
    Column(
        modifier =modifier
            .fillMaxSize()
    ) {
        val refreshState = rememberSwipeRefreshState(false)
        SwipeRefresh(
            state = refreshState,
            onRefresh = {
                //显示刷新头
                refreshState.isRefreshing = true
                //刷新数据
                lazyPagingListData.refresh()
                viewModel.sleepTime(1000) {
                    refreshState.isRefreshing = false
                }
            }
        ) {
            //列表数据
            PagingStateUtil().pagingState(lazyPagingListData, refreshState, viewModel) {
                LazyColumn(modifier = Modifier.fillMaxSize(), state = state) {
                    itemContent()
                    itemsIndexed(lazyPagingListData) { index, data ->
                        content(index, data!!)
                    }
                    //如果在加载途中遇到错误的话，pagingData的状态为append
                    when (lazyPagingListData.loadState.append) {
                        //加载失败
                        is LoadState.Error -> {
                            item {
                                ErrorMoreRetryItem{
                                    lazyPagingListData.retry()
                                }
                            }
                        }
                        //加载中
                        LoadState.Loading -> {
                            item {
                                LoadingItem()
                            }
                        }
                    }
                }

            }
        }

    }
}