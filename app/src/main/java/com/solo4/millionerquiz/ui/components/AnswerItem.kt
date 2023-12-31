package com.solo4.millionerquiz.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.solo4.millionerquiz.model.game.Answer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.solo4.millionerquiz.ui.theme.GreenRightAnswer
import com.solo4.millionerquiz.ui.theme.GreenPickAnswer
import com.solo4.millionerquiz.ui.theme.RedBadAnswer
import com.solo4.millionerquiz.ui.theme.YellowRightAnswer

@Composable
fun AnswerItem(
    modifier: Modifier = Modifier,
    answer: Answer,
    isPicked: Boolean,
    isShowResult: Boolean = false,
    onClick: (Answer) -> Unit
) {
    /*val bgColor by animateColorAsState(
        targetValue = if (isShowResult)
            if (answer.isRight) GreenRightAnswer else
                if (isPicked) RedBadAnswer else MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.secondary,
        animationSpec = tween(durationMillis = 300)
    )*/

    val bgColor by animateColorAsState(
        targetValue = if (isShowResult) if (answer.isRight && isPicked) GreenRightAnswer else if (!answer.isRight && isPicked) RedBadAnswer else if (answer.isRight && !isPicked) GreenRightAnswer else MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = modifier
            .background(
                bgColor,
                RoundedCornerShape(10.dp)
            )
            .clickable(interactionSource = MutableInteractionSource(), null) {
                onClick.invoke(answer)
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val width by animateDpAsState(
                targetValue = if (!isPicked) 0.dp else maxWidth,
                animationSpec = tween(durationMillis = 800)
            )

            var alpha: Int by remember { mutableStateOf(0) }

            if (!isShowResult) {
                Canvas(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxHeight()
                        .alpha(alpha.toFloat())
                        .width(width)
                        .onSizeChanged { alpha = if (it.width <= 50) 0 else 1 },
                    onDraw = { drawRect(YellowRightAnswer) }
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = answer.text,
                style = TextStyle(fontSize = 16.sp, color = Color.Black)
            )
        }
    }
}
