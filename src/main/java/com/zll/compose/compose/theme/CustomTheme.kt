package com.zll.compose.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zll.compose.compose.util.StatusBar

object CustomTheme {
    object Default {
        val darkColors = darkColors(
            primary = Default300,
            primaryVariant = Default600,
            secondary = DefaultSecondary,
            secondaryVariant = Color.White,
            background = Color.Black,
            surface = Color.White,
        )

        val lightColors = lightColors(
            primary = Default100,
            primaryVariant = Default600,
            secondary = DefaultSecondary,
            secondaryVariant = Color.Black,
            background = Color.White,
            surface = DefaultOnPrimary
        )
    }

    @Composable
    fun AppTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val colors = if (darkTheme) Default.darkColors else Default.lightColors

        val systemUiCtrl = rememberSystemUiController()
        systemUiCtrl.setStatusBarColor(colors.primary)
        systemUiCtrl.setSystemBarsColor(colors.primary)
        systemUiCtrl.setNavigationBarColor(if (darkTheme) Color.Black else Color.White)

        LaunchedEffect(StatusBar.isTransparentState.value) {
            if (StatusBar.isTransparentState.value) {
                systemUiCtrl.setStatusBarColor(Color.Transparent)
            } else {
                systemUiCtrl.setStatusBarColor(colors.primary)
            }
        }

        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }

}