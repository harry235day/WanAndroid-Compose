package com.zll.compose.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.WindowCompat
import com.google.android.material.composethemeadapter.MdcTheme

class PagIngActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
//        window.statusBarColor = android.R.color.transparent
        super.onCreate(savedInstanceState)
        val composeView = ComposeView(this)
        setContentView(composeView)
        composeView.apply {
            // 当视图的LifecycleOwner被销毁时，处理这个Composition
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                   PagIngView()
                }
            }
        }
        //销毁 ComposeView组合
        //composeView.disposeComposition()//
    }

    @Composable
    private fun PagIngView() {

    }
}