package com.zll.compose

import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zll.compose.test.WaveLoadingCompose


@Composable
fun AppComposeTest() {
    Scaffold() {
        WaveLoadingCompose(
            modifier = Modifier.size(size = 220.dp),
            text = "心",
            textSize = 150.sp,
            waveColor = Color(0xFF00897B)
        )
    }
}