package com.zll.compose.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.composethemeadapter.MdcTheme
import com.zll.compose.model.ArticleListModel
import com.zll.compose.util.blackColor
import com.zll.compose.views.HorizontalSpacer
import com.zll.compose.views.PageEmptyView
import com.zll.compose.views.PageErrorView
import com.zll.compose.vm.ArticleViewModel

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
    private fun PagIngView(vm: ArticleViewModel = hiltViewModel()) {

    }


    @Composable
    fun SuccessView(data: List<ArticleListModel>?) {
        if (data.isNullOrEmpty()) {
            PageEmptyView()
        } else {
            PageListView(data)
        }
    }

    @Composable
    fun PageListView(data: List<ArticleListModel>) {
        LazyColumn(Modifier.fillMaxSize()) {
            for (item in data) {
                item {
                    ArticleView(item)
                }
            }
        }
    }

    @Composable
    fun ArticleView(item: ArticleListModel) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.author ?: "暂无", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = blackColor)
            Row {
                Icon(Icons.Default.Assistant, contentDescription = "")
                HorizontalSpacer(2.dp)

            }
        }
    }

}