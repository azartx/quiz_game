package com.solo4.millionerquiz.ui.screens.picklevel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.MediaManager
import com.solo4.millionerquiz.ui.components.LevelItem
import com.solo4.millionerquiz.ui.navigation.Routes
import com.solo4.millionerquiz.ui.theme.bgBrush
import com.solo4.millionerquiz.ui.theme.contentPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun PickLevelScreen(navHostController: NavHostController) {
    val viewModel: PickLevelViewModel = koinViewModel()
    val lastCompletedLevel by viewModel.lastCompletedLevel.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        if (viewModel.levelsCount.value == 0) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgBrush, RoundedCornerShape(20.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = rememberLazyListState(lastCompletedLevel, lastCompletedLevel),
                reverseLayout = true
            ) {
                items(viewModel.levelsCount.value) { level ->
                    LevelItem(
                        isLeft = level % 2 != 0,
                        hideLine = level == (viewModel.levelsCount.value - 1),
                        text = (level + 1).toString(),
                        isClickEnabled = viewModel.isLevelClickable(level + 1),
                        onClick = { levelStr ->
                            MediaManager.playClick()
                            navHostController.navigate(Routes.GameRoute.nameWithArgs(levelStr))
                        }
                    )
                }
            }
            Image(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        MediaManager.playClick()
                        navHostController.popBackStack()
                    },
                painter = painterResource(id = R.drawable.ic_arrow_bask),
                contentDescription = "Back"
            )
        }
    }
}
