package com.zll.compose.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PermScanWifi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zll.compose.R
import com.zll.compose.util.white

/**
 * 无数据时候的布局
 */
@Composable
fun ErrorComposable(title: String = "网络不佳，请点击重试", block: () -> Unit) {
    Column(
        modifier = Modifier
            .background(white)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.PermScanWifi, contentDescription = null, tint = Color.Red,
            modifier = Modifier.size(50.dp)
        )
//        Image(
//            modifier = Modifier.size(300.dp, 180.dp),
//            painter = painterResource(id = R.drawable.svg_empty),
//            contentDescription = "网络问题",
//            contentScale = ContentScale.Crop
//        )
        Button(modifier = Modifier.padding(8.dp), onClick = block) {
            Text(title)
        }
    }

}