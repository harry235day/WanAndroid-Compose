package com.zll.compose.views

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zll.compose.R
import com.zll.compose.ext.swipeToDismiss
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@Composable
fun MyBaseColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val columnHeights = Array(1) { 0 }
    columnHeights.maxOrNull()?.coerceAtMost(0)
    Layout(content = content, modifier = modifier, measurePolicy = { measurables, constraints ->
        val places = measurables.map {
            it.measure(constraints)
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            var y = 0
            places.forEach {
                it.placeRelative(0, y)
                y += it.height
            }
        }
    })
}

@Composable
fun VerticalGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val itemWidth = constraints.maxWidth / columns
        // Keep given height constraints, but set an exact width
        val itemConstraints = constraints.copy(
            minWidth = itemWidth,
            maxWidth = itemWidth
        )
        // Measure each item with these constraints
        val placeables = measurables.map { it.measure(itemConstraints) }
        // Track each columns height so we can calculate the overall height
        val columnHeights = Array(columns) { 0 }
        placeables.forEachIndexed { index, placeable ->
            val column = index % columns
            columnHeights[column] += placeable.height
        }
        val height = (columnHeights.maxOrNull() ?: constraints.minHeight)
            .coerceAtMost(constraints.maxHeight)
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            // Track the Y co-ord per column we have placed up to
            val columnY = Array(columns) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val column = index % columns
                placeable.placeRelative(
                    x = column * itemWidth,
                    y = columnY[column]
                )
                columnY[column] += placeable.height
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ContactsList() {
    // "E", "F", "G", "H", "I", "J", "K", "M", "N", "O", "P", "Q", "R", "S", "T"
    val apps = arrayListOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "M")
    val map = HashMap<String, List<String>>()
    map["A"] = apps
    map["B"] = apps
    map["C"] = apps
    map["D"] = apps
    map["E"] = apps
    map["F"] = apps
    map["G"] = apps
    map["H"] = apps
    map["I"] = apps
    map["J"] = apps
    map["K"] = apps
    LazyColumn {
        for ((key, list) in map) {
            stickyHeader {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Gray)
                        .padding(horizontal = 5.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = key)
                }
            }
            items(list) { text ->
                Box(
                    Modifier
                        .background(Color.Cyan)
                        .height(30.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
                    Text(text)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun LazyVerticalGridView() {
    val apps = arrayListOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "M")
    LazyVerticalGrid(GridCells.Adaptive(100.dp)) {
        items(apps) { str ->
            Box(
                Modifier
                    .background(Color.Cyan)
            ) {
                Text(str, color = Color.Blue)
            }
        }
    }
}


@ExperimentalFoundationApi
@Composable
fun AnimatedVisibilityView() {
    Box(contentAlignment = Alignment.BottomEnd) {
        val listState = rememberLazyListState()
        LazyColumn(state = listState) {
            items(30) { index ->
                Column(
                    Modifier
                        .background(Color.Cyan)
                        .padding(vertical = 10.dp)
                ) {
                    Text(index.toString())
                    Divider(color = Color.Blue, modifier = Modifier.fillMaxWidth())
                }
            }
        }
        // Show the button if the first visible item is past
        // the first item. We use a remembered derived state to
        // minimize unnecessary compositions
        val showButton = remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }
        val scope = rememberCoroutineScope()

        var c = remember {
            mutableStateOf(0)
        }

        AnimatedVisibility(visible = showButton.value) {
            Button(onClick = {
                scope.launch {
                    listState.animateScrollToItem(0, 0)
                }
            }) {
                Text(text = "BTN${c.value}")
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex }
                //过滤重复的项
                .distinctUntilChanged()
                .collect {
                    c.value = it
                }
        }
    }
}

@Composable
fun CanvasView() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        drawLine(
            start = Offset(w, 0f),
            end = Offset(0f, h),
            color = Color.Blue,
            strokeWidth = 2f,
        )
        drawCircle(color = Color.Cyan, center = Offset(w / 2f, h / 2f), radius = w / 4f)
        rotate(45f, pivot = Offset(3 * w / 8f, 3 * w / 8f)) {
            drawRoundRect(
                color = Color.Green,
                cornerRadius = CornerRadius(10f, 10f),
                topLeft = Offset(w / 4f, w / 4f),
                size = Size(w / 4f, w / 4f)
            )
        }
        drawRoundRect(
            color = Color.Green,
            cornerRadius = CornerRadius(10f, 10f),
            topLeft = Offset(w / 4f, w / 4f),
            size = Size(w / 4f, w / 4f)
        )

        withTransform({
            rotate(45f, pivot = Offset(3 * w / 8f, (6 * h + w) / 8f))
        }) {
            drawRoundRect(
                color = Color.Green,
                cornerRadius = CornerRadius(10f, 10f),
                topLeft = Offset(w / 4f, 3 * h / 4f),
                size = Size(w / 4f, w / 4f)
            )
        }
        drawRoundRect(
            color = Color.Green,
            cornerRadius = CornerRadius(10f, 10f),
            topLeft = Offset(w / 4f, 3 * h / 4f),
            size = Size(w / 4f, w / 4f)
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun AnimationView() {
    var visible = remember {
        mutableStateOf(true)
    }
    val density = LocalDensity.current

    Column {
        AnimatedVisibility(visible = true,
            enter = slideInVertically {
                with(density) {
                    -40.dp.roundToPx()
                }
            } + expandVertically {
                with(density) {
                    140.dp.roundToPx()
                }
            } + fadeIn(
                initialAlpha = 0.4f
            ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            Text(
                text = "HELLO",

                )
        }

        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            // Fade in/out the background and the foreground.
            Box(Modifier.background(Color.DarkGray)) {
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .animateEnterExit(
                            // Slide in/out the inner box.
                            enter = slideInVertically(),
                            exit = slideOutVertically()
                        )
                        .sizeIn(minWidth = 256.dp, minHeight = 64.dp)
                        .background(Color.Red)
                ) {

                }
            }
        }

        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val background: Color by transition.animateColor { state ->
                if (state == EnterExitState.Visible) Color.Blue else Color.Gray
            }
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .background(background)
            )
        }


        Button(onClick = {
            visible.value = !visible.value
        }) {
            Text(text = "BTN")
        }

        Row {
            var count by remember { mutableStateOf(0) }
            Button(onClick = { count++ }) {
                Text("Add")
            }
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    // Compare the incoming number with the previous number.
                    if (targetState > initialState) {
                        // If the target number is larger, it slides up and fades in
                        // while the initial (smaller) number slides up and fades out.
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        // If the target number is smaller, it slides down and fades in
                        // while the initial number slides down and fades out.
                        slideInVertically { height -> -height } + fadeIn() with
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(
                        // Disable clipping since the faded slide-in/out should
                        // be displayed out of bounds.
                        SizeTransform(clip = false)
                    )
                }
            ) { targetCount ->
                Text(text = "Count: $targetCount")
            }
        }

        AnimationContentView()

        val alpha = animateFloatAsState(targetValue = 1f)
        Box(
            Modifier
                .size(40.dp, 40.dp)
                .graphicsLayer(alpha = alpha.value)
                .background(Color.Red)
        )

        val color = remember { Animatable(Color.Gray) }
        var ok = remember {
            mutableStateOf(true)
        }
        LaunchedEffect(ok) {
            repeat(10) {
                color.animateTo(if (ok.value) Color.Green else Color.Red)
                ok.value = !ok.value
                delay(100)
            }
        }
        Box(
            Modifier
                .size(50.dp, 50.dp)
                .background(color.value)
        )

        AnimationVisibleAndContentView()

        InfiniteTransitionView()
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Cyan)
                .swipeToDismiss {
                    Log.e("TAG", "Swipe Dismiss")
                }) {

            LazyRow(state = rememberLazyListState()) {
                items(10){
                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 10.dp)
                            .height(50.dp),
                        onClick = { /*TODO*/ }) {
                    }
                }
            }
        }
    }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun AnimationContentView() {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        color = MaterialTheme.colors.primary,
        onClick = { expanded = !expanded }
    ) {
        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn(animationSpec = tween(300, 150)) with
                        fadeOut(animationSpec = tween(150)) using
                        SizeTransform { initialSize, targetSize ->
                            if (targetState) {
                                keyframes {
                                    // Expand horizontally first.
                                    IntSize(targetSize.width, initialSize.height) at 150
                                    durationMillis = 300
                                }
                            } else {
                                keyframes {
                                    // Shrink vertically first.
                                    IntSize(initialSize.width, targetSize.height) at 150
                                    durationMillis = 300
                                }
                            }
                        }
            }
        ) { targetExpanded ->
            if (targetExpanded) {
                Text(
                    "EnterTransition 定义了目标内容应如何显示，ExitTransition 则定义了初始内容应如何消失。除了可用于 AnimatedVisibility 的所有 EnterTransition 和 ExitTransition 函数之外，AnimatedContent 还提供了 slideIntoContainer 和 slideOutOfContainer。这些是 slideInHorizontally/Vertically 和 slideOutHorizontally/Vertically 的便捷替代方案，它们可根据初始内容的大小和 AnimatedContent 内容的目标内容计算滑动距离。\n" +
                            "\n" +
                            "SizeTransform 定义了大小应如何在初始内容与目标内容之间添加动画效果。在创建动画时，您可以访问初始大小和目标大小。SizeTransform 还可控制在动画播放期间是否应将内容裁剪为组件大小。"
                )
            } else {
                Image(painter = painterResource(id = R.drawable.ic_launcher), contentDescription = "AAA")
            }
        }
    }
}

