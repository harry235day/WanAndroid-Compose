package com.zll.compose.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.zll.compose.R
import com.zll.compose.model.Message
import com.zll.compose.vm.CountModel
import kotlin.math.roundToInt
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zll.compose.ui.ChartDataPreview
import com.zll.compose.ui.ConstraintLayoutContent
import com.zll.compose.views.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MeActivity : AppCompatActivity() {

    private val vm by viewModels<CountModel>()

    public override fun onCreate(savedInstanceState: Bundle?) {
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        window.statusBarColor = android.R.color.transparent
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
//                Scaffold(
//                    drawerContent = {
//                        Text("Drawer title", modifier = Modifier.padding(16.dp))
//                        Divider()
//                    }
//                ) {
//                    Conversation(vm)
//                }

                Scaffold() {
                    Conversation(vm)
                }

//                val drawerState = rememberDrawerState(DrawerValue.Closed)
//                //BottomDrawer
//                //BottomSheetScaffold
//                ModalDrawer(
//                    drawerState = drawerState,
//                    drawerContent = {
//                        ConstraintLayoutContent()
//                    }
//                ) {
//                    Conversation(vm)
//                }
            }
        }
    }


    @ExperimentalMaterialApi
    @Composable
    fun BackdropScaffoldAPP() {
        val scaffoldState = rememberBackdropScaffoldState(
            BackdropValue.Concealed
        )
        val scope = rememberCoroutineScope()
        BackdropScaffold(
            scaffoldState = scaffoldState,
            appBar = {
                TopAppBar(
                    title = { Text("Backdrop") },
                    navigationIcon = {
                        if (scaffoldState.isConcealed) {
                            IconButton(
                                onClick = {
                                    scope.launch { scaffoldState.reveal() }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    scope.launch { scaffoldState.conceal() }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close"
                                )
                            }
                        }
                    },
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent
                )
            },
            backLayerContent = {
                // Back layer content
            },
            frontLayerContent = {
                // Front layer content
            }
        )
    }


    @Composable
    fun Api() {
        val mutableState1 = remember { mutableStateOf(false) }
        var value2 by remember { mutableStateOf(false) }
        var (value1, setValue1) = remember { mutableStateOf(false) }
        value1 = false
        setValue1 = {
            true
        }
        val liveData = MutableLiveData<String>()
        val lData: String by liveData.observeAsState("initial")

        val stateFlow = MutableStateFlow("")
        val valueStateFlow: String by stateFlow.collectAsState()


    }

    data class City(val name: String, val country: String)


    @Composable
    fun CityScreen() {

        //MapSaver 定义自己的规则
        //规定如何将对象转换为系统可保存到 Bundle 的一组值。

        val CitySaver = run {
            val nameKey = "Name"
            val countryKey = "Country"
            mapSaver(
                save = { mapOf(nameKey to it.name, countryKey to it.country) },
                restore = { City(it[nameKey] as String, it[countryKey] as String) }
            )
        }
        //为了避免需要为映射定义键，您也可以使用 listSaver 并将其索引用作键：
        val CitySaver2 = listSaver<City, Any>(
            save = { listOf(it.name, it.country) },
            restore = { City(it[0] as String, it[1] as String) }
        )
        var selectedCity = rememberSaveable(stateSaver = CitySaver) {
            mutableStateOf(City("Madrid", "Spain"))
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun Conversation(vm: CountModel) {

        val nav = rememberNavController()

        NavHost(navController = nav, startDestination = "app2") {
            composable("app1") {
                App1(vm, nav)
            }
            composable("app2") {
                App2(vm, nav)
            }
            composable("barchart"){
                ChartDataPreview()
            }
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @Composable
    fun App2(vm: CountModel, nav: NavHostController) {
//        LazyColumn {
//            item {
//                Button(
//                    modifier = Modifier.fillMaxWidth(),
//                    onClick = {
//                        nav.navigate("app1")
//                    }) {
//                    Text(text = "Nav")
//                }
//            }
//        }
//        AnimationView()
        PointerView()
//        NestScrollView()
//        NestScrollView2()
    }




    @Composable
    fun App1(vm: CountModel, nav: NavHostController) {
        val list = remember {
            mutableStateOf(arrayListOf<Message>())
        }
        repeat(8) {
            list.value.add(Message())
        }
        val state = rememberScrollState()
        val c = vm.liveData.observeAsState()

        Column(
            modifier = Modifier
                .verticalScroll(state)
                .fillMaxHeight()
        ) {
            Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    list.value.add(Message())
                    vm.add()
                }) {
                Text(text = "ADD ${c.value}")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    nav.navigate("app2")
                }) {
                Text(text = "Nav")
            }
            BoxWithConstraintCard("Title", "Description")
            SimpleClickableText()
            Spacer(modifier = Modifier.height(2.dp))
            AnnotatedClickableText()
            Spacer(modifier = Modifier.height(2.dp))
            SimpleFilledTextFieldSample()
            Spacer(modifier = Modifier.height(2.dp))
            SimpleOutlinedTextFieldSample()
            Spacer(modifier = Modifier.height(2.dp))
            PasswordTextField()
            Spacer(modifier = Modifier.height(2.dp))
            NoLeadingZeroes()
            Spacer(modifier = Modifier.height(2.dp))
            HelloContent()
            Spacer(modifier = Modifier.height(2.dp))
            HelloScreen()
            Spacer(modifier = Modifier.height(2.dp))
            MoveBoxWhereTapped()
            Spacer(modifier = Modifier.height(2.dp))

            VerticalGrid(
                Modifier
                    .background(Color.Gray)
                    .padding(10.dp), 3
            ) {
                Text(text = "AAA")
                Text(text = "AAA")
                Text(text = "AAA")
                Text(text = "AAA")
                Text(text = "AAA")
                Text(text = "AAA")
                Text(text = "AAA")
                Text(text = "AAA")
                Text(text = "AAA")
                Text(text = "AAA")
            }

            Spacer(modifier = Modifier.height(2.dp))
            MyBaseColumn(Modifier.padding(10.dp)) {
                Text(text = "AAA")
                Text(text = "AAA")
                Text(text = "AAA")
                Text(text = "AAA")
            }
            Spacer(modifier = Modifier.height(2.dp))
            HelloScreen()
            LazyColumn(Modifier.height(300.dp)) {
                itemsIndexed(list.value) { index, message ->
                    MessageCard(index, message)
                }
            }
        }
    }

    @Composable
    fun BoxWithConstraintCard(
        title: String,
        description: String
    ) {
        BoxWithConstraints {
            if (maxWidth < 400.dp) {
                Column {
                    Box(
                        Modifier
                            .size(50.dp)
                            .background(Color.Cyan))
                    Text(title)
                }
            } else {
                Row {
                    Column {
                        Text(title)
                        Text(description)
                    }
                    Box(
                        Modifier
                            .size(50.dp)
                            .background(Color.Cyan))
                }
            }
        }
    }


    @Composable()
    fun ModalDrawerTap() {

    }

    fun Modifier.firstBaselineToTop(
        firstBaselineToTop: Dp
    ) = layout { measurable, constraints ->
        // Measure the composable
        val placeable = measurable.measure(constraints)

        // Check the composable has a first baseline
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            // Where the composable gets placed
            placeable.placeRelative(0, placeableY)
        }
    }


    @Composable
    fun MoveBoxWhereTapped() {
        // Creates an `Animatable` to animate Offset and `remember` it.
        val animatedOffset = remember {
            Animatable(Offset(0f, 0f), Offset.VectorConverter)
        }
        val coroutineScope = rememberCoroutineScope()
        Box(
            // The pointerInput modifier takes a suspend block of code
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // Create a new CoroutineScope to be able to create new
                    // coroutines inside a suspend function
                    coroutineScope {
                        while (true) {
                            val offset = awaitPointerEventScope {
                                awaitFirstDown().position
                            }
                            // Launch a new coroutine to asynchronously animate to where
                            // the user tapped on the screen
                            launch {
                                // Animate to the pressed position
                                animatedOffset.animateTo(offset)
                            }
                        }
                    }
                }
                .background(Color.Cyan)
        ) {
            Text("Tap anywhere", Modifier.align(Alignment.Center))
            Box(
                Modifier
                    .offset {
                        // Use the animated offset as the offset of this Box
                        IntOffset(
                            animatedOffset.value.x.roundToInt(),
                            animatedOffset.value.y.roundToInt()
                        )
                    }
                    .size(40.dp)
                    .background(Color(0xff3c1361), CircleShape)
            )
        }
    }


    //状态下降、事件上升的这种模式称为“单向数据流”
    @Composable
    fun HelloScreen() {
        var name by rememberSaveable { mutableStateOf("") }

        HelloContent(name = name, onNameChange = { name = it })
    }

    @Composable
    fun HelloContent(name: String, onNameChange: (String) -> Unit) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Hello, $name",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Name") }
            )
        }
    }

    @Composable
    fun HelloContent() {
        Column(modifier = Modifier.padding(16.dp)) {
            var name by remember { mutableStateOf("") }
            if (name.isNotEmpty()) {
                Text(
                    text = "Hello, $name!",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.h5
                )
            }
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
        }
    }

    @Composable
    fun SimpleClickableText() {
        //可点击的Text
        ClickableText(
            text = AnnotatedString("Click Me"),
            onClick = { offset ->
                Log.e("ClickableText", "$offset -th character is clicked.")
            }
        )
    }

    @Composable
    fun AnnotatedClickableText() {
        val annotatedText = buildAnnotatedString {
            append("Click ")

            // We attach this *URL* annotation to the following content
            // until `pop()` is called
            pushStringAnnotation(
                tag = "URL",
                annotation = "https://www.baidu.com/"
            )
            withStyle(
                style = SpanStyle(
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold, fontSize = 30.sp
                )
            ) {
                append("here")
            }
            pop()
        }

        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                // We check if there is an *URL* annotation attached to the text
                // at the clicked position
                annotatedText.getStringAnnotations(
                    tag = "URL", start = offset,
                    end = offset
                )
                    .firstOrNull()?.let { annotation ->
                        // If yes, we log its value
                        Log.d("Clicked URL", annotation.item)
                    }
            }
        )
    }

    @Composable
    fun SimpleFilledTextFieldSample() {
        var text by remember { mutableStateOf("Hello") }
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") },
            modifier = Modifier.background(Color.White)
        )
    }

    @Composable
    fun SimpleOutlinedTextFieldSample() {
        var text by remember { mutableStateOf("Hello") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") }
        )
    }

    @Composable
    fun PasswordTextField() {
        var password by rememberSaveable { mutableStateOf("") }

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }

    @Composable
    fun NoLeadingZeroes() {
        var input by rememberSaveable { mutableStateOf("") }
        TextField(
            value = input,
            onValueChange = { newText ->
                //过滤字符
                input = newText.trimStart { it == '0' }
            }
        )
    }

    @Composable
    fun MessageCard(index: Int, msg: Message) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))

            // We keep track if the message is expanded or not in this
            // variable
            var isExpanded by remember { mutableStateOf(false) }

            // We toggle the isExpanded variable when we click on this Column
            Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Text(
                    text = "$index  ${msg.author}",
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 4.dp,
                ) {
                    //启用文字选择
                    //禁用
                    //DisableSelection {
                    //                Text("But not this one")
                    //                Text("Neither this one")
                    //            }
                    SelectionContainer() {
                        Text(
                            text = msg.body,
                            modifier = Modifier.padding(all = 4.dp),
                            // If the message is expanded, we display all its content
                            // otherwise we only display the first line
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }


//    @Composable
//    fun MvvmApp(
//        mvvmViewModel: MvvmViewModel
//    ) {
//        val navController = rememberNavController()
//
//        LaunchedEffect(Unit) {
//            mvvmViewModel.navigateToResults
//                .collect {
//                    navController.navigate("result") //订阅VM路由事件通知，处理路由跳转
//                }
//        }
//
//        NavHost(navController, startDestination = "searchBar") {
//            composable("searchBar") {
//                MvvmSearchBarScreen(
//                    mvvmViewModel,
//                )
//            }
//            composable("result") {
//                MvvmSearchResultScreen(
//                    mvvmViewModel,
//                )
//            }
//        }
//    }


}


//    @Preview
//    @Composable
//    fun customAnimationTest(){
//        val big = remember() {
//            mutableStateOf(true)
//        }
//
//        val customAnimation = remember{
//            Animatable<CustomSize,AnimationVector2D>(initialValue = if(big.value) CustomSize(200.dp,200.dp) else CustomSize(80.dp,80.dp),TwoWayConverter(
//                convertFromVector = {
//                    CustomSize(it.v1.dp,it.v2.dp)
//                },
//                convertToVector = {
//                    AnimationVector2D(it.width.value,it.height.value)
//                }
//            ))
//        }
//        LaunchedEffect(big.value, block = {
//            customAnimation.animateTo(if(big.value) CustomSize(80.dp,80.dp) else CustomSize(200.dp,200.dp))
//        })
//
//        Column() {
//            Box(modifier = Modifier
//                .size(customAnimation.value.width, customAnimation.value.height)
//                .clickable {
//                    big.value = !big.value
//                }
//                .background(Color.Red))
//        }
//    }
//
//    @SuppressLint("UnrememberedAnimatable")
//    @Preview
//    @Composable
//
//    fun animatableTest2(){
//        val big = remember {
//            mutableStateOf(true)
//        }
//        Column(){
//            val anim = remember() {
//                Animatable(100.dp,Dp.VectorConverter)
//            }
//            LaunchedEffect(big.value, block = {
//                anim.snapTo(if(big.value) 250.dp else 0.dp)
//                anim.animateTo(if(big.value) 200.dp else 100.dp)
//            })
//            Box(modifier = Modifier
//                .size(anim.value)
//                .clickable {
//                    big.value = !big.value
//                }
//                .background(Color.Red))
//        }
//    }
//
//
//
//    @SuppressLint("UnrememberedAnimatable")
//    @Preview
//    @Composable
//    fun animatableTest(){
//        val big = remember {
//            mutableStateOf(true)
//        }
//        Column(){
//            val anim = remember() {
//                Animatable(100.dp,Dp.VectorConverter)
//            }
//            LaunchedEffect(big.value, block = {
//                anim.animateTo(if(big.value) 200.dp else 100.dp)
//            })
//            Box(modifier = Modifier
//                .size(anim.value)
//                .clickable {
//                    big.value = !big.value
//                }
//                .background(Color.Red))
//        }
//    }
//
//
//    @Preview
//    @Composable
//    fun rememberInfiniteTransitionTest(){
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//            val infiniteTransition = rememberInfiniteTransition()
//            val color by infiniteTransition.animateColor(
//                initialValue = Color.Red,
//                targetValue = Color.Green,
//                animationSpec = infiniteRepeatable(
//                    animation = tween(1000, easing = LinearEasing),
//                    repeatMode = RepeatMode.Reverse
//                )
//            )
//            val alpha by infiniteTransition.animateFloat(
//                initialValue = 1f,
//                targetValue = 0.5f,
//                animationSpec = infiniteRepeatable(
//                    animation = tween(1000, easing = LinearEasing),
//                    repeatMode = RepeatMode.Reverse
//                )
//            )
//
//            Box(
//                Modifier
//                    .fillMaxSize()
//                    .background(color)
//                    .alpha(alpha))
//        }
//    }
//
//
//
//    // 首先定义Box的两种状态，Small小，Big是大
//    enum class BoxState{
//        Small,
//        Big
//    }
//
//    // 定义一个对象TransitionData。里面存放着State<Color>,跟State<Dp>
//    class TransitionData(color:State<Color>,size:State<Dp>){
//        val colorValue = color
//        val sizeValue = size
//    }
//
//    // 封装做动画的box，点击的时候会改变boxState的状态
//    @Composable
//    fun AnimatingBox(boxState: MutableState<BoxState>){
//        val transitionData = updateTransitionData(boxState.value)
//        Box(modifier = Modifier
//            .background(transitionData.colorValue.value)
//            .size(transitionData.sizeValue.value)
//            .clickable {
//                when (boxState.value) {
//                    BoxState.Big -> boxState.value = BoxState.Small
//                    else -> boxState.value = BoxState.Big
//                }
//            })
//    }
//
//    // 根据boxState。Box的状态去封装动画
//    @Composable
//    fun updateTransitionData(boxState: BoxState):TransitionData{
//        val transition = updateTransition(boxState, label = "")
//        val color = transition.animateColor{
//            when(it){
//                BoxState.Big->Color.Red
//                BoxState.Small->Color.Yellow
//            }
//        }
//        val size = transition.animateDp{
//            when(it){
//                BoxState.Big->120.dp
//                BoxState.Small->40.dp
//            }
//        }
//        return remember {
//            TransitionData(color,size)
//        }
//    }
//
//    // 例子使用
//    @Preview
//    @Composable
//    fun updateTransitionTest(){
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxSize()
//        ) {
//            val boxState = remember {
//                mutableStateOf(BoxState.Small)
//            }
//            AnimatingBox(boxState)
//        }
//    }
//
//
//
//    data class CustomSize(val width:Dp,val height:Dp)
//
//    @Preview
//    @Composable
//    fun customTwoWayConvert(){
//        val big = remember() {
//            mutableStateOf(true)
//        }
//        val sizeState = animateValueAsState<CustomSize,AnimationVector2D>(
//            targetValue = if(big.value) CustomSize(200.dp,200.dp) else CustomSize(80.dp,80.dp),
//            TwoWayConverter(
//                convertFromVector = {
//                    CustomSize(it.v1.dp,it.v2.dp)
//                },
//                convertToVector = {
//                    AnimationVector2D(it.width.value,it.height.value)
//                }
//            )
//        )
//        Column() {
//            Box(modifier = Modifier
//                .size(sizeState.value.width, sizeState.value.height)
//                .clickable {
//                    big.value = !big.value
//                }
//                .background(Color.Red))
//        }
//    }
//
//
//
//    @Preview
//    @Composable
//    fun animateXXXStateTest(){
//
//        val redChange = remember {
//            mutableStateOf(true)
//        }
//
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//            Button(onClick = {
//                redChange.value = !redChange.value
//            },modifier = Modifier.padding(vertical = 10.dp)) {
//                Text(text =  if(redChange.value) "变成黄色" else "变成红色")
//            }
//
//
//            val colorState = animateColorAsState(
//                targetValue = if(redChange.value) Color.Red else Color.Yellow,
//                animationSpec = tween(durationMillis = 2000),
//                finishedListener={
//                    // 动画完成的监听
//                })
//
//            Box(modifier = Modifier
//                .background(colorState.value)
//                .size(100.dp))
//        }
//    }
//
//
//
//    enum class ContentStyle{
//        IMAGE,
//        TEXT
//    }
//
//    @Preview
//    @Composable
//    fun crossfadeTest(){
//        val contentStyle = remember {
//            mutableStateOf(ContentStyle.TEXT)
//        }
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//            Button(onClick = {
//                contentStyle.value = if(contentStyle.value==ContentStyle.TEXT) ContentStyle.IMAGE else ContentStyle.TEXT
//            },modifier = Modifier.padding(vertical = 10.dp)) {
//                Text(text =  if(contentStyle.value==ContentStyle.TEXT) "变成图片" else "变成文本")
//            }
//
//            Crossfade(
//                targetState = contentStyle,
//                animationSpec = tween(durationMillis = 1000,easing = LinearEasing)
//            ) {
//                when(it.value){
//                    // 文本
//                    ContentStyle.TEXT->{
//                        Text(text =  "我是海绵宝宝的文本")
//                    }
//                    // 图片
//                    ContentStyle.IMAGE->{
//                        Image(bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_launcher), contentDescription = "海绵宝宝的图片")
//                    }
//                }
//            }
//
//        }
//    }
//
//
//
//    @ExperimentalAnimationApi
//    @Preview
//    @Composable
//    fun animatedVisibilityTest(){
//        val visible = remember {
//            mutableStateOf(true)
//        }
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//            Button(onClick = {
//                visible.value = !visible.value
//            },modifier = Modifier.padding(vertical = 10.dp)) {
//                Text(text = if(visible.value) "hide" else "show")
//            }
//
//            AnimatedVisibility(
//                visible = visible.value,
//                enter = fadeIn(),
//                exit = fadeOut()) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    Icon(imageVector = Icons.Filled.Login, contentDescription = "stringResource(id = R.string.login)")
//                    Text(modifier = Modifier.padding(start = 5.dp),text = "stringResource(id = R.string.login)")
//                }
//            }
//        }
//    }
//
//
//    @Preview
//    @Composable
//    fun animateContentSizeTest(){
//
//        val expand = remember {
//            mutableStateOf(true)
//        }
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//            Button(onClick = {
//                expand.value = !expand.value
//            },modifier = Modifier.padding(vertical = 10.dp)) {
//                Text(text =  if(expand.value) "收起" else "展开")
//            }
//
//            Box(
//                modifier = Modifier
//                    .background(Color.Blue)
//                    .padding(horizontal = 5.dp)
//                    .animateContentSize(
//                        animationSpec = tween(),
//                        finishedListener = { initialValue, targetValue ->
//
//                        })
//            ) {
//                Text(text = "探清水河，桃叶儿尖上尖，柳叶儿遮满了天，在其位这位名阿公，细听我来言呐。此事啊，" +
//                        "发生在京西南电厂啊，南电厂火器营有一位宋老三啊，提起那宋老三，两口子落平川，" +
//                        "一生啊无有儿，所生女婵娟啊，小女啊年长啊一十六啊，取了个乳名，名字叫大连啊，姑娘叫大连，" +
//                        "俊俏好容颜",
//                    color = Color.White,
//                    maxLines = if(expand.value) 100 else 2,lineHeight = 20.sp,fontSize = 15.sp,overflow = TextOverflow.Ellipsis)
//            }
//
//        }
//    }
//
//
//
//
//    @Preview
//    @Composable
//    fun Layout() {
//
//        val offsetY  =  remember { mutableStateOf(0f) }
//
//        val state = remember {
//            mutableStateOf(1)
//        }
//
//        if (state.value == 0) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(text = "loading...")
//            }
//        } else {
//            Box(Modifier.fillMaxSize()) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .verticalScroll(rememberScrollState())
//                ) {
//                    Column(
//                        Modifier
//                            .fillMaxSize()
//                    ) {
//                        Head()
//                        Box(Modifier.padding(top = 80.dp)) {
//                            ListColumn()
//                        }
//                        ImageAvator()
//                    }
//                    Box(Modifier.padding(top = 180.dp)) {
//                        Coach()
//                    }
//                }
//
//                TitleBar()
//
//                Box(
//                    Modifier
//                        .padding(bottom = 40.dp)
//                        .fillMaxSize(), contentAlignment = Alignment.BottomCenter
//                ) {
//                    Counter()
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun laxz(){
//
//    }
//
//    @Composable
//    fun ImageAvator(){
//        CoilImage(
//            data = url,
//            contentDescription = "My content description",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(80.dp)
//                .shadow(2.dp, shape = CircleShape)
//                .border(shape = CircleShape, width = 10.dp, color = Color.White)
//                .clip(shape = CircleShape),
//            fadeIn = true
//        )
//    }
//
//
//    @Preview
//    @Composable
//    fun TitleBar() {
//        Row(
//            Modifier
//                .systemBarsPadding()
//                .fillMaxWidth()
//                .height(0.dp)
//                .background(Color.White)
//                .shadow(2.dp)
//            , verticalAlignment = Alignment.CenterVertically
//        ) {
//            Image(
//                painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.svg_back_black)), contentDescription = "back",
//                modifier = Modifier
//                    .clickable(onClick = {
//                        finish()
//                    })
//                    .padding(start = 16.dp)
//            )
//            Text("AAAAA", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier
//                .weight(1f)
//                .padding(horizontal = 48.dp)
//                .wrapContentWidth(Alignment.CenterHorizontally)
//            )
//        }
//    }
//
//    @Composable
//    fun Head() {
//        Box(
//            Modifier
//                .fillMaxWidth()
//                .height(210.dp)
//                .background(
//                    brush = Brush.verticalGradient(
//                        mutableListOf(Color(0xFF1970FF), Color(0xFF43CCFF)),
//                    )
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Row(modifier = Modifier.padding(25.dp)) {
//                Column(modifier = Modifier.weight(1f)) {
//                    Row(verticalAlignment = Alignment.Bottom) {
//                        Text(text = "秀秀要睡觉", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.White)
//                        Box(modifier = Modifier.padding(start = 5.dp))
//                        Text(text = "上海-小车", fontSize = 13.sp, color = Color.White)
//                    }
//                    Row(
//                        modifier = Modifier.padding(top = 8.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(text = "136****9028", style = TextStyle(Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold))
//                        Box(
//                            modifier = Modifier
//                                .padding(start = 15.dp)
//                                .background(Color(0x33FFFFFF), shape = RoundedCornerShape(CornerSize(5.dp)))
//                        ) {
//                            Text(text = "切换", color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
//                        }
//                    }
//                }
//                Text(text = "AAA")
//            }
//        }
//    }
//
//    @Composable
//    fun Coach() {
//        Column(
//            modifier = Modifier
//                .padding(horizontal = 25.dp)
//                .shadow(2.dp, shape = RoundedCornerShape(3.dp))
//                .background(Color.White)
//        ) {
//            Box(Modifier.padding(horizontal = 20.dp)) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                ) {
//                    Text(text = "我的教练", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
//                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Text(text = "张子睿", color = Color.Gray, fontSize = 14.sp)
//                            Image(painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.svg_right_gray)), contentDescription = "right")
//                        }
//                    }
//                }
//            }
//            Divider(
//                color = Color(0xFFE6E6E6), modifier = Modifier
//                    .fillMaxWidth()
//                    .height(0.5.dp)
//                    .padding(horizontal = 20.dp)
//            )
//            Box(Modifier.padding(horizontal = 20.dp)) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                ) {
//                    Box(modifier = Modifier.fillMaxWidth()) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Image(painter = painterResource(id = R.mipmap.ic_launcher), contentDescription = "right", modifier = Modifier.size(45.dp))
//                            Text(
//                                text = "张子睿".repeat(10), color = Color.Gray, fontSize = 14.sp,
//                                overflow = TextOverflow.Ellipsis, modifier = Modifier
//                                    .weight(1f)
//                                    .padding(horizontal = 2.dp),
//                                maxLines = 1
//                            )
//                            Button(
//                                onClick = {},
//                                colors = ButtonDefaults.buttonColors(
//                                    backgroundColor = Color(0xFF0093F0)
//                                ),
//                                modifier = Modifier.clip(shape = RoundedCornerShape(3.dp))
//                            ) {
//                                Text(text = "预约练车", color = Color.White, fontSize = 14.sp)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun ListColumn() {
//        Column(Modifier.padding(horizontal = 25.dp)) {
//            for (i in 0..13) {
//                ListItem("第${i}个item列表")
//                Divider(
//                    color = Color(0xFFE6E6E6), modifier = Modifier
//                        .fillMaxWidth()
//                        .height(0.5.dp)
//                )
//            }
//        }
//    }
//
//    @Composable
//    fun ListItem(title: String) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//        ) {
//            Image(painter = painterResource(id = R.mipmap.ic_launcher), contentDescription = "right", modifier = Modifier.size(25.dp))
//            Text(
//                text = title, color = Color.Black, fontSize = 16.sp,
//                overflow = TextOverflow.Ellipsis, modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 2.dp),
//                maxLines = 1
//            )
//            Image(painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.svg_right_gray)), contentDescription = "right")
//        }
//    }
//
//
//    @Composable
//    fun Counter(countModel: CountModel = viewModel()) {
//        Button(
//            onClick = { countModel.add() }, modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//        ) {
//            Text(text = "点击${countModel.count.value}")
//        }
//    }
//
//}
//
//
//@Composable
//fun MyBasicColumn(
//    modifier: Modifier = Modifier,
//    content: @Composable() () -> Unit
//) {
//    Layout(
//        modifier = modifier,
//        content = content
//    ) { measurables, constraints ->
//        val placeables = measurables.map { measurable ->
//            measurable.measure(constraints)
//        }
//
//        // Set the size of the layout as big as it can
//        layout(constraints.maxWidth, constraints.maxHeight) {
//            // Track the y co-ord we have placed children up to
//            var yPosition = 0
//
//            // Place children in the parent layout
//            placeables.forEach { placeable ->
//                // Position item on the screen
//                placeable.placeRelative(x = 0, y = yPosition)
//
//                // Record the y co-ord placed up to
//                yPosition += placeable.height
//            }
//        }
//    }
//}

//
//fun Modifier.firstBaselineToTop(
//    firstBaselineToTop: Dp
//) = layout { measurable, constraints ->
//    // Measure the composable
//    val placeable = measurable.measure(constraints)
//
//    // Check the composable has a first baseline
//    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
//    val firstBaseline = placeable[FirstBaseline]
//
//    // Height of the composable with padding - first baseline
//    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
//    val height = placeable.height + placeableY
//    layout(placeable.width, height) {
//        // Where the composable gets placed
//        placeable.placeRelative(0, placeableY)
//    }
//}