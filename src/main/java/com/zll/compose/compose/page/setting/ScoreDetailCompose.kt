package com.zll.compose.compose.page.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.zll.compose.compose.ui.BaseView
import com.zll.compose.compose.ui.DividerCompose
import com.zll.compose.compose.ui.NavigationTopBar
import com.zll.compose.compose.ui.SwipeRefreshContent
import com.zll.compose.compose.vm.SettingViewModel
import com.zll.compose.compose.vm.UserViewModel
import com.zll.compose.util.black2
import com.zll.compose.util.grey3


@Composable
fun ScoreDetailCompose(navHostController: NavHostController) {
    val settingViewModel: SettingViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val scoreListData = settingViewModel.userScoreListData.collectAsLazyPagingItems()
    val userInfo = userViewModel.userInfo.observeAsState().value
    userViewModel.loadUserInfo()
    BaseView {
        Column {
            NavigationTopBar(navHostController, title = "积分明细")
            SwipeRefreshContent(
                viewModel = settingViewModel,
                lazyPagingListData = scoreListData,
                state = settingViewModel.userScoreLazyListState,
                itemContent = {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(MaterialTheme.colors.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "${userInfo?.coinInfo?.coinCount ?: 0}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }) { index, data ->
                ScoreItemView(data.coinCount, data.desc ?: "", data.reason ?: "")
            }
        }
    }
}

@Composable
private fun ScoreItemView(coinCount: Int, desc: String, reason: String) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f, true)) {
                Text(text = reason, fontSize = 14.sp, color = black2)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = desc, fontSize = 12.sp, color = grey3)

            }
            Text(text = "+$coinCount", fontSize = 14.sp, color = MaterialTheme.colors.primary)
        }
        DividerCompose()
    }
}


