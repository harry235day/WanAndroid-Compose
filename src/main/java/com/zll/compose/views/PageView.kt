package com.zll.compose.views

import android.graphics.Bitmap
import android.view.View
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun PageErrorView(errorMsg: String? = null, clickListener: (() -> Unit)? = null) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.clickable {
            clickListener?.invoke()
        }) {
            Icon(Icons.Default.Error, contentDescription = "", tint = Color.Red)
            VerticalSpacer(5.dp)
            Text(text = errorMsg ?: "加载失败")
        }
    }
}

@Composable
fun PageEmptyView(emptyMsg: String? = null, clickListener: (() -> Unit)? = null) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.clickable {
            clickListener?.invoke()
        }) {
            Icon(Icons.Default.HourglassEmpty, contentDescription = "", tint = Color.Gray)
            VerticalSpacer(5.dp)
            Text(text = emptyMsg ?: "空空如也")
        }
    }
}


@Composable
fun VerticalSpacer(space: Dp = 10.dp) {
    Spacer(Modifier.height(space))
}

@Composable
fun HorizontalSpacer(space: Dp = 10.dp) {
    Spacer(Modifier.width(space))
}

@Composable
fun ImageCircle(@DrawableRes ids: Int, dp: Dp) {
    val modifier = Modifier
        .size(dp)
        .border(2.dp, Color.White, shape = RoundedCornerShape(dp))
        .clip(RoundedCornerShape(dp))

    Image(painter = painterResource(id = ids), modifier = modifier, contentDescription = "id", contentScale = ContentScale.Crop)
}

@Composable
fun ImageCircle(bitmap: Bitmap, dp: Dp) {
    val modifier = Modifier
        .size(dp)
        .border(2.dp, Color.White, shape = RoundedCornerShape(dp))
        .clip(RoundedCornerShape(dp))

    Image(bitmap = bitmap.asImageBitmap(), modifier = modifier, contentDescription = "id", contentScale = ContentScale.Crop)
}

@Composable
fun ImageCircle(url: String, dp: Dp) {
    val modifier = Modifier
        .size(dp)
        .border(2.dp, Color.White, shape = RoundedCornerShape(dp))
        .clip(CircleShape)
    Image(painter = rememberImagePainter(url), modifier = modifier, contentDescription = "id", contentScale = ContentScale.Crop)
}

@Composable
fun ImageFillMax(url: String, roundDp: Dp = 0.dp) {
    val modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(roundDp))
    Image(painter = rememberImagePainter(url), modifier = modifier, contentDescription = "id", contentScale = ContentScale.Crop)
}

@Composable
fun ImageWidth(url: String, width: Dp, roundDp: Dp = 0.dp) {
    val modifier = Modifier
        .fillMaxHeight()
        .width(width)
        .clip(RoundedCornerShape(roundDp))
    Image(painter = rememberImagePainter(url), modifier = modifier, contentDescription = "id", contentScale = ContentScale.Crop)
}