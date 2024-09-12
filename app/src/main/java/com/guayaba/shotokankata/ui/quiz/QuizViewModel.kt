package com.guayaba.shotokankata.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guayaba.shotokankata.data.quizzes.QuizResult
import com.guayaba.shotokankata.ui.KataApplication.Companion.quizStorage
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
    fun saveResult(result: QuizResult) = viewModelScope.launch {
        quizStorage.saveResult(result)
    }

}