package com.zll.compose.compose.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun DividerCompose(
    modifier: Modifier = Modifier.fillMaxWidth(),
    height: Dp = 1.dp, color: Color = Color.Gray
) {
    Divider(modifier.height(height),color,)
}