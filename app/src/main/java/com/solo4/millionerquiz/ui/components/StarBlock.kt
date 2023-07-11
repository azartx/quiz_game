package com.solo4.millionerquiz.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.solo4.millionerquiz.ui.theme.QuizGameTheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun StarBlock(filledStarsCount: Int, modifier: Modifier = Modifier) {
    val animationTargetState = remember { mutableStateOf(0f) }
    val animatedFloatState = animateFloatAsState(
        targetValue = animationTargetState.value,
        animationSpec = tween(durationMillis = 3000)
    )
    Row(modifier = modifier) {
        for (item in 1.. filledStarsCount) {
            Star(animatedFloatState.value)
        }
    }
    LaunchedEffect(key1 = "", block = {
        delay(300)
        animationTargetState.value = 1f
    })
}

@Composable
fun Star(alpha: Float) {
    Canvas(modifier = Modifier
        .size(80.dp)
        .rotate(-16f), onDraw = {
            drawPath(createStarPath(), Color.Red, alpha = alpha)
    })
}

private fun createStarPath(): Path {
    val path = Path()
    val outerRadius = 100f
    val innerRadius = outerRadius / 2f
    val numPoints = 5
    val angle = Math.PI / numPoints
    for (i in 0 until numPoints * 2) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val x = outerRadius + radius * cos(i * angle).toFloat()
        val y = outerRadius + radius * sin(i * angle).toFloat()
        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()
    return path
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizGameTheme {
        StarBlock(2)
    }
}