sealed class BoxState {
    object Expanded : BoxState()
    object Collapsed : BoxState()
}

//@Composable
//fun UpdateTransitionView(){
//    val cur = remember {
//        mutableStateOf(BoxState.Expanded)
//    }
//    val transition = updateTransition(targetState = cur, label = "")
//    val color by transition.animateColor(
//        transitionSpec = {
//            when {
//                BoxState.Expanded isTransitioningTo BoxState.Collapsed ->
//                    spring(stiffness = 50f)
//                else ->
//                    tween(durationMillis = 500)
//            }
//        }, label = ""
//    ) { state ->
//        when (state.value) {
//            BoxState.Collapsed -> MaterialTheme.colors.primary
//            BoxState.Expanded -> MaterialTheme.colors.background
//            else -> {MaterialTheme.colors.primary}
//        }
//    }
//
//}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun AnimationVisibleAndContentView() {
    var selected by remember { mutableStateOf(false) }
    // Animates changes when `selected` is changed.
    val transition = updateTransition(selected, label = "")

    val borderColor by transition.animateColor(label = "") { isSelected ->
        if (isSelected) Color.Magenta else Color.White
    }
    val elevation by transition.animateDp(label = "") { isSelected ->
        if (isSelected) 10.dp else 2.dp
    }
    Surface(
        onClick = { selected = !selected },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, borderColor),
        elevation = elevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Hello, world!")
            // AnimatedVisibility as a part of the transition.
            transition.AnimatedVisibility(
                visible = { targetSelected -> targetSelected },
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Text(text = "It is fine today.")
            }
            // AnimatedContent as a part of the transition.
            transition.AnimatedContent { targetState ->
                if (targetState) {
                    Text(text = "Selected")
                } else {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone")
                }
            }
        }
    }
}


