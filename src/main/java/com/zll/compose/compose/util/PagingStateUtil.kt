package com.zll.compose.compose.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.material.ButtonDefaults.textButtonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.zll.compose.compose.ui.ErrorComposable
import com.zll.compose.ext.sleepTime
import com.zll.compose.util.grey3

class PagingStateUtil {
    //是否显示无数据的布局
    var showNullScreen = false

    @Composable
    fun <T : Any> pagingState(
        //paging数据
        pagingData: LazyPagingItems<T>,
        refreshState: SwipeRefreshState,
        viewModel: ViewModel,
        isLazyColumn: Boolean = true,
        content: @Composable () -> Unit
    ) {
        when (pagingData.loadState.refresh) {
            //未加载或者成功失败完成的回调
            is LoadState.NotLoading -> NotLoading(refreshState, viewModel) {
                when (pagingData.itemCount) {
                    0 -> {
                        if (!showNullScreen) {
                            showNullScreen = true
                            ErrorComposable("暂无数据，请点击重试") {
                                pagingData.refresh()
                            }
                        } else {
                            ErrorComposable("暂无数据，请点击重试") {
                                pagingData.refresh()
                            }
                        }
                    }
                    else -> {
                        content()
                    }
                }
            }
            is LoadState.Error -> Error(pagingData, refreshState)
            LoadState.Loading -> Loading(refreshState)
        }
//        //如果在加载途中遇到错误的话，pagingData的状态为append
//        when (pagingData.loadState.append) {
//            //加载失败
//            is LoadState.Error -> {
//                if(isLazyColumn){
//                    ErrorMoreRetryItem{
//                        pagingData.retry()
//                    }
//                }else{
//                    Error(pagingData, refreshState)
//                }
//            }
//            //加载中
//            LoadState.Loading -> LoadingItem()
//        }
    }

    /**
     * 未加载且未观察到错误
     */
    @Composable
    private fun NotLoading(
        refreshState: SwipeRefreshState,
        viewModel: ViewModel,
        content: @Composable () -> Unit
    ) {
        content()
        //让刷新头停留一下子再收回去
        viewModel.sleepTime {
            refreshState.isRefreshing = false
        }
    }

    /**
     * 加载失败
     */
    @Composable
    private fun <T : Any> Error(
        pagingData: LazyPagingItems<T>,
        refreshState: SwipeRefreshState
    ) {
        refreshState.isRefreshing = false
        ErrorComposable {
            pagingData.refresh()
        }
    }

    /**
     * 加载中
     */
    @Composable
    private fun Loading(refreshState: SwipeRefreshState) {
        Row(modifier = Modifier.fillMaxSize()) { }
        //显示刷新头
        if (!refreshState.isRefreshing) refreshState.isRefreshing = true
    }


    @Composable
    private fun Placeholder() {
        Box(modifier = Modifier.fillMaxSize()) { }
    }

}

/**
 * 底部加载更多失败处理
 * */
@Composable
fun ErrorMoreRetryItem(retry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { retry() }
            .background(Color.White)
            .padding(10.dp), contentAlignment = Alignment.Center
    ) {
        Text(text = "请重试", fontSize = 14.sp, color = grey3)
    }
}

/**
 * 底部加载更多正在加载中...
 * */
@Composable
fun LoadingItem() {
    Row(
        modifier = Modifier
            .height(34.dp)
            .fillMaxWidth()
            .background(Color.White)
            .padding(5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(24.dp),
            color = grey3,
            strokeWidth = 2.dp
        )
        Text(
            text = "加载中...",
            color = grey3,
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 20.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
    }
}