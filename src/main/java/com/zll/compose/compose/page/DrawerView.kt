package com.zll.compose.compose.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsPadding
import com.zll.compose.compose.MainDestinations
import com.zll.compose.compose.vm.UserViewModel
import com.zll.compose.util.blackColor
import com.zll.compose.util.blueColor
import com.zll.compose.views.ImageCircle
import com.zll.compose.views.VerticalSpacer
import kotlinx.coroutines.launch

@Composable
fun DrawerViewCompose(navController: NavHostController, vm: UserViewModel, name: String?, head: String?, coinCount: Int, rank: String?, scaffoldState: ScaffoldState, onClick: () -> Unit) {
    val isLogin = vm.isLogin()
    Column(
        Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .clickable {
                onClick.invoke()
            }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .padding(10.dp)

        ) {
            Column(
                Modifier
                    .clickable {
                        onClick()
                        login(navController, isLogin)
                    }
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageCircle(head ?: "", 60.dp)
                VerticalSpacer(5.dp)
                Text(text = name ?: "去登陆")
                VerticalSpacer(5.dp)
                Text(text = "积分：${if (coinCount <= 0) "--" else coinCount} 排行：${if (rank.isNullOrBlank()) "--" else rank} ")
            }
        }
        VerticalSpacer()
        DrawerItemView(Icons.Default.DateRange, "我的积分") {

        }
        DrawerItemView(Icons.Default.Favorite, "我的收藏") {

        }
        DrawerItemView(Icons.Default.Share, "我的分享") {

        }
        DrawerItemView(Icons.Default.Settings, "系统设置") {

        }
    }
}

fun login(navController: NavHostController, isLogin: Boolean, listener: () -> Unit = {}) {
    if (isLogin) {
        listener.invoke()
    } else {
        navController.navigate(MainDestinations.LOGIN)
    }
}

@Composable
fun DrawerItemView(icon: ImageVector, title: String, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Icon(icon, contentDescription = null, tint = Color.Gray)
        Spacer(
            modifier = Modifier
                .width(20.dp)
                .height(0.dp)
        )
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = blackColor)
    }
}