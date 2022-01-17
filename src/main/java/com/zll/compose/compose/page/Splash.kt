package com.zll.compose.compose.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.insets.ProvideWindowInsets
import com.zll.compose.ui.MaxChartValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Composable
fun Splash(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onComplete: () -> Unit
) {
    val MAX = 1
    ProvideWindowInsets {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            val count = remember {
                mutableStateOf(MAX)
            }
            Text(text = "倒计时: ${count.value}")
            val launch = rememberCoroutineScope()
            DisposableEffect(lifecycleOwner) {
                var job: Job? = null
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_CREATE) {
                        kotlin.runCatching {
                            val flow = flow {
                                repeat(MAX) {
                                    kotlinx.coroutines.delay(1000)
                                    emit(it)
                                }
                            }
                            job = launch.launch {
                                flow.catch {
                                    onComplete.invoke()
                                }.collect {
                                    count.value = it
                                    if (it == 0) {
                                        onComplete.invoke()
                                    }
                                }
                            }
                        }
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    job?.cancel()
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

        }
    }
}