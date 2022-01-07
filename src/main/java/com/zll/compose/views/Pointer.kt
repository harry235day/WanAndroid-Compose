package com.zll.compose.views

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlin.math.roundToInt


@ExperimentalMaterialApi
@Composable
fun PointerView() {
    Column(Modifier.fillMaxWidth()) {
        PointerInputView()
        ScrollBoxes()
        ScrollBoxesSmooth()
        ScrollableSample()
        MoreColumnScrollView()
        DraggableView()
        DraggableView2()
        SwipeableSample()
        TransformableSample()
    }
}

@Composable
fun PointerInputView() {
    val state = remember {
        mutableStateOf("点这里")
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Gray)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { state.value = "onPress" },
                    onDoubleTap = { state.value = "onDoubleTap" },
                    onLongPress = { state.value = "onLongPress" },
                    onTap = { state.value = "onTap" }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = state.value)
    }

}

@Composable
fun ScrollBoxes() {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .size(80.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(10) {
            Text("Item $it", modifier = Modifier.padding(2.dp))
        }
    }
}

@Composable
private fun ScrollBoxesSmooth() {
    // Smoothly scroll 100px on first composition
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .size(100.dp)
            .padding(horizontal = 8.dp)
            .verticalScroll(state)
    ) {
        repeat(10) {
            Text("Item $it", modifier = Modifier.padding(2.dp))
        }
    }
}

@Composable
fun ScrollableSample() {
    // actual composable state
    val offset = remember { mutableStateOf(0f) }
    Box(
        Modifier
            .size(50.dp)
            .scrollable(
                orientation = Orientation.Vertical,
                // Scrollable state: describes how to consume
                // scrolling delta and update offset
                state = rememberScrollableState { delta ->
                    offset.value += delta
                    delta
                }
            )
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(offset.value.toString())
    }
}


@Composable
fun MoreColumnScrollView() {
    val gradient = Brush.horizontalGradient(0f to Color.Gray, 10f to Color.White)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.LightGray)
            .horizontalScroll(rememberScrollState())

    ) {
        Row() {
            repeat(6) {
                Box(
                    modifier = Modifier
                        .width(108.dp)
                        .horizontalScroll(rememberScrollState())
                        .fillMaxHeight()
                ) {
                    Text(
                        "Scroll here",
                        modifier = Modifier
                            .border(12.dp, Color.DarkGray)
                            .background(brush = gradient)
                            .width(150.dp)
                            .padding(20.dp)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Composable
fun DraggableView() {
    val offset = remember {
        mutableStateOf(0f)
    }
    Button(
        onClick = {
            offset.value = 0f
        },
        modifier = Modifier
            .offset {
                IntOffset(offset.value.roundToInt(), 0)
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState(onDelta = {
                    offset.value += it
                })
            ),

        ) {
        Text(text = "Drag me!")
    }
}

@Composable
fun DraggableView2() {
    val offsetX = remember {
        mutableStateOf(0f)
    }
    val offsetY = remember {
        mutableStateOf(0f)
    }
    Box(
        Modifier
            .background(Color.Cyan)
            .fillMaxWidth()
            .height(100.dp)
    ) {

        Box(Modifier
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .size(50.dp)
            .background(Color.Blue)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consumeAllChanges()
                        offsetX.value += dragAmount.x
                        offsetY.value += dragAmount.y
                    }
                )
            }
        ) {

        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SwipeableSample() {
    val width = 96.dp
    val squareSize = 48.dp

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states

    Box(
        modifier = Modifier
            .width(width)
            .height(IntrinsicSize.Min)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
    ) {
        Row(Modifier.width(width)) {
            Box(
                Modifier
                    .width(squareSize)
                    .fillMaxHeight()
                    .background(Color.Blue)
            )
            Box(
                Modifier
                    .width(squareSize)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            )
        }
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(squareSize)
                .background(Color.DarkGray)
        )
    }
}

@Composable
fun TransformableSample() {
    // set up all transformation states
    val scale =  remember { mutableStateOf(1f) }
    val rotation = remember { mutableStateOf(0f) }
    val offset = remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale.value *= zoomChange
        rotation.value += rotationChange
        offset.value += offsetChange
    }
    Box(
        Modifier
            // apply other transformations like rotation and zoom
            // on the pizza slice emoji
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                rotationZ = rotation.value,
                translationX = offset.value.x,
                translationY = offset.value.y
            )
            // add transformable to listen to multitouch transformation events
            // after offset
            .transformable(state = state)
            .background(Color.Blue)
            .fillMaxSize()
    )
}