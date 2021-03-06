package com.zll.compose

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.material.composethemeadapter.MdcTheme
import com.zll.compose.ext.number
import com.zll.compose.util.*
import com.zll.compose.views.HorizontalSpacer
import com.zll.compose.views.ImageCircle
import com.zll.compose.views.VerticalSpacer
import com.zll.compose.vm.CountModel

@ExperimentalPermissionsApi
class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
//        window.statusBarColor = android.R.color.transparent
        super.onCreate(savedInstanceState)
        val composeView = ComposeView(this)
        setContentView(composeView)
        composeView.apply {
            // ????????????LifecycleOwner???????????????????????????Composition
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    MeView()
                }
            }
        }
        //?????? ComposeView??????
        //composeView.disposeComposition()//
    }

    @ExperimentalPermissionsApi
    @Preview("mainActivity1")
    @Composable
    fun MeView() {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val hiltViewModel = hiltViewModel<CountModel>()
            val viewModel = viewModel<CountModel>()
            Text(text = "${hiltViewModel.c}")
            Text(text = "${viewModel.c}")
            if (minWidth > 400.dp) {
                HorizontalView(minWidth / 2)
            } else {
                VerticalView()
            }
        }

        val backHandlingEnabled = remember { mutableStateOf(true) }
        BackHandler(backHandlingEnabled.value) {
            // Handle back press
            Log.e("TAG","BACK")
            finish()
        }
    }

    @Composable
    fun HorizontalView(dp: Dp) {
        Row(
            Modifier
                .fillMaxSize(),
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(dp)
            ) {
                HeadView(false)
            }
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(dp)
                    .padding(top = 50.dp)
            ) {
                AttentionView()
                DividerView()
                ContactView()
                DividerView()
                CacheView()
                DividerView()
                LoginView()
            }
        }
    }

    @Composable
    fun VerticalView() {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            HeadView(true)
            HeadLineSpace()
            AttentionView()
            DividerView()
            ContactView()
            DividerView()
            CacheView()
            DividerView()

            LoginView()

        }
    }


    @Composable
    fun LoginView() {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = 30.dp), contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(bgColor)
                    .clickable {

                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "????????????", fontSize = 16.sp, color = redColor)
            }
        }
    }

    @Composable
    fun HeadLineSpace() {
        Box(
            Modifier
                .fillMaxWidth()
                .height(22.dp)
                .background(blueColor)
        ) {
            Box(
                Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .fillMaxWidth()
                    .height(22.dp)
                    .background(Color.White)
            )
        }
    }

    @Composable
    fun DividerView(color: Color = lineColor) {
        Divider(
            Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp), color = color
        )
    }

    @Composable
    fun AttentionView() {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .clickable {

                }
            , horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemLeftView(ids = R.drawable.ic_launcher, title = "????????????")
            Text(text = "?????????", fontSize = 16.sp, color = blackThreeColor)
        }
    }

    @Composable
    fun ContactView() {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 20.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemLeftView(ids = R.drawable.ic_launcher, title = "????????????")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "18000000000", fontSize = 16.sp, color = blackThreeColor)
                Image(painter = painterResource(id = R.drawable.svg_right_gray), contentDescription = "")
            }
        }
    }

    @Composable
    fun CacheView() {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 20.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemLeftView(ids = R.drawable.ic_launcher, title = "????????????")
            Text(text = "112MB", fontSize = 16.sp, color = blackThreeColor)
        }
    }

    @Composable
    fun HeadView(isVertical: Boolean) {
        val cbg = Brush.horizontalGradient(
            listOf(
                blueColor,
                blueSecondColor
            )
        )
        val modifier = if (isVertical) {
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(cbg)
                .padding(bottom = 44.dp, top = 50.dp)
        } else {
            Modifier
                .fillMaxSize()
                .background(cbg)
                .padding(bottom = 44.dp, top = 50.dp)
        }

        val result = remember {
            mutableStateOf<Bitmap?>(null)
        }
        
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()){
            result.value =it
        }

        Box(
            modifier
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        "?????????????????????????????????".number(6), fontSize = 14.sp,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,

                        )
                }
                VerticalSpacer(22.dp)
                Box(
                    Modifier
                        .clickable {
                            launcher.launch()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if(result.value!=null){
                        ImageCircle(result.value!!, 80.dp)
                    }else{
                        ImageCircle(R.drawable.ic_launcher, 80.dp)
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 6.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "?????????", fontSize = 24.sp, fontWeight = FontWeight.Medium,
                        color = Color.White, fontFamily = FontFamily.SansSerif
                    )
                    HorizontalSpacer(7.dp)
                    TagView1()
                }

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "13245680098", fontSize = 15.sp, color = Color.White)
                }
            }
        }
    }

    @Composable
    fun ItemLeftView(@DrawableRes ids: Int, title: String) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = ids), contentDescription = "ids", Modifier.size(18.dp), contentScale = ContentScale.Crop)
            HorizontalSpacer(10.dp)
            Text(text = title, fontSize = 16.sp, color = blackColor, fontWeight = FontWeight.Medium)
        }
    }

    @Composable
    fun TagView1() {
        val cbg = Brush.horizontalGradient(
            listOf(
                Color(0xFFFFE8D3),
                Color(0xFFCFA67A)
            )
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(cbg)
                .padding(horizontal = 8.dp, vertical = 2.dp)

        ) {
            Text(text = "??????", color = Color(0xFF665346), fontSize = 12.sp)
        }
    }


    @Composable
    fun BtnSimple() {
        val count = rememberSaveable() {
            mutableStateOf(0)
        }
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                count.value++
            }) {
                Text("??????")
            }
            HorizontalSpacer()
            Text("${count.value}")
        }
    }




    @ExperimentalPermissionsApi
    @Composable
    private fun FeatureThatRequiresCameraPermission(
        navigateToSettingsScreen: () -> Unit
    ) {
        // Track if the user doesn't want to see the rationale any more.
        val doNotShowRationale  =  rememberSaveable { mutableStateOf(false) }

        val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
        PermissionRequired(
            permissionState = cameraPermissionState,
            permissionNotGrantedContent = {
                if (doNotShowRationale.value) {
                    Text("???????????????")
                } else {
                    Column {
                        Text("????????????????????????????????????????????????????????????")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                                Text("??????")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = { doNotShowRationale.value = true }) {
                                Text("?????????")
                            }
                        }
                    }
                }
            },
            permissionNotAvailableContent = {
                Column {
                    Text(
                        "?????????????????????????????????????????????????????????????????????????????????+???\n" +
                                "????????????????????????????????????????????????????????????"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = navigateToSettingsScreen) {
                        Text("????????????")
                    }
                }
            }
        ) {
            Text("??????????????????")
        }
    }


    @ExperimentalPermissionsApi
    @Composable
    private fun FeatureThatRequiresCameraPermission1(
        navigateToSettingsScreen: () -> Unit
    ) {
        // Track if the user doesn't want to see the rationale any more.
        val doNotShowRationale  =  rememberSaveable { mutableStateOf(false) }

        // Camera permission state
        val cameraPermissionState = rememberPermissionState(
            android.Manifest.permission.CAMERA
        )
        when {
            // If the camera permission is granted, then show screen with the feature enabled
            cameraPermissionState.hasPermission -> {
                Text("Camera permission Granted")
            }
            // If the user denied the permission but a rationale should be shown, or the user sees
            // the permission for the first time, explain why the feature is needed by the app and allow
            // the user to be presented with the permission again or to not see the rationale any more.
            cameraPermissionState.shouldShowRationale ||
                    !cameraPermissionState.permissionRequested -> {
                if (doNotShowRationale.value) {
                    Text("Feature not available")
                } else {
                    Column {
                        Text("The camera is important for this app. Please grant the permission.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                                Text("Request permission")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = { doNotShowRationale.value = true }) {
                                Text("Don't show rationale again")
                            }
                        }
                    }
                }
            }
            // If the criteria above hasn't been met, the user denied the permission. Let's present
            // the user with a FAQ in case they want to know more and send them to the Settings screen
            // to enable it the future there if they want to.
            else -> {
                Column {
                    Text(
                        "Camera permission denied. See this FAQ with information about why we " +
                                "need this permission. Please, grant us access on the Settings screen."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = navigateToSettingsScreen) {
                        Text("Open Settings")
                    }
                }
            }
        }
    }



}