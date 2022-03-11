package com.zll.compose.test

import android.graphics.Typeface
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit


@Composable
fun WaveLoadingCompose(modifier: Modifier,
                       text: String,
                       textSize: TextUnit,
                       waveColor: Color,
                       downTextColor: Color = Color.White,
                       animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 500,
                                easing = CubicBezierEasing(0.2f, 0.2f, 0.7f, 0.9f)
                            ),
                            repeatMode = RepeatMode.Restart
                        )
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current.density
        val circleSizeDp = minOf(maxWidth, maxHeight)
        val circleSizePx = circleSizeDp.value * density
        val waveWidth = circleSizePx / 1.2f
        val waveHeight = circleSizePx / 10f
        val textPaint =  remember {
            mutableStateOf(Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                isDither = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = android.graphics.Paint.Align.CENTER
            })
        }
        val wavePath = remember {
            mutableStateOf(Path())
        }
        val circlePath = remember {
            mutableStateOf(Path().apply {
                addArc(
                    Rect(0f, 0f, circleSizePx, circleSizePx),
                    0f, 360f
                )
            })
        }
        val animateValue = rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = waveWidth,
            animationSpec = animationSpec,
        )
        androidx.compose.foundation.Canvas(modifier.requiredSize(size = circleSizeDp)) {
            drawTextToCenter(
                text = text,
                textPaint = textPaint.value,
                textSize = textSize.toPx(),
                textColor = waveColor.toArgb()
            )
            wavePath.value.reset()
            wavePath.value.moveTo(-waveWidth + animateValue.value, circleSizePx / 2.3f)
            var i = -waveWidth
            while (i < circleSizePx + waveWidth) {
                wavePath.value.relativeQuadraticBezierTo(waveWidth / 4f, -waveHeight, waveWidth / 2f, 0f)
                wavePath.value.relativeQuadraticBezierTo(waveWidth / 4f, waveHeight, waveWidth / 2f, 0f)
                i += waveWidth
            }
            wavePath.value.lineTo(circleSizePx, circleSizePx)
            wavePath.value.lineTo(0f, circleSizePx)
            wavePath.value.close()
            val resultPath = Path.combine(
                path1 = circlePath.value,
                path2 = wavePath.value,
                operation = PathOperation.Intersect
            )
            drawPath(path = resultPath, color = waveColor)
            clipPath(path = resultPath, clipOp = ClipOp.Intersect) {
                drawTextToCenter(
                    text = text,
                    textPaint = textPaint.value,
                    textSize = textSize.toPx(),
                    textColor = downTextColor.toArgb()
                )
            }
        }
    }
}

fun DrawScope.drawTextToCenter(
    text: String,
    textPaint: android.graphics.Paint,
    textSize: Float,
    textColor: Int
) {
    textPaint.textSize = textSize
    textPaint.color = textColor
    val fontMetrics = textPaint.fontMetrics
    val x = size.width / 2f
    val y = size.height / 2f - (fontMetrics.top + fontMetrics.bottom) / 2f
    drawContext.canvas.nativeCanvas.drawText(text, x, y, textPaint)
}
