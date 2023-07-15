package com.solo4.millionerquiz.ui.screens.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.solo4.millionerquiz.MainActivity
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.MediaManager
import com.solo4.millionerquiz.model.game.Answer
import com.solo4.millionerquiz.model.game.GameScreenState
import com.solo4.millionerquiz.ui.components.AnswerItem
import com.solo4.millionerquiz.ui.components.StarBlock
import com.solo4.millionerquiz.ui.theme.QuizGameTheme
import com.solo4.millionerquiz.ui.theme.contentPadding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(navHostController: NavHostController = rememberNavController()) {
    val viewModel: GameViewModel = koinViewModel()
    var pickedAnswer by remember { mutableStateOf<Answer?>(null) }

    var checkResults by remember { mutableStateOf(false) }

    val currentState by viewModel.currentQuestion.collectAsState()

    var activity: MainActivity? = LocalContext.current as? MainActivity

    fun invalidateAnswers() {
        pickedAnswer = null
        checkResults = false
    }

    LaunchedEffect(key1 = null, block = {
        viewModel.initialize()
        launch {
            viewModel.backFromGameScreenEvent.collectLatest {
                navHostController.popBackStack()
            }
        }
    })
    DisposableEffect(key1 = "", effect = {
        onDispose { activity = null }
    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            if (currentState.imageUrl.isNotBlank()) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .size(200.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    model = currentState.imageUrl,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.question_placeholder),
                    error = painterResource(id = R.drawable.question_placeholder),
                    contentDescription = stringResource(R.string.question_image)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.question).plus(" ${currentState.id}"),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = currentState.questionText,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(40.dp))

            currentState.answers.forEach { answer ->
                AnswerItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    answer = answer,
                    isPicked = pickedAnswer?.id == answer.id,
                    isShowResult = checkResults,
                    onClick = {
                        MediaManager.playClick()
                        if (!currentState.isAnswered) pickedAnswer = answer
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        MediaManager.playClick()
                        if (pickedAnswer == null) return@clickable // no one answer is not picked yet
                        if (viewModel.currentQuestion.value.isAnswered) {
                            pickedAnswer = null
                            invalidateAnswers()
                            viewModel.nextQuestion()
                        } else {
                            checkResults = true
                            viewModel.markCurrentQuestionAsAnswered(pickedAnswer)
                        }
                    }),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 16.sp, color = Color.Black),
                    text = if (currentState.isAnswered)
                        stringResource(R.string.next) else stringResource(R.string.answer)
                )
            }
        }
        Image(
            modifier = Modifier
                .padding(40.dp)
                .align(Alignment.TopEnd)
                .background(Color.White)
                .border(BorderStroke(1.dp, Color.Black))
                .clickable {
                    MediaManager.playClick()
                    navHostController.popBackStack()
                },
            painter = painterResource(id = R.drawable.ic_arrow_bask),
            contentDescription = "Back"
        )
        if (viewModel.screenState.value is GameScreenState.EndGame) {
            Box(
                modifier = Modifier
                    .padding(40.dp)
                    .fillMaxSize()
                    .background(Color.Gray, RoundedCornerShape(size = 20.dp))
                    .padding(2.dp)
                    .background(
                        MaterialTheme.colorScheme.onSecondary,
                        RoundedCornerShape(size = 20.dp)
                    )
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.level_completed),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(60.dp))
                    Text(
                        text = viewModel.scoreForEndGameUI.value.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    StarBlock(
                        filledStarsCount = viewModel.getNumberOfEndGameStars(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(100.dp))
                    Text(
                        text = stringResource(R.string.wanna_play_next),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    Button(onClick = {
                        MediaManager.playClick()
                        viewModel.backWithAdvert(activity)
                    }) {
                        Text(text = stringResource(R.string.continue_game))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizGameTheme {
        GameScreen()
    }
}
