package com.guayaba.shotokankata.ui.quiz

data class QuizQuestion(
    val id: Long = 0,
    val question: String,
    val answer: String,
    val wrongAnswers: List<String>
) {
    private var _allAnswers: List<String>

    init {
        val list = mutableListOf(answer)
        list.addAll(wrongAnswers)
        _allAnswers = list.shuffled()
    }

    val allAnswers
        get() = _allAnswers

}