package com.solo4.millionerquiz.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.solo4.millionerquiz.model.game.Answer
import com.solo4.millionerquiz.model.game.Question
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLevelScreen() {

    val viewModel: AddLevelViewModel = koinViewModel()

    var levelNumber by remember { mutableStateOf("") }

    var questionNumber by remember { mutableStateOf("1") }

    var questionId by remember { mutableStateOf("1") }
    var questionText by remember { mutableStateOf("") }

    var answer1Id by remember { mutableStateOf("1") }
    var answer2Id by remember { mutableStateOf("2") }
    var answer3Id by remember { mutableStateOf("3") }
    var answer4Id by remember { mutableStateOf("4") }

    var answer1Text by remember { mutableStateOf("") }
    var answer2Text by remember { mutableStateOf("") }
    var answer3Text by remember { mutableStateOf("") }
    var answer4Text by remember { mutableStateOf("") }

    var answer1IsRight by remember { mutableStateOf("false") }
    var answer2IsRight by remember { mutableStateOf("false") }
    var answer3IsRight by remember { mutableStateOf("false") }
    var answer4IsRight by remember { mutableStateOf("false") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "NewLevelNumber:")
        TextField(value = levelNumber, onValueChange = { levelNumber = it })

        Button(onClick = { viewModel.createLevel(levelNumber) }) {
            Text(text = "Create level")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Level number: $questionNumber")

        Text(text = "Question id and text")
        TextField(value = questionId, onValueChange = { questionId = it })
        TextField(value = questionText, onValueChange = { questionText = it })

        Spacer(modifier = Modifier.height(48.dp))

        Text(text = "Answer 1: id, text, isRight")
        TextField(value = answer1Id, onValueChange = { answer1Id = it })
        TextField(value = answer1Text, onValueChange = { answer1Text = it })
        TextField(value = answer1IsRight, onValueChange = { answer1IsRight = it })

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Answer 2: id, text, isRight")
        TextField(value = answer2Id, onValueChange = { answer2Id = it })
        TextField(value = answer2Text, onValueChange = { answer2Text = it })
        TextField(value = answer2IsRight, onValueChange = { answer2IsRight = it })

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Answer 3: id, text, isRight")
        TextField(value = answer3Id, onValueChange = { answer3Id = it })
        TextField(value = answer3Text, onValueChange = { answer3Text = it })
        TextField(value = answer3IsRight, onValueChange = { answer3IsRight = it })

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Answer 4: id, text, isRight")
        TextField(value = answer4Id, onValueChange = { answer4Id = it })
        TextField(value = answer4Text, onValueChange = { answer4Text = it })
        TextField(value = answer4IsRight, onValueChange = { answer4IsRight = it })

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            viewModel.addQuestion(
                Question(
                    questionId.toInt(),
                    questionText,
                    listOf(
                        Answer(answer1Id.toInt(), answer1Text, answer1IsRight.toBoolean()),
                        Answer(answer2Id.toInt(), answer2Text, answer2IsRight.toBoolean()),
                        Answer(answer3Id.toInt(), answer3Text, answer3IsRight.toBoolean()),
                        Answer(answer4Id.toInt(), answer4Text, answer4IsRight.toBoolean()),
                    )
                )
            )
            questionNumber = (questionNumber.toInt() + 1).toString()
        }) {
            Text(text = "Add question")
        }

        Button(onClick = { viewModel.addLevel() }) { Text(text = "Send") }

        Button(onClick = {
            questionId = ""
            questionText = ""
            answer1Id = "1"
            answer1Text = ""
            answer1IsRight = ""
            answer2Id = "2"
            answer2Text = ""
            answer2IsRight = ""
            answer3Id = "3"
            answer3Text = ""
            answer3IsRight = ""
            answer4Id = "4"
            answer4Text = ""
            answer4IsRight = ""
        }) {
            Text(text = "Clear")
        }
    }
}