@Composable
fun AnimatingBox(boxState: BoxState) {
    val transitionData = updateTransitionData(boxState)
    // UI tree
    Box(
        modifier = Modifier
            .background(transitionData.color)
            .size(transitionData.size)
    )
}

// Holds the animation values.
private class TransitionData(
    color: State<Color>,
    size: State<Dp>
) {
    val color by color
    val size by size
}

@Composable
private fun updateTransitionData(boxState: BoxState): TransitionData {
    val transition = updateTransition(boxState, label = "")
    val color = transition.animateColor(label = "") { state ->
        when (state) {
            BoxState.Collapsed -> Color.Gray
            BoxState.Expanded -> Color.Red
        }
    }
    val size = transition.animateDp(label = "") { state ->
        when (state) {
            BoxState.Collapsed -> 64.dp
            BoxState.Expanded -> 128.dp
        }
    }
    return remember(transition) { TransitionData(color, size) }
}

@Composable
fun InfiniteTransitionView() {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        Modifier
            .size(50.dp, 50.dp)
            .background(color)
            .clickable {

            }
    )
}

@Composable
fun TargetBasedAnimationView() {
    val anim = remember {
        TargetBasedAnimation(
            animationSpec = tween(200),
            typeConverter = Float.VectorConverter,
            initialValue = 200f,
            targetValue = 1000f
        )
    }
    var playTime by remember { mutableStateOf(0L) }

    LaunchedEffect(anim) {
        val startTime = withFrameNanos { it }
        do {
            playTime = withFrameNanos { it } - startTime
            val animationValue = anim.getValueFromNanos(playTime)
        } while (true)
    }

    val alpha: Float by animateFloatAsState(
        targetValue = 1f,
        // Configure the animation duration and easing.
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

}


