package com.zll.compose.compose.page.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.zll.compose.compose.ui.BaseView
import com.zll.compose.compose.ui.DividerCompose
import com.zll.compose.compose.ui.SwipeRefreshContent
import com.zll.compose.compose.ui.TopBar
import com.zll.compose.compose.vm.SettingViewModel

@Composable
fun ScoreViewCompose(navHostController: NavHostController) {
    val settingViewModel: SettingViewModel = viewModel()
    val scoreListData = settingViewModel.scoreListData.collectAsLazyPagingItems()
    BaseView {
        Column() {
            TopBar(title = "积分排行榜", leftIcon = Icons.Default.ArrowBack, leftClick = {
                navHostController.popBackStack()
            })
            SwipeRefreshContent(
                viewModel = settingViewModel,
                lazyPagingListData = scoreListData,
                state = settingViewModel.scoreLazyListState,
            ) { index, data ->
                ScoreItemView(index, data.coinCount, data.username ?: "")
            }
        }
    }
}

@Composable
private fun ScoreItemView(index: Int, coinCount: Int, name: String) {
    Column{
        Row(Modifier.padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)) {
            Text(text = index.toString(), fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = name, fontSize = 16.sp, color = Color.Black)
            Box(Modifier.weight(1f, true), contentAlignment = Alignment.CenterEnd) {
                Text(text = coinCount.toString(), fontSize = 14.sp, color = Color.Blue)
            }
        }
        DividerCompose()
    }
}
