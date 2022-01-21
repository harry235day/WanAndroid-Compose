package com.zll.compose.compose.page.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.zll.compose.compose.navSearchResult
import com.zll.compose.compose.page.main.NavTagView
import com.zll.compose.compose.ui.BaseView
import com.zll.compose.compose.util.Constant
import com.zll.compose.compose.vm.SettingViewModel
import com.zll.compose.util.black3
import com.zll.compose.util.grey5
import com.zll.compose.util.white5
import kotlin.random.Random

@Composable
fun SearchViewCompose(navController: NavHostController) {

    val settingViewModel: SettingViewModel = viewModel()

    val hotListData = settingViewModel.hotListData.observeAsState().value
    val loadSearchList = settingViewModel.loadSearchList.observeAsState().value

    settingViewModel.loadHotKey()
    settingViewModel.loadSearchList()

    val result = rememberSaveable {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    BaseView {
        Column {
            SearchBar(result, leftClick = {
                focusManager.clearFocus()
                navController.popBackStack()
            }, searchClick = {
                if(result.value.isNotBlank()) {
                    focusManager.clearFocus()
                    settingViewModel.addSearch(result.value)
                    navSearchResult(navController, result.value)
                }
            })
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = settingViewModel.searchLazyListState,
            ) {
                item {
                    Text(
                        text = "热门搜索", fontSize = 16.sp, color = Color.Green, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                item {
                    hotListData?.let {
                        FlowRow(mainAxisSpacing = 10.dp, crossAxisSpacing = 10.dp, modifier = Modifier.padding(start = 5.dp, top = 10.dp)) {
                            it.forEachIndexed { index, item ->
                                TagView(navController, MaterialTheme.colors.primary, item.name ?: "", item.id)
                            }
                        }
                    }
                }
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "搜索历史", fontSize = 16.sp, color = Color.Green, fontWeight = FontWeight.Bold)
                        IconButton(onClick = {
                            settingViewModel.deleteAll()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = grey5)
                        }
                    }
                }
                items(loadSearchList?.size ?: 0) { index ->
                    ResultView(navController, settingViewModel, loadSearchList!![index].title) {
                        settingViewModel.delete(loadSearchList[index].title)
                    }
                }
            }
        }
    }
}

@Composable
fun ResultView(navController: NavHostController, settingViewModel: SettingViewModel, title: String, clearClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .clickable {
                settingViewModel.addSearch(title)
                navSearchResult(navController, title)
            }
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 14.sp, color = black3)
        IconButton(
            modifier = Modifier.size(16.dp),
            onClick = {
                clearClick()
            }) {
            Icon(Icons.Default.Clear, contentDescription = null, tint = grey5)
        }
    }
}

@Composable
fun TagView(navController: NavHostController, color: Color, title: String, id: Int) {
    Text(
        text = title, fontSize = 14.sp, color = color,
        modifier = Modifier
            .clip(RoundedCornerShape(2.dp))
            .clickable {
                navSearchResult(navController, title)
            }
            .background(white5)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}


@Composable
private fun SearchBar(result: MutableState<String>, leftClick: () -> Unit = {}, searchClick: () -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colors.primary)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {
            leftClick()
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
        }
        TextField(
            modifier = Modifier
                .weight(1f, true)
                .fillMaxHeight()
                .padding(horizontal = 10.dp),
            value = result.value,
            placeholder = {
                Text(text = "发现更多干货", fontSize = 14.sp)
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                cursorColor = Color.White,
                placeholderColor = white5,
                backgroundColor = MaterialTheme.colors.primary
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    searchClick()
                }
            ),
            trailingIcon = {
                if (result.value.isNotBlank()) {
                    Icon(
                        Icons.Default.Clear,
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            result.value = ""
                        })
                }
            },
            onValueChange = {
                result.value = it
            })
        IconButton(onClick = {
            searchClick()
        }) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
        }
    }
}

