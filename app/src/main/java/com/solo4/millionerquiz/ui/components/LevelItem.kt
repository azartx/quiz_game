package com.solo4.millionerquiz.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.solo4.millionerquiz.R

const val LEVEL_IMG_WIDTH = 100f
const val LEVEL_IMG_PADDING = 50f

@Composable
fun LevelItem(
    text: String,
    isLeft: Boolean,
    hideLine: Boolean = false,
    isClickEnabled: Boolean = false,
    onClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().alpha(if (isClickEnabled) 1f else 0.5f)) {
        if (!hideLine) {
            Canvas(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                val width = size.width
                val height = size.height

                val path = Path().apply {
                    if (isLeft) {
                        moveTo(
                            width - LEVEL_IMG_PADDING.dp.toPx() - (LEVEL_IMG_WIDTH / 2).dp.toPx(),
                            0f
                        )
                        quadraticBezierTo(
                            width - LEVEL_IMG_PADDING,
                            height / 2,
                            LEVEL_IMG_PADDING.dp.toPx() + (LEVEL_IMG_WIDTH / 2).dp.toPx(),
                            height
                        )
                    } else {
                        moveTo(LEVEL_IMG_PADDING.dp.toPx() + (LEVEL_IMG_WIDTH / 2).dp.toPx(), 0f)
                        quadraticBezierTo(
                            LEVEL_IMG_PADDING,
                            height / 2,
                            width - LEVEL_IMG_PADDING.dp.toPx() - (LEVEL_IMG_WIDTH / 2).dp.toPx(),
                            height
                        )
                    }
                }
                drawPath(
                    path = path,
                    color = androidx.compose.ui.graphics.Color.Black,
                    style = Stroke(5f)
                )
            }
        }

        Box(modifier = Modifier
            .let {
                if (isLeft) it.padding(start = LEVEL_IMG_PADDING.dp) else
                    it.padding(end = LEVEL_IMG_PADDING.dp)
            }
            .align(if (isLeft) Alignment.Start else Alignment.End)
            .clickable(enabled = isClickEnabled, onClick = { onClick.invoke(text) })
        ) {
            Image(
                modifier = Modifier
                    .width(LEVEL_IMG_WIDTH.dp)
                    .clip(RoundedCornerShape(180.dp)),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_oval_level_number),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Text(modifier = Modifier.align(Alignment.Center), text = text)
        }
    }
}
