package com.guayaba.shotokankata.data.quizzes

import com.guayaba.shotokankata.ui.KataApplication.Companion.database
import com.guayaba.shotokankata.utils.AsyncUtil.Companion.ioScope
import kotlinx.coroutines.launch

class QuizStorageImpl : QuizStorage() {
    override fun saveResult(result: QuizResult) {
        ioScope.launch {
            database.quizDAO().insert(result)
        }
    }

    override suspend fun getLastQuizResult(quizId: Long): QuizResult? {
        return database.quizDAO().getLatestRecordForQuiz(quizId)
    }

    override suspend fun getAllResultsForQuiz(quizId: Long): List<QuizResult> {
        return database.quizDAO().getAllResultsForQuiz(quizId)
    }
}