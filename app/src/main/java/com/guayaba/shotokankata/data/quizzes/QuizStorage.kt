package com.guayaba.shotokankata.data.quizzes

abstract class QuizStorage {
    abstract fun saveResult(result: QuizResult)
    abstract suspend fun getAllResultsForQuiz(quizId: Long): List<QuizResult>
    abstract suspend fun getLastQuizResult(quizId: Long): QuizResult?
}