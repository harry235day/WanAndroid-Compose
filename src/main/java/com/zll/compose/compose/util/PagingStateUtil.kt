package com.zll.compose.compose.util

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.zll.compose.compose.ui.ErrorComposable
import com.zll.compose.ext.sleepTime

class PagingStateUtil {
    //是否显示无数据的布局
    var showNullScreen = false

    @Composable
    fun <T : Any> pagingState(
        //paging数据
        pagingData: LazyPagingItems<T>,
        refreshState: SwipeRefreshState,
        viewModel: ViewModel,
        content: @Composable () -> Unit
    ) {
        when(pagingData.loadState.refresh){
            //未加载或者成功失败完成的回调
            is LoadState.NotLoading -> NotLoading(refreshState, viewModel) {
                when(pagingData.itemCount){
                    0->{
                        if(!showNullScreen){
                            showNullScreen = true
                            Placeholder()
                        }else{
                            ErrorComposable("暂无数据，请点击重试") {
                                pagingData.refresh()
                            }
                        }
                    }
                    else ->{
                        content()
                    }
                }
            }
            is LoadState.Error -> Error(pagingData, refreshState)
            LoadState.Loading -> Loading(refreshState)
        }
        //如果在加载途中遇到错误的话，pagingData的状态为append
        when (pagingData.loadState.append) {
            //加载失败
            is LoadState.Error -> Error(pagingData, refreshState)
            //加载中
            LoadState.Loading -> Loading(refreshState)
        }
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
    private fun Placeholder(){
        Text(
            text = "Content to display after content has loaded",
            modifier = Modifier
                .padding(16.dp)
                .placeholder(
                    visible = true,
                    color = Color.Gray,
                    // optional, defaults to RectangleShape
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderHighlight.fade(
                        highlightColor = Color.White,
                    ),
                )
        )
    }

}