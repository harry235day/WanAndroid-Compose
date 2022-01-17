package com.zll.compose.compose.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun NestTopAndContentView(
    modifier: Modifier = Modifier.fillMaxSize(),
    topBarOtherDp: Dp = 0.dp,
    state: State<Boolean> = mutableStateOf(true),
    topBar: @Composable (offset: IntOffset) -> Unit,
    content: @Composable (offset: IntOffset, toolbarHeight: Dp) -> Unit
) {
    val toolbarHeight = 50.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val current = LocalDensity.current
    val toolbarOffsetHeightDp = remember { mutableStateOf(toolbarHeight) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                val dp = with(current) { (toolbarHeightPx - abs(newOffset.coerceIn(-toolbarHeightPx, 0f))).toDp() }
                toolbarOffsetHeightDp.value = dp
                return Offset.Zero
            }
        }
    }
    Box(
        modifier
            .statusBarsPadding()
            .nestedScroll(nestedScrollConnection)
    ) {
        Box(
            Modifier
                .fillMaxSize().padding(top = topBarOtherDp)
        ) {
            content(IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()), toolbarOffsetHeightDp.value)
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
        ) {
            topBar(IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()))
        }
    }
}