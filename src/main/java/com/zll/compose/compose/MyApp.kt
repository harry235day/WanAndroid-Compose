package com.zll.compose.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.zll.compose.compose.page.DrawerViewCompose
import com.zll.compose.compose.theme.CustomTheme
import com.zll.compose.compose.util.Nav
import com.zll.compose.compose.vm.UserViewModel
import com.zll.compose.util.*
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun MyApp(finishActivity: () -> Unit) {
    ProvideWindowInsets {
        CustomTheme.AppTheme {
            val tabs = remember {
                CourseTabs.values
            }
            val userViewModel: UserViewModel = viewModel()
            val userInfo = userViewModel.userInfo.observeAsState().value

            LaunchedEffect(Nav.loginState) {
                if(Nav.loginState.value){
                    userViewModel.loadUserInfo()
                }
            }

            val scaffoldState = rememberScaffoldState()
            val navController = rememberAnimatedNavController()
            val scope = rememberCoroutineScope()
            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {
                    Column() {
                        BottomBar(navController, tabs)
                    }
                },
                drawerGesturesEnabled = false,
                drawerContent = {
                    DrawerViewCompose(
                        navController, userViewModel, userInfo?.userInfo?.username, null,
                        userInfo?.coinInfo?.coinCount ?: 0, userInfo?.coinInfo?.rank, scaffoldState
                    ) {
                        if (scaffoldState.drawerState.isOpen) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        } else {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    }
                }
            ) {
                NavGraph(
                    finishActivity = finishActivity,
                    navController = navController,
                    modifier = Modifier.padding(it),
                    drawerClick = {
                        if (scaffoldState.drawerState.isOpen) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        } else {
                            scope.launch {
                                scaffoldState.drawerState.open()
                                if (userViewModel.isLogin()) {
                                    userViewModel.loadUserInfo()
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, tabs: Array<CourseTabs>) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
        ?: CourseTabs.HOME.route

    val routes = remember {
        CourseTabs.values.map { it.route }
    }
    if (currentRoute in routes) {
        BottomNavigation(
            Modifier.navigationBarsHeight(56.dp),
            backgroundColor = MaterialTheme.colors.background
        ) {
            for (tab in tabs) {
                BottomNavigationItem(
                    icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                    label = { Text(stringResource(tab.title).uppercase(Locale.getDefault())) },
                    selected = currentRoute == tab.route,
                    onClick = {
                        if (tab.route != currentRoute) {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                    },
                    alwaysShowLabel = true,
                    selectedContentColor = blueColor,
                    unselectedContentColor = blackSecondColor,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }
}
