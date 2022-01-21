package com.zll.compose.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.zll.compose.R
import com.zll.compose.compose.page.main.*
import com.zll.compose.compose.util.StatusBar

@ExperimentalAnimationApi
@ExperimentalPagerApi
fun NavGraphBuilder.courses(
    drawerClick: () -> Unit = {},
    onCourseSelected: (Long, NavBackStackEntry) -> Unit = {_,_ ->},
    navController: NavHostController,
    onboardingComplete: State<Boolean>,
    modifier: Modifier = Modifier
) {
    composable(CourseTabs.HOME.route) {
        LaunchedEffect(onboardingComplete) {
            if (!onboardingComplete.value) {
                navController.navigate(MainDestinations.SPLASH)
            }
        }
        if (onboardingComplete.value) {
            StatusBar.isTransparentState.value = false
            HomeViewCompose(modifier, navController,drawerClick)
        }
    }
    composable(CourseTabs.SQUARE.route) {
        StatusBar.isTransparentState.value = false
        SquareViewCompose(
            modifier, navController,drawerClick
        )
    }
    composable(CourseTabs.PUBLIC.route) {
        StatusBar.isTransparentState.value = false
        PublicViewCompose(
            modifier, navController,drawerClick
        )
    }
    composable(CourseTabs.SYSTEM.route) {
        StatusBar.isTransparentState.value = false
        SystemViewCompose(
            modifier, navController,drawerClick
        )
    }
    composable(CourseTabs.PROJECT.route) {
        StatusBar.isTransparentState.value = false
        ProjectViewCompose(
            modifier, navController,drawerClick
        )
    }
}


sealed class CourseTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String
) {
    object HOME : CourseTabs(R.string.Home, R.drawable.svg_tab_home, CoursesDestinations.Home)
    object SQUARE : CourseTabs(R.string.Square, R.drawable.svg_tab_square, CoursesDestinations.Square)
    object PUBLIC : CourseTabs(R.string.Public, R.drawable.svg_tab_public, CoursesDestinations.Public)
    object SYSTEM : CourseTabs(R.string.System, R.drawable.svg_tab_system, CoursesDestinations.System)
    object PROJECT : CourseTabs(R.string.Project, R.drawable.svg_tab_project, CoursesDestinations.Project)


    companion object {
        val values: Array<CourseTabs>
            get() = arrayOf(HOME, SQUARE, PUBLIC, SYSTEM, PROJECT)
    }

}

object CoursesDestinations {
    const val Home = "main/home"
    const val Square = "main/square"
    const val Public = "main/public"
    const val System = "main/system"
    const val Project = "main/project"
}