package com.zll.compose.compose

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.zll.compose.compose.page.Splash
import com.zll.compose.compose.page.WebViewCompose
import com.zll.compose.compose.page.login.LoginViewCompose
import com.zll.compose.compose.page.system.SystemArticleView
import com.zll.compose.compose.util.StatusBar


object MainDestinations {
    const val MAIN = "main"
    const val SPLASH = "splash"
    const val WEBVIEW = "webview"
    const val SYSTEM = "system_article"
    const val LOGIN = "system_article"
}

@ExperimentalPagerApi
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    finishActivity: () -> Unit = {},
    drawerClick: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.MAIN,
    showOnboardingInitially: Boolean = true
) {
    val onboardingComplete = remember(showOnboardingInitially) {
        mutableStateOf(!showOnboardingInitially)
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        //闪屏
        composable(MainDestinations.SPLASH) {
            StatusBar.isTransparentState.value = true
            Splash {
                onboardingComplete.value = true
                navController.popBackStack()
            }
        }
        //主页
        navigation(
            route = MainDestinations.MAIN,
            startDestination = CourseTabs.HOME.route
        ) {

            courses(
                drawerClick = drawerClick, onCourseSelected = { _, _ -> }, navController, onboardingComplete, modifier
            )
        }

        //webView页面
        composable("${MainDestinations.WEBVIEW}?url={url}",
            arguments = listOf(
                navArgument("url") {
                    defaultValue = "https://www.wanandroid.com/"
                }
            )) {
            StatusBar.isTransparentState.value = false
            WebViewCompose(
                navController,
                it.arguments?.getString("url") ?: "https://www.wanandroid.com"
            )
        }

        //体系下的文章
        composable("${MainDestinations.SYSTEM}?id={id}&title={title}",
            arguments = listOf(
                navArgument("id") {
                    defaultValue = 0
                }
            )
        ) {
            StatusBar.isTransparentState.value = false
            SystemArticleView(navController, it.arguments?.getInt("id") ?: 0, it.arguments?.getString("title")) {
                navController.popBackStack()
            }
        }
        //登录页面
        composable("${MainDestinations.LOGIN}") {
            StatusBar.isTransparentState.value = true
            LoginViewCompose(navController)
        }

    }

}