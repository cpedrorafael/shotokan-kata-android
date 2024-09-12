package com.guayaba.shotokankata.ui.quiz

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordQuiz(viewModel: QuizViewModel, navHostController: NavHostController, quizId: Int) {
    val state = viewModel.questionFlow.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.initWordQuiz(quizId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Quizzes", fontSize = TextUnit(30.0F, TextUnitType.Sp))
                }
            }, navigationIcon = {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
        }
    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            state.value.let { state ->
                if (state.isFinished) {
                    Column(Modifier.align(Alignment.Center)) {
                        Text("Yame!")
                        Text("Your score is: ${viewModel.getScore()}")
                        OutlinedButton(onClick = {
                            viewModel.initWordQuiz(quizId)
                        }) {
                           Text("Start Over")
                        }
                    }
                } else {
                    state.question?.let { question ->
                        QuestionView(question) { answer ->
                            viewModel.answerQuestion(answer)
                        }
                    } ?: run {
                        Text("Loading...", modifier = Modifier.align(Alignment.Center))
                    }
                }

            }
        }

    }
}


@Composable
fun QuestionView(question: QuizQuestion, onAnswer: (String) -> Unit) {
    Log.d("QuestionView", question.allAnswers.toString())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = question.question,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(64.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(question.allAnswers.size) {
                TextAnswerButton(answer = question.allAnswers[it]){ answer ->
                    onAnswer(answer)
                }
            }
        }
    }
}

@Composable
fun TextAnswerButton(answer: String, onAnswer: (String) -> Unit) {
    OutlinedButton(onClick = { onAnswer(answer) }, modifier = Modifier.padding(horizontal = 4.dp)) {
        Text(text = answer)
    }
}