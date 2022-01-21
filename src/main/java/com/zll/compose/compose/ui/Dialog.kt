package com.zll.compose.compose.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@Composable
fun CommonDialog(
    openDialog: MutableState<Boolean>, title: String,
    content: String = "", dismissButton: String = "取消", confirmButton: String = "确定",
    confirmListener: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        },
        text = {
            Text(text = content, fontSize = 14.sp, color = Color.Gray)
        },
        confirmButton = {
            Button(onClick = {
                openDialog.value = false
                confirmListener()
            }) {
                Text(text = confirmButton)
            }
        },
        dismissButton = {
            Button(onClick = {
                openDialog.value = false

            }) {
                Text(text = dismissButton)
            }
        }
    )
}