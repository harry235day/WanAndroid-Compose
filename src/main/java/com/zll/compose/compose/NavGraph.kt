package com.zll.compose.compose

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.zll.compose.compose.page.Splash
import com.zll.compose.compose.page.WebViewCompose
import com.zll.compose.compose.page.home.ScoreViewCompose
import com.zll.compose.compose.page.login.LoginViewCompose
import com.zll.compose.compose.page.search.SearchResultViewCompose
import com.zll.compose.compose.page.search.SearchViewCompose
import com.zll.compose.compose.page.setting.ScoreDetailCompose
import com.zll.compose.compose.page.setting.SettingViewCompose
import com.zll.compose.compose.page.system.SystemArticleView
import com.zll.compose.compose.util.StatusBar


object MainDestinations {
    const val MAIN = "main"
    const val SPLASH = "splash"
    const val WEBVIEW = "webview"
    const val SYSTEM = "system_article"
    const val LOGIN = "login"
    const val SETTING = "setting"
    const val SCORE = "score"
    const val SEARCH = "search"
    const val SEARCH_RESULT = "search_result"
    const val USER_SCORE = "user_score"
}

@ExperimentalAnimationApi
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

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
        },

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
        composable(MainDestinations.LOGIN) {
            StatusBar.isTransparentState.value = true
            LoginViewCompose(navController)
        }
        //设置
        composable(MainDestinations.SETTING) {
            SettingViewCompose(navController)
        }
        //积分
        composable(MainDestinations.SCORE) {
            ScoreViewCompose(navController)
        }
        //搜索
        composable(MainDestinations.SEARCH) {
            SearchViewCompose(navController)
        }
        //搜索结果
        composable(
            "${MainDestinations.SEARCH_RESULT}?key={key}",
            arguments = listOf(
                navArgument("key") {
                    defaultValue = ""
                }
            ),

            ) {
            SearchResultViewCompose(navController, it.arguments?.getString("key") ?: "") {

            }
        }
        //个人积分
        composable(MainDestinations.USER_SCORE){
            ScoreDetailCompose(navController)
        }
    }
}

fun navSearch(navController: NavHostController) {
    navController.navigate(MainDestinations.SEARCH)
}

fun navSearchResult(navController: NavHostController, key: String) {
    navController.navigate("${MainDestinations.SEARCH_RESULT}?key=$key")
}