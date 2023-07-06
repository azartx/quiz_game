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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.model.game.Answer
import com.solo4.millionerquiz.ui.components.AnswerItem
import com.solo4.millionerquiz.ui.theme.QuizGameTheme
import com.solo4.millionerquiz.ui.theme.contentPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(navHostController: NavHostController = rememberNavController()) {
    val viewModel: GameViewModel = koinViewModel()
    var pickedAnswer by remember { mutableStateOf<Answer?>(null) }

    var checkResults by remember { mutableStateOf(false) }

    fun newQuestionPicked(answer: Answer) {
        if (!viewModel.currentQuestion.value.isAnswered) pickedAnswer = answer
    }

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
                .padding(20.dp),
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
                text = "Question ${viewModel.currentQuestion.value.id}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = viewModel.currentQuestion.value.questionText,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(40.dp))

            LazyColumn(modifier = Modifier
                .fillMaxWidth(), content = {
                items(viewModel.currentQuestion.value.answers) {
                    AnswerItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        answer = it,
                        isPicked = pickedAnswer?.id == it.id,
                        isShowResult = checkResults,
                        onClick = ::newQuestionPicked
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            })

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = {
                        if (viewModel.currentQuestion.value.isAnswered) {
                            invalidateAnswers()
                            viewModel.nextQuestion()
                        } else {
                            checkResults = true
                            viewModel.currentQuestion.value.isAnswered = true
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizGameTheme {
        GameScreen()
    }
}
