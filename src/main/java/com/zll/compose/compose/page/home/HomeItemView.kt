package com.zll.compose.compose.page.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zll.compose.util.*

@Composable
fun HomeItemView(
    author: String,
    //新的内容
    fresh: Boolean,
    //是否置顶,
    stick: Boolean = false,
    //发布时间
    niceDate: String,
    //标题
    title: String,
    //来源
    superChapterName: String,
    //是否收藏
    collect: Boolean,
    //是否显示具体时间
    isSpecific: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column(
        //内边距
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxSize()
            .padding(12.dp)
    ) {
        TopCardView(author, fresh, stick, niceDate)
        CenterCard(title, Modifier.weight(1f, true))
        BottomCard(superChapterName,collect)
    }
}

/**
 * 底部部分
 */
@Composable
private fun BottomCard(
    //渠道名
    chapterName: String,
    //是否收藏
    collect: Boolean
) {
    Row(
        modifier = Modifier.padding(start = 6.dp, top = 2.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        redText(chapterName)
        Spacer(modifier = Modifier.weight(1f, true))
        //收藏
        Icon(
            Icons.Default.Favorite,
            contentDescription = null,
            tint = if (collect) Color.Red else Color.LightGray
        )
    }

}


/**
 * 中间部分
 */
@Composable
private fun CenterCard(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(start = 6.dp,top = 6.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = blackColor,
            maxLines = 2
        )
    }

}

@Composable
fun TopCardView(
    author: String,
    fresh: Boolean,
    stick: Boolean = false,
    niceDate: String,
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if (stick) {
            borderText("置顶")
        }
        if (fresh) {
            borderText("最新", modifier = Modifier.padding(start = 6.dp))
        }
        grayText(author, Modifier.padding(start = 6.dp))
        Box(modifier = Modifier.weight(1f, true), contentAlignment = Alignment.CenterEnd) {
            grayText(niceDate)
        }
    }
}

@Composable
private fun borderText(str: String, color: Color = Color.Red, modifier: Modifier = Modifier) {
    Text(
        str,
        color = color,
        fontSize = 10.sp,
        modifier = modifier
            .border(BorderStroke(1.dp, color = color), RoundedCornerShape(4.dp))
            .padding(start = 3.dp, end = 3.dp)
    )

}


/**
 * 红色文字
 */
@Composable
private fun redText(str: String = "", modifier: Modifier = Modifier) {
    Text(
        str,
        modifier = modifier,
        color = red,
        fontSize = H6
    )
}

/**
 * 灰色文字
 */
@Composable
private fun grayText(str: String = "", modifier: Modifier = Modifier) {
    Text(
        str,
        modifier = modifier,
        color = blackThreeColor,
        fontSize = H7
    )
}

/**
 * 获取发布者昵称
 */
fun getAuthor(author: String?, shareUser: String?): String {
    return when {
        //isNotBlank 不为空且包含空字符以外的内容
        author?.isNotBlank() == true -> author
        shareUser?.isNotBlank() == true -> shareUser
        else -> ""
    }
}

