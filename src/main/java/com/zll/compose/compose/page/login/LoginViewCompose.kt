package com.zll.compose.compose.page.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.zll.compose.R
import com.zll.compose.compose.ui.SimpleSquareCard
import com.zll.compose.compose.util.Key
import com.zll.compose.compose.util.SpUtilsMMKV
import com.zll.compose.compose.vm.NetWorkState
import com.zll.compose.compose.vm.UserViewModel


@Composable
fun LoginViewCompose(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.mipmap.long_bg), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        LoginView(userViewModel, navController)
    }

}

@Preview("login")
@Composable
fun LoginViewPreview() {
    LoginView(UserViewModel(), rememberNavController())
}

@Composable
fun LoginView(userViewModel: UserViewModel, navController: NavHostController) {
    val userName = SpUtilsMMKV.getString(Key.KEY_USER_NAME) ?: ""
    val loading = userViewModel.loading.observeAsState()
    val loginData = userViewModel.loginData.observeAsState()

    val nameState = rememberSaveable {
        mutableStateOf(userName)
    }

    val pwdState = rememberSaveable {
        mutableStateOf("")
    }

    val pwdVisible = rememberSaveable {
        mutableStateOf(false)
    }


    val loginBtnEnable = rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = pwdState.value, key2 = nameState.value) {
        loginBtnEnable.value = pwdState.value.isNotBlank() && nameState.value.isNotBlank()
    }

    val loadingState = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(loading.value) {
        if (loadingState.value && loading.value?.state == NetWorkState.State.Loading) return@LaunchedEffect
        loadingState.value = loading.value?.state == NetWorkState.State.Loading
        if (loading.value != null && loading.value!!.state != NetWorkState.State.Loading) {
            Toast.makeText(context, loading.value!!.msg, Toast.LENGTH_LONG).show()
        }
        if (loading.value?.state == NetWorkState.State.Success) {
            focusManager.clearFocus()
            navController.popBackStack()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .clickable { focusManager.clearFocus() }, contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.long_bg), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 30.dp)) {
            Text(text = "欢迎登录WanAndroid", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextFieldView(nameState, false, true, "登录名", "请输入登录名", Icons.Default.Person) {

            }
            Box(
                Modifier.padding(top = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                OutlinedTextFieldView(pwdState, true, pwdVisible.value, "密码", "请输入密码", Icons.Default.Lock) {

                }
                IconButton(
                    onClick = {
                        pwdVisible.value = !pwdVisible.value
                    }) {
                    val icon = if (pwdVisible.value) {
                        Icons.Default.Visibility
                    } else {
                        Icons.Default.VisibilityOff
                    }
                    Icon(icon, contentDescription = null)
                }
            }

            Button(
                enabled = loginBtnEnable.value,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    userViewModel.login(nameState.value, pwdState.value)

                }) {
                Text(text = "登录")
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = loadingState.value
        ) {
            LoginLoadingViewCompose()
        }

    }

}


@Preview("CircularProgressIndicator")
@Composable
fun LoginLoadingViewCompose() {
    Box(
        Modifier
            .fillMaxSize()
            .clickable { },
        contentAlignment = Alignment.Center,
    ) {
        SimpleSquareCard {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun OutlinedTextFieldView(
    value: MutableState<String>, isPwd: Boolean, isVisual: Boolean, label: String, placeholder: String, leadIcon: ImageVector, onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value.value,
        label = {
            Text(text = label, fontSize = 14.sp)
        },
        placeholder = {
            Text(text = placeholder, fontSize = 14.sp)
        },
        singleLine = true,
        leadingIcon = {
            Icon(leadIcon, contentDescription = null)
        },
        trailingIcon = {
            if (!isPwd && value.value.isNotBlank()) {
                Icon(Icons.Default.Clear, contentDescription = null, Modifier.clickable {
                    value.value = ""
                })
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = if (isVisual) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = {
            value.value = it
        })
}


