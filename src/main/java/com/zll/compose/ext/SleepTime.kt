package com.zll.compose.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun sleepTime(millis: Long = 1500, block: () -> Unit) {
    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            delay(millis)
            block()
        }
    }
}

/**
 * 开启一个IO线程，一段时间后执行代码
 */
fun ViewModel.sleepTime(millis: Long = 1500, block: () -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        delay(millis)
        block()
    }
}