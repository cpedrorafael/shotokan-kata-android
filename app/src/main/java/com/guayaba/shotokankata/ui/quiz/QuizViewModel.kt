package com.guayaba.shotokankata.ui.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guayaba.shotokankata.data.KataInfo
import com.guayaba.shotokankata.data.quizzes.QuizResult
import com.guayaba.shotokankata.ui.KataApplication.Companion.quizStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.LinkedList
import java.util.Queue

const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    private val _questionFlow: MutableStateFlow<QuizState> = MutableStateFlow(QuizState(null))
    val questionFlow: StateFlow<QuizState> = _questionFlow
    private val questions: Queue<QuizQuestion> = LinkedList()
    private var score = 0
    private var quizId = 0L

    private val _timerFlow: MutableStateFlow<TimerState> = MutableStateFlow(TimerState())
    val timerFlow: StateFlow<TimerState> = _timerFlow
    private var timerJob: Job? = null

    private fun saveResult(result: QuizResult) = viewModelScope.launch {
        quizStorage.saveResult(result)
    }

    fun initWordQuiz(quizId: Int) {
        score = 0
        this@QuizViewModel.quizId = quizId.toLong()
        questions.clear()

        when (quizId) {
            0 -> initKataMovesQuiz()
        }
    }

    private fun initKataMovesQuiz() {
        quizId = 0
        val questionItems = KataInfo.entries.map { it.moves }
        val resultList = mutableListOf<QuizQuestion>()
        for (i in 1..questionItems.size ) {
            val currentKata = KataInfo.findById(i)

            //get random indices to display wrong answers
            val wrongAnswers = questionItems
                .shuffled()
                .distinct()
                .filter { it != questionItems[i - 1] }
                .map { it.toString() }
                .take(3)

            val question = QuizQuestion(
                i.toLong(),
                "How many moves are there in ${currentKata.kataName}?",
                currentKata.moves.toString(),
                wrongAnswers
            )
            resultList.add(question)
        }

        questions.addAll(resultList.shuffled())

        loadNextQuestion()
    }

    fun answerQuestion(answer: String) {
        if (answer == questionFlow.value.question!!.answer) {
            score++
        }

        loadNextQuestion()
    }

    fun getScore() = score

    private fun loadNextQuestion() = viewModelScope.launch {
        Log.d(TAG, "loading question")
        if (questions.peek() == null) {
            _questionFlow.emit(QuizState(null, true))
            saveResult(
                QuizResult(
                    quizId = quizId,
                    score = score.toDouble(),
                    dateTime = LocalDateTime.now()
                )
            )
        } else {
            _questionFlow.emit(QuizState(questions.poll()))
            timerJob?.cancel()
            timerJob = startQuestionTimer()
        }
    }

    private fun startQuestionTimer() = viewModelScope.launch {
        var state = TimerState()
        _timerFlow.value = state

        delay(300L)

        while (state.tick < state.total) {
            delay(100L)
            val newState = TimerState(state.tick + 100L, state.total)
            state = newState
            _timerFlow.value = state
        }

        answerQuestion("")
    }


}

data class QuizState(val question: QuizQuestion?, val isFinished: Boolean = false)

data class TimerState(val tick: Long = 0, val total: Long = 10000L)