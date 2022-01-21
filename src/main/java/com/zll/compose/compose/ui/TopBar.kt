package com.zll.compose.compose.ui

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import com.zll.compose.util.blueColor
import com.zll.compose.util.white

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String, bgColor: Color = MaterialTheme.colors.primary,
    leftIcon: ImageVector? = null, menuIcon: ImageVector? = null,
    leftClick: (() -> Unit) = {}, menuClick: (() -> Unit) = {},
) {
    TopAppBar(
        modifier = modifier,
        elevation = 2.dp,
        title = {
            Text(
                title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = white,
                overflow = TextOverflow.Ellipsis, maxLines = 1,
                )
        },
        backgroundColor = bgColor,
        navigationIcon = {
            if (leftIcon != null) {
                IconButton(onClick = leftClick) {
                    Icon(leftIcon, null, tint = white)
                }
            }
        }, actions = {
            if (menuIcon != null) {
                IconButton(onClick = menuClick) {
                    Icon(menuIcon, null, tint = white)
                }
            }
        }
    )
}

@Composable
fun NavigationTopBar(navHostController: NavHostController,title: String,
                     leftIcon: ImageVector=Icons.Default.ArrowBack,leftClick: () -> Unit={}){
    TopBar(title = title,
        leftIcon = leftIcon,
        leftClick = {
        navHostController.popBackStack()
        leftClick()
    })
}