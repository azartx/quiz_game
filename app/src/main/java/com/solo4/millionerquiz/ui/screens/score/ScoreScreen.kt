package com.solo4.millionerquiz.ui.screens.score

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.MediaManager
import com.solo4.millionerquiz.ui.theme.contentPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScoreScreen(navHostController: NavHostController) {
    val viewModel: ScoreViewModel = koinViewModel()
    val scores by viewModel.userScoresList.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    modifier = Modifier.clickable {
                        MediaManager.playClick()
                        navHostController.popBackStack()
                    },
                    painter = painterResource(id = R.drawable.ic_arrow_bask),
                    contentDescription = "Back"
                )
            }
            Text(
                text = stringResource(R.string.liderboard),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(38.dp))

            LazyColumn(content = {
                itemsIndexed(scores) { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .background(
                                if (viewModel.currentUser.id == item.userId)
                                    if (index > 2) Color.Red else Color(0xFF2E7D32) else
                                        Color.White.copy(0f)
                            )
                    ) {
                        Text(text = "${index + 1}. ")
                        Text(modifier = Modifier.weight(1f), text = "${item.username} ")
                        Text(
                            modifier = Modifier.weight(1f),
                            text = item.score.toString(),
                            textAlign = TextAlign.End
                        )
                    }
                }
            })
        }
    }
}
