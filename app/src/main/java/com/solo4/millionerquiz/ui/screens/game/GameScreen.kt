package com.solo4.millionerquiz.ui.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.model.game.Answer
import com.solo4.millionerquiz.model.game.GameScreenState
import com.solo4.millionerquiz.ui.components.AnswerItem
import com.solo4.millionerquiz.ui.components.StarBlock
import com.solo4.millionerquiz.ui.theme.QuizGameTheme
import com.solo4.millionerquiz.ui.theme.contentPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(navHostController: NavHostController = rememberNavController()) {
    val viewModel: GameViewModel = koinViewModel()
    var pickedAnswer by remember { mutableStateOf<Answer?>(null) }

    var checkResults by remember { mutableStateOf(false) }

    val currentState by viewModel.currentQuestion.collectAsState()

    fun invalidateAnswers() {
        pickedAnswer = null
        checkResults = false
    }

    LaunchedEffect(key1 = null, block = {
        viewModel.initialize()
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(20.dp))
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .defaultMinSize(minHeight = 200.dp)
                    .fillMaxWidth(200f)
                    .background(Color.LightGray, RoundedCornerShape(10.dp)),
                painter = painterResource(id = R.drawable.ic_placeholder),
                contentDescription = "Question image"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Question ${currentState.id}",
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
                        if (!currentState.isAnswered) pickedAnswer = answer
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        if (viewModel.currentQuestion.value.isAnswered) {
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
                        .background(Color.Cyan, RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center,
                    text = "Next"
                )
            }
        }
        if (viewModel.screenState.value is GameScreenState.EndGame) {
            Box(
                modifier = Modifier
                    .padding(40.dp)
                    .fillMaxSize()
                    .background(Color.Gray, RoundedCornerShape(size = 20.dp))
                    .padding(2.dp)
                    .background(Color.LightGray, RoundedCornerShape(size = 20.dp))
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Level completed!", style = TextStyle(fontSize = 22.sp))
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text = viewModel.scoreForEndGameUI.value.toString())
                    Spacer(modifier = Modifier.height(30.dp))
                    StarBlock(filledStarsCount = 2, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(100.dp))
                    Text(text = "Wanna play next?")
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(onClick = { navHostController.popBackStack() }) {
                        Text(text = "Continue")
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
