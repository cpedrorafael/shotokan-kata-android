package com.guayaba.shotokankata.ui.quiz

data class QuizQuestion(
    val id: Long = 0,
    val question: String,
    val answer: String,
    val wrongAnswers: List<String>
) {
    val allAnswers: List<String>
        get() {
            val list = mutableListOf(answer)
            list.addAll(wrongAnswers)
            return list.shuffled()
        }
}