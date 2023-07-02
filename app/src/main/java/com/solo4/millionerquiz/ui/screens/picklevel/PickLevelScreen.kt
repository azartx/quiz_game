package com.solo4.millionerquiz.ui.screens.picklevel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.solo4.millionerquiz.ui.components.LevelItem
import com.solo4.millionerquiz.ui.theme.QuizGameTheme
import com.solo4.millionerquiz.ui.theme.contentPadding

@Composable
fun PickLevelScreen(currentLevel: Int = 0) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(contentPadding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(20.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = rememberLazyListState(currentLevel, currentLevel),
            reverseLayout = true
        ) {
            items(20) {
                LevelItem(
                    isLeft = it % 2 != 0,
                    hideLine = it == 19,
                    text = it.toString(),
                    onClick = {
                        //todo navigate to the specific game level
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizGameTheme {
        PickLevelScreen()
    }
}
