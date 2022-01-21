package com.zll.compose.compose.page.setting

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.zll.compose.compose.page.login.LoginLoadingViewCompose
import com.zll.compose.compose.ui.BaseView
import com.zll.compose.compose.ui.CommonDialog
import com.zll.compose.compose.ui.TopBar
import com.zll.compose.compose.vm.NetWorkState
import com.zll.compose.compose.vm.UserViewModel

@Composable
fun SettingViewCompose(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()
    val loading = userViewModel.loading.observeAsState()
    val logoutData = userViewModel.logoutData.observeAsState()
    val loadingState = remember {
        mutableStateOf(false)
    }

    val openDialog = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    LaunchedEffect(loading.value) {
        if (loadingState.value && loading.value?.state == NetWorkState.State.Loading) return@LaunchedEffect
        loadingState.value = loading.value?.state == NetWorkState.State.Loading
        if (loading.value != null && loading.value!!.state != NetWorkState.State.Loading) {
            Toast.makeText(context, loading.value!!.msg, Toast.LENGTH_LONG).show()
        }
        if (loading.value?.state == NetWorkState.State.Success) {
            navController.popBackStack()
        }
    }

    BaseView {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(Modifier.fillMaxSize()) {
                TopBar(title = "设置", leftIcon = Icons.Default.ArrowBack, leftClick = {
                    navController.popBackStack()
                })
                if (userViewModel.isLogin()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, true),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Button(
                            onClick = {
                                openDialog.value = true
                            },
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "退出", fontSize = 14.sp, color = Color.White)
                        }
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = loadingState.value
            ) {
                LoginLoadingViewCompose()
            }

            if (openDialog.value) {
                CommonDialog(openDialog, title = "是否退出？", confirmButton = "退出") {
                    userViewModel.logout()
                }
            }
        }
    }

}
