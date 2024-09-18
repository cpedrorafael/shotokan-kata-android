package com.guayaba.shotokankata.ui.quiz

import CustomLinearProgressIndicator
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

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
                Text("Quizzes", fontSize = TextUnit(30.0F, TextUnitType.Sp), textAlign = TextAlign.Left)
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
                    Column(
                        Modifier
                            .align(Alignment.Center)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            "YAME!!",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp
                        )
                        Spacer(modifier = Modifier.height(64.dp))
                        Text(
                            "Your score is: ${viewModel.getScore()}%",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.initWordQuiz(quizId)
                            }) {
                            Text("Start Over")
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        val wrongAnswers = viewModel.getWrongAnswers()
                        Text(
                            "What you got wrong: ",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (wrongAnswers.isNotEmpty()) {
                            Column(Modifier.fillMaxWidth()) {
                                wrongAnswers.map {
                                    Text(
                                        text = it,
                                        Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                    }
                } else {
                    state.question?.let { question ->
                        QuestionView(question, viewModel) { answer ->
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
fun QuestionView(question: QuizQuestion, viewModel: QuizViewModel, onAnswer: (String) -> Unit) {
    Log.d("QuestionView", question.allAnswers.toString())

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
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
                val variant = question.allAnswers[it]
                TextAnswerButton(answer = variant) { answer ->
                    onAnswer(answer)
                }
            }
        }

        Spacer(Modifier.height(64.dp))

        QuestionTimer(viewmodel = viewModel)
    }
}

@Composable
fun TextAnswerButton(answer: String, onAnswer: (String) -> Unit) {
    OutlinedButton(onClick = { onAnswer(answer) }, modifier = Modifier.padding(horizontal = 4.dp)) {
        Text(text = answer)
    }
}

@Composable
fun QuestionTimer(viewmodel: QuizViewModel) {
    val state = viewmodel.timerFlow.collectAsState()

    CustomLinearProgressIndicator(
        progress = state.value.tick.toFloat() / state.value.total.toFloat(),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(16.dp)
            .padding(4.dp),
    )
}