package com.zll.compose.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zll.compose.compose.model.TabBaseModel
import com.zll.compose.util.blueColor

@Composable
fun <T:TabBaseModel> TabLayoutCompose(
    tabBarIndex: MutableState<Int>,
    tabData: List<T>?
) {
    if (tabData.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .fillMaxWidth()
                .height(54.dp)
        )
        return
    }
    ScrollableTabRow(
        selectedTabIndex = tabBarIndex.value,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
            edgePadding = 0.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        tabData.forEachIndexed { index, item ->
            Tab(
                text = { Text(item.name ?: "") },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Black,
                selected = tabBarIndex.value == index,
                onClick = {
                    tabBarIndex.value = index
                }
            )
        }
    }
}