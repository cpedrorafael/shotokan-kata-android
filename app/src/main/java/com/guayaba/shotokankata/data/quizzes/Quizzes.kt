package com.guayaba.shotokankata.data.quizzes

import com.guayaba.shotokankata.ui.Routes

class Quizzes {
    companion object {
        val quizzes = listOf(
            Quiz(0, "How many moves in Kata")
        )

        fun mapQuizToRoute(quizId: Long): String {
            return when(quizId){
                0L -> Routes.WORD_QUIZ.route
                else -> throw IllegalStateException("Invalid Route")
            }
        }
    }
}