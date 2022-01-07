package com.zll.compose

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.zll.compose.ext.number
import com.zll.compose.util.*
import com.zll.compose.views.TextViewLayout
import com.zll.compose.vm.CountModel
import kotlinx.coroutines.launch

class CustomActivity : AppCompatActivity() {

    private val TAG: String = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val composeView = ComposeView(this)
        setContentView(composeView)
        composeView.apply {
            // 当视图的LifecycleOwner被销毁时，处理这个Composition
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    MeView()
                }
            }
        }
        //销毁 ComposeView组合
        //composeView.disposeComposition()//
    }

    @Preview
    @Composable
    fun MeView() {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            AndroidLayoutView()
            SystemBroadcastReceiverView()
            ViewModelView("第一个人")
            ViewModelView("第二个人")
            val visible = remember {
                mutableStateOf(true)
            }
            Column() {
                Button(onClick = {visible.value= !visible.value }) {
                    Text(text = "点击")
                }
                
                if(visible.value){
                    Text(text = "显示")
                }
                
            }
        }
    }

    @Composable
    fun ViewModelView(userId: String){
        val vm :UserViewModel = viewModel(factory = UserViewModelFactory(userId))
        val message = vm.message.observeAsState("")
        Text(text = message.value)
    }

    class UserViewModel(private val userId:String):ViewModel(){
        private val _message = MutableLiveData("Hi $userId")
        val message: LiveData<String> = _message
    }

    class UserViewModelFactory(private val userId:String):ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(userId = userId) as T
        }
    }

    @Composable
    fun SystemBroadcastReceiverView() {
        val state = remember {
            mutableStateOf(0)
        }
        SystemBroadcastReceiver(systemAction = Intent.ACTION_BATTERY_CHANGED) {
            it?.let {
                val status =it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                state.value = status
                Log.e(TAG,status.toString())
            }
        }

        Text(text = "${state.value}")
    }

    @Composable
    fun SystemBroadcastReceiver(systemAction: String, onSystemIntent: (intent: Intent?) -> Unit) {
        val context = LocalContext.current
        val currentSystemIntent = rememberUpdatedState(newValue = onSystemIntent)
        DisposableEffect(context, systemAction) {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    onSystemIntent(intent)
                }
            }
            val intentFilter = IntentFilter(systemAction)
            context.registerReceiver(receiver, intentFilter)
            onDispose {
                context.unregisterReceiver(receiver)
            }
        }
    }

    @Composable
    fun AndroidLayoutView() {
        val size = remember {
            mutableStateOf(0)
        }
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                TextViewLayout(context).apply {
                    btn.setOnClickListener {
                        size.value++
                    }
                }
            },
            update = { view ->
                view.setTextView(size.value)
            }
        )
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
}