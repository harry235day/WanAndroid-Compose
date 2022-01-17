package com.zll.compose.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsHeight
import com.zll.compose.util.blueColor

@Composable
fun BaseView(content: @Composable () -> Unit) {
    Column() {
        Spacer(modifier = Modifier
            .statusBarsHeight()
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary))
        content()
        Spacer(modifier = Modifier
            .navigationBarsHeight()
            .fillMaxWidth()
            .background(Color.Black))
    }
}