package com.zll.compose

import android.content.BroadcastReceiver
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class LayoutActivity1 : AppCompatActivity() {

    var check = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            var count = 0
//            ClickCounter(count) {
//                count++
//            }
//            sharedSetting(check){
//                check = it
//                Log.e("TAG",it.toString())
//            }
//            lazyColumn()

            computeView()
        }
    }


    /**
     * 计数器
     */
    @Composable
    fun computeView() {
        val inputText = remember {
            mutableStateOf("")
        }
        Column(Modifier.padding(16.dp)) {
            Text("HELLO", Modifier.padding(bottom = 8.dp), style = MaterialTheme.typography.h4)
            OutlinedTextField(
                modifier = Modifier.padding(bottom = 10.dp),
                value = inputText.value,
                onValueChange = { inputText.value = it },
                label = { Text("NAME") })
            Counter()
        }

    }

    @Composable
    fun Counter() {
        var counter  =  remember { mutableStateOf(0) }
        Button(onClick = { counter.value = counter.value+1 }) {
            Text(text = counter.value.toString())
        }
    }


    @Composable
    fun NewsStory() {
        MaterialTheme() {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Image(
                    painter = painterResource(R.mipmap.head1),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "A day wandering through the sandhills  in Shark Fin Cove, and a few of thesights I saw",
                    style = typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text("Davenport, California", style = typography.body1)
                Text("December 2018", style = typography.body2)

            }
        }
    }

    @Composable
    fun ClickCounter(count: Int, click: () -> Unit) {
        Button(onClick = click) {
            Text(text = "I've been clicked $count times")
        }
    }

    @Composable
    fun sharedSetting(check: Boolean, onChange: (Boolean) -> Unit) {
        Row() {
            Text(text = "AAA")
            Checkbox(checked = check, onCheckedChange = onChange)
        }
    }

    @Preview
    @Composable
    fun DefaultPreview() {
        NewsStory()

    }

    val names = arrayListOf("1", "2", "3", "4", "5", "6")

    @Composable
    fun lazyColumn() {
        LazyColumn() {
            items(names.size) { index ->
                NewsStory()
            }
        }
    }

    @Composable
    fun editColumn() {


        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Hello!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = typography.h5
            )

        }
    }

}