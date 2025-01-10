package com.example.zen_water_tablet.ui

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.zen_water_tablet.Line
import com.example.zen_water_tablet.data.BackgroundColor
import com.example.zen_water_tablet.data.PenColor
import com.example.zen_water_tablet.data.PenSize
import com.example.zen_water_tablet.data.Settings
import kotlinx.coroutines.launch

@Composable
fun DrawingFadingLinesCanvasScreen(drawSettings: Settings, modifier: Modifier = Modifier) {
    var lines = remember { mutableStateListOf<Line>() }
    val scope = rememberCoroutineScope()

    var radius = 20f

    when(drawSettings.size){
        PenSize.small -> radius = 10f
        PenSize.medium -> radius = 20f
        PenSize.large -> radius = 30f
    }

    var bgColor = Color.White
    when(drawSettings.backGroundColor){
        BackgroundColor.red -> bgColor = Color.Red
        BackgroundColor.blue -> bgColor = Color.Blue
        BackgroundColor.white -> bgColor = Color.White
        BackgroundColor.clear -> bgColor = Color.Transparent
        else -> bgColor = Color.Black
    }

    var color = Color.Red
    when(drawSettings.penColor){
        PenColor.red -> color = Color.Red
        PenColor.blue -> color = Color.Blue
        PenColor.white -> color = Color.White
        else -> color = Color.Black
    }

        if (drawSettings.backGroundImage != null) {
            bgColor = Color.Transparent
            Image(
                painter = painterResource(id = drawSettings.backGroundImage!!),
                contentDescription = "Pitch",
                modifier = Modifier.fillMaxSize()
            )
        }
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(color = bgColor)
            .pointerInput(Unit) {
                detectDragGestures() { change, dragAmount ->
                    change.consume()
                    val animatableAlpha = Animatable(1f)
                    val newLine =
                        Line(
                            start = change.position - dragAmount,
                            end = change.position,
                            alpha = animatableAlpha,
                            color = color
                        )
                    lines.add(newLine)

                    scope.launch {
                        if (drawSettings.autoErase) {
                            animatableAlpha.animateTo(
                                targetValue = if (drawSettings.autoErase) 0f else 1f,
                                animationSpec = tween(durationMillis = drawSettings.timeToErase.toInt() * 1000)
                            )
                        }
                        lines.filter { it != newLine }
                    }
                }
            }
        ) {

            lines.forEach { line ->
                drawLine(
                    color = line.color.copy(alpha = line.alpha?.value ?: 0.0f),
                    start = line.start,
                    end = line.end,
                    strokeWidth = radius,
                    cap = StrokeCap.Round,
                )

            }
        }
}