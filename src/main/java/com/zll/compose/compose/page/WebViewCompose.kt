package com.zll.compose.compose.page

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsPadding
import com.zll.compose.compose.ui.TopBar

@Composable
fun WebViewCompose(navHostController: NavHostController, url: String, back: (back: Boolean) -> Unit = {}) {
    var webView: WebView? = null
    BackHandler {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            navHostController.navigateUp()
        }
    }
    val titleValue = remember {
       mutableStateOf("玩Android")
    }

    Column(
        Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        TopBar(title = titleValue.value, leftIcon = Icons.Default.ArrowBack, leftClick = {
            navHostController.navigateUp()
        })
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    settings.apply {
                        javaScriptEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                    }
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            request?.url?.let {
                                view?.loadUrl(it.toString())
                            }
                            return true
                        }
                    }
                    webChromeClient = object :WebChromeClient(){
                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            titleValue.value = title?:"玩Android"
                        }

                    }
                }
            },
            update = {
                webView = it
                it.loadUrl(url)
            }
        )
    }
}

@Composable
fun LinearProgress(process:Float){
    LinearProgressIndicator(
        progress = process,
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp),
    )
}